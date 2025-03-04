package com.zinc.waver.ui_feed.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.domain.usecases.common.CopyOtherBucket
import com.zinc.domain.usecases.common.SaveBucketLike
import com.zinc.domain.usecases.feed.LoadFeedItems
import com.zinc.domain.usecases.feed.LoadFeedKeyWords
import com.zinc.domain.usecases.feed.SavedKeywordItems
import com.zinc.waver.ui.viewmodel.CommonViewModel
import com.zinc.waver.ui_feed.models.FeedLoadStatus
import com.zinc.waver.ui_feed.models.UIFeedInfo
import com.zinc.waver.ui_feed.models.UIFeedKeyword
import com.zinc.waver.ui_feed.models.parseUI
import com.zinc.waver.ui_feed.models.toUIModel
import com.zinc.waver.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val loadFeedKeyWords: LoadFeedKeyWords,
    private val loadFeedItems: LoadFeedItems,
    private val savedKeywordItems: SavedKeywordItems,
    private val saveBucketLike: SaveBucketLike,
    private val copyOtherBucket: CopyOtherBucket
) : CommonViewModel() {
    private val _feedKeyWords = MutableLiveData<List<UIFeedKeyword>>()
    val feedKeyWords: LiveData<List<UIFeedKeyword>> get() = _feedKeyWords

    private val _feedItems = MutableLiveData<List<UIFeedInfo>>()
    val feedItems: LiveData<List<UIFeedInfo>> get() = _feedItems

    private val _loadStatusEvent = SingleLiveEvent<FeedLoadStatus>()
    val loadStatusEvent: LiveData<FeedLoadStatus> get() = _loadStatusEvent

    private val loadCeh = CoroutineExceptionHandler { _, _ ->
        val hasData = _feedItems.value != null
        _loadStatusEvent.value = FeedLoadStatus.LoadFail(hasData)
    }

    fun loadFeedItems() {
        _loadStatusEvent.value = FeedLoadStatus.RefreshLoading
        viewModelScope.launch(loadCeh) {
            runCatching {
                loadFeedItems.invoke().apply {
                    Log.e("ayhan", "feedListResponse : $this")
                    if (this.code == "5000") {
                        loadFeedKeyWords()
                    } else if (this.success.not()) {
                        _loadStatusEvent.value = FeedLoadStatus.LoadFail(false)
                    } else {
                        _feedItems.value = this.data.toUIModel()
                    }
                    _loadStatusEvent.value = FeedLoadStatus.Success
                }
            }.getOrElse {
                val hasData = _feedItems.value != null
                _loadStatusEvent.value = FeedLoadStatus.LoadFail(hasData)
            }
        }
    }

    fun savedKeywordList(list: List<String>) {
        _loadStatusEvent.value = FeedLoadStatus.None

        viewModelScope.launch(ceh(_loadStatusEvent, FeedLoadStatus.ToastFail)) {
            runCatching {
                val response = savedKeywordItems.invoke(list)
                if (response.success) {
                    loadFeedItems()
                } else {
                    _loadStatusEvent.value = FeedLoadStatus.ToastFail
                }
            }.getOrElse {
                _loadStatusEvent.value = FeedLoadStatus.ToastFail
            }
        }
    }

    fun saveBucketLike(bucketId: String) {
        _loadStatusEvent.value = FeedLoadStatus.None

        viewModelScope.launch(ceh(_loadStatusEvent, FeedLoadStatus.ToastFail)) {
            runCatching {
                val response = saveBucketLike.invoke(bucketId)
                Log.e("ayhan", "response : $response")
                if (response.success) {
                    loadFeedItems()
                } else {
                    _loadStatusEvent.value = FeedLoadStatus.ToastFail
                }
            }.getOrElse {
                _loadStatusEvent.value = FeedLoadStatus.ToastFail
            }
        }
    }

    fun copyOtherBucket(bucketId: String) {
        _loadStatusEvent.value = FeedLoadStatus.None

        viewModelScope.launch(ceh(_loadStatusEvent, FeedLoadStatus.ToastFail)) {
            runCatching {
                val response = copyOtherBucket.invoke(bucketId)
                Log.e("ayhan", "response : $response")
                if (response.success) {
                    loadFeedItems()
                    _loadStatusEvent.value = FeedLoadStatus.CopySuccess
                } else {
                    _loadStatusEvent.value = FeedLoadStatus.ToastFail
                }
            }.getOrElse {
                _loadStatusEvent.value = FeedLoadStatus.ToastFail
            }
        }
    }

    private fun loadFeedKeyWords() {
        _loadStatusEvent.value = FeedLoadStatus.KeywordLoading
        viewModelScope.launch(loadCeh) {
            runCatching {
                loadFeedKeyWords.invoke().apply {
                    Log.e("ayhan", "feed response : $this")
                    if (success) {
                        _feedKeyWords.value = data?.parseUI()
                        _loadStatusEvent.value = FeedLoadStatus.Success
                    } else {
                        _loadStatusEvent.value = FeedLoadStatus.LoadFail(false)
                    }
                }
            }.getOrElse {
                _loadStatusEvent.value = FeedLoadStatus.LoadFail(false)
            }
        }
    }
}
