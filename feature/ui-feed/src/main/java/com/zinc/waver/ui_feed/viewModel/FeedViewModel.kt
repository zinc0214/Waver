package com.zinc.waver.ui_feed.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.datastore.login.PreferenceDataStoreModule
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val loadFeedKeyWords: LoadFeedKeyWords,
    private val loadFeedItems: LoadFeedItems,
    private val savedKeywordItems: SavedKeywordItems,
    private val saveBucketLike: SaveBucketLike,
    private val copyOtherBucket: CopyOtherBucket,
    private val preferenceDataStoreModule: PreferenceDataStoreModule,
) : CommonViewModel() {
    private val _feedKeyWords = MutableLiveData<List<UIFeedKeyword>>()
    val feedKeyWords: LiveData<List<UIFeedKeyword>> get() = _feedKeyWords

    private val _feedItems = MutableLiveData<List<UIFeedInfo>>()
    val feedItems: LiveData<List<UIFeedInfo>> get() = _feedItems

    private val _loadStatusEvent = SingleLiveEvent<FeedLoadStatus>()
    val loadStatusEvent: LiveData<FeedLoadStatus> get() = _loadStatusEvent

    private val _hasFeedNextPage = MutableLiveData<Boolean>()
    val hasFeedNextPage: LiveData<Boolean> get() = _hasFeedNextPage

    private val _pageEndIndices = MutableLiveData<List<Int>>(emptyList())
    val pageEndIndices: LiveData<List<Int>> get() = _pageEndIndices

    private val loadCeh = CoroutineExceptionHandler { _, t ->
        Log.e("ayhan", "loadCeh : ${t.message}")
        val hasData = _feedItems.value != null
        _loadStatusEvent.value = FeedLoadStatus.LoadFail(hasData)
    }

    private var userId: String = ""
    private var feedNextKey: Int? = null

    init {
        viewModelScope.launch {
            preferenceDataStoreModule.loadUserIdKey.collectLatest { value ->
                userId = value
            }
        }
    }

    fun loadFeedItems(isRefresh: Boolean = true) {
        if (isRefresh) {
            feedNextKey = null
            _loadStatusEvent.value = FeedLoadStatus.RefreshLoading
        } else {
            _loadStatusEvent.value = FeedLoadStatus.PagingLoading
        }
        viewModelScope.launch(loadCeh) {
            loadFeedItems.invoke(feedNextKey).apply {
                Log.e("ayhan", "feedListResponse : $this")
                if (this.code == "8000") {
                    loadFeedKeyWords()
                } else if (this.success.not()) {
                    _loadStatusEvent.value = FeedLoadStatus.LoadFail(_feedItems.value != null)
                } else {
                    val newItems = this.toUIModel(userId)
                    val combined = if (isRefresh) {
                        newItems
                    } else {
                        _feedItems.value.orEmpty() + newItems
                    }
                    _feedItems.value = combined
                    _pageEndIndices.value = if (newItems.isNotEmpty()) {
                        val boundary = combined.size - 1
                        if (isRefresh) listOf(boundary)
                        else _pageEndIndices.value.orEmpty() + boundary
                    } else if (isRefresh) {
                        emptyList()
                    } else {
                        _pageEndIndices.value.orEmpty()
                    }
                    _hasFeedNextPage.value = this.data.hasNext
                    feedNextKey = this.data.nextKey
                }
                _loadStatusEvent.value = FeedLoadStatus.Success
            }
        }
    }

    fun loadNextFeedItems() {
        if (_hasFeedNextPage.value != true) {
            return
        }
        if (_loadStatusEvent.value == FeedLoadStatus.PagingLoading) {
            return
        }
        loadFeedItems(isRefresh = false)
    }


    fun savedKeywordList(list: List<String>) {
        _loadStatusEvent.value = FeedLoadStatus.None

        viewModelScope.launch(ceh(_loadStatusEvent, FeedLoadStatus.ToastFail)) {
            val response = savedKeywordItems.invoke(list)
            Log.e("ayhan", "response : $response")
            if (response.success) {
                loadFeedItems()
            } else {
                _loadStatusEvent.value = FeedLoadStatus.ToastFail
            }
        }
    }

    fun saveBucketLike(bucketId: String) {
        _loadStatusEvent.value = FeedLoadStatus.None

        viewModelScope.launch(ceh(_loadStatusEvent, FeedLoadStatus.ToastFail)) {
            val response = saveBucketLike.invoke(bucketId)
            Log.e("ayhan", "response : $response")
            if (response.success) {
                loadFeedItems()
            } else {
                _loadStatusEvent.value = FeedLoadStatus.ToastFail
            }
        }
    }

    fun copyOtherBucket(bucketId: String) {
        _loadStatusEvent.value = FeedLoadStatus.None

        viewModelScope.launch(ceh(_loadStatusEvent, FeedLoadStatus.ToastFail)) {
            val response = copyOtherBucket.invoke(bucketId)
            Log.e("ayhan", "copyOtherBucket response : $response")
            if (response.success) {
                loadFeedItems()
                _loadStatusEvent.value = FeedLoadStatus.CopySuccess
            } else {
                _loadStatusEvent.value = FeedLoadStatus.ToastFail
            }
        }
    }

    private fun loadFeedKeyWords() {
        _loadStatusEvent.value = FeedLoadStatus.KeywordLoading
        viewModelScope.launch(loadCeh) {
            loadFeedKeyWords.invoke().apply {
                Log.e("ayhan", "feed response : $this")
                if (success) {
                    _feedKeyWords.value = data?.parseUI()
                    _loadStatusEvent.value = FeedLoadStatus.Success
                } else {
                    _loadStatusEvent.value = FeedLoadStatus.LoadFail(false)
                }
            }
        }
    }
}
