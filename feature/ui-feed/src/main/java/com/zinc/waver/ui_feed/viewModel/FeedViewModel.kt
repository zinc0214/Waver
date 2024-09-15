package com.zinc.waver.ui_feed.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.domain.usecases.common.SaveBucketLike
import com.zinc.domain.usecases.feed.CheckSavedKeywords
import com.zinc.domain.usecases.feed.LoadFeedItems
import com.zinc.domain.usecases.feed.LoadFeedKeyWords
import com.zinc.domain.usecases.feed.SavedKeywordItems
import com.zinc.waver.ui.viewmodel.CommonViewModel
import com.zinc.waver.ui_feed.models.UIFeedInfo
import com.zinc.waver.ui_feed.models.UIFeedKeyword
import com.zinc.waver.ui_feed.models.parseUI
import com.zinc.waver.ui_feed.models.toUIModel
import com.zinc.waver.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val checkSavedKeywords: CheckSavedKeywords,
    private val loadFeedKeyWords: LoadFeedKeyWords,
    private val loadFeedItems: LoadFeedItems,
    private val savedKeywordItems: SavedKeywordItems,
    private val saveBucketLike: SaveBucketLike
) : CommonViewModel() {
    private val _isKeyWordSelected = MutableLiveData<Boolean>()
    val isKeyWordSelected: LiveData<Boolean> get() = _isKeyWordSelected

    private val _feedKeyWords = MutableLiveData<List<UIFeedKeyword>>()
    val feedKeyWords: LiveData<List<UIFeedKeyword>> get() = _feedKeyWords

    private val _feedItems = MutableLiveData<List<UIFeedInfo>>()
    val feedItems: LiveData<List<UIFeedInfo>> get() = _feedItems

    private val _loadFail = SingleLiveEvent<Boolean>()
    val loadFail: LiveData<Boolean> get() = _loadFail

    private val _likeFail = MutableLiveData<Boolean>()
    val likeFail: LiveData<Boolean> get() = _likeFail

    fun checkSavedKeyWords() {
        _loadFail.value = false
        viewModelScope.launch(ceh(_loadFail, true)) {
            runCatching {
                checkSavedKeywords.invoke().apply {
                    Log.e("ayhan", "feed check response : $this")
                    if (this.code == "5000") {
                        _isKeyWordSelected.value = false
                    } else if (!success) {
                        _loadFail.value = true
                    } else {
                        _isKeyWordSelected.value = true
                    }
                }
            }.getOrElse {
                _loadFail.value = true
            }
        }
    }

    fun loadFeedKeyWords() {
        _loadFail.value = false
        viewModelScope.launch(ceh(_loadFail, true)) {
            runCatching {
                loadFeedKeyWords.invoke().apply {
                    Log.e("ayhan", "feed response : $this")
                    if (success) {
                        _feedKeyWords.value = data?.parseUI()
                    } else {
                        _loadFail.value = true
                    }
                }
            }.getOrElse {
                _loadFail.value = true
            }
        }
    }

    fun loadFeedItems() {
        viewModelScope.launch(ceh(_loadFail, true)) {
            runCatching {
                loadFeedItems.invoke().apply {
                    Log.e("ayhan", "feedListResponse : $this")
                    if (this.success.not()) {
                        _loadFail.value = true
                    } else {
                        _isKeyWordSelected.value = true
                        _feedItems.value = this.data.toUIModel()
                    }
                }
            }.getOrElse {
                _loadFail.value = true
            }

        }
    }

    fun savedKeywordList(list: List<Int>) {
        viewModelScope.launch(ceh(_isKeyWordSelected, false)) {
            runCatching {
                val response = savedKeywordItems.invoke(list)
                _isKeyWordSelected.value = response.success
            }.getOrElse {
                _loadFail.value = true
            }
        }
    }

    fun saveBucketLike(bucketId: String) {
        viewModelScope.launch(ceh(_likeFail, true)) {
            runCatching {
                val response = saveBucketLike.invoke(bucketId)
                Log.e("ayhan", "response : $response")
                if (response.success) {
                    loadFeedItems()
                    _likeFail.value = false
                } else {
                    _likeFail.value = true
                }
            }.getOrElse {
                _likeFail.value = true
            }
        }
    }
}
