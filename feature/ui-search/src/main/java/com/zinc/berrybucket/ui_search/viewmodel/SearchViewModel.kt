package com.zinc.berrybucket.ui_search.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.berrybucket.ui.viewmodel.CommonViewModel
import com.zinc.berrybucket.ui_search.model.RecommendItem
import com.zinc.berrybucket.ui_search.model.RecommendList
import com.zinc.berrybucket.ui_search.model.RecommendType
import com.zinc.berrybucket.ui_search.model.SearchBucketItem
import com.zinc.berrybucket.ui_search.model.SearchRecommendItems
import com.zinc.berrybucket.ui_search.model.SearchResultItems
import com.zinc.berrybucket.ui_search.model.parseUI
import com.zinc.berrybucket.util.SingleLiveEvent
import com.zinc.domain.usecases.other.RequestFollowUser
import com.zinc.domain.usecases.other.RequestUnfollowUser
import com.zinc.domain.usecases.search.DeleteRecentWord
import com.zinc.domain.usecases.search.LoadSearchRecommend
import com.zinc.domain.usecases.search.LoadSearchResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
//    private val loadRecommendList: LoadRecommendList,
    private val loadSearchResult: LoadSearchResult,
    private val loadSearchRecommend: LoadSearchRecommend,
    private val deleteRecentWord: DeleteRecentWord,
    private val requestFollow: RequestFollowUser,
    private val requestUnFollow: RequestUnfollowUser
) : CommonViewModel() {

    private val _recommendList = MutableLiveData<RecommendList>()
    val recommendList: LiveData<RecommendList> get() = _recommendList

    private val _searchRecommendItems = MutableLiveData<SearchRecommendItems>()
    val searchRecommendItems: LiveData<SearchRecommendItems> get() = _searchRecommendItems

    private val _searchResultItems = MutableLiveData<SearchResultItems>()
    val searchResultItems: LiveData<SearchResultItems> get() = _searchResultItems

    private val _searchResultIsEmpty = MutableLiveData<Boolean>()
    val searchResultIsEmpty: LiveData<Boolean> get() = _searchResultIsEmpty

    private val _loadFail = MutableLiveData<String?>(null)
    val loadFail: LiveData<String?> get() = _loadFail

    private val _actionFail = SingleLiveEvent<Nothing>()
    val actionFail: LiveData<Nothing> get() = _actionFail

    var prevSearchWord: String = ""

    fun loadRecommendList() {
        viewModelScope.launch {
            runCatching {
                _recommendList.value = loadRecommendListDummy()
            }
        }
    }

    fun loadSearchRecommendItems() {
        viewModelScope.launch(ceh(_loadFail, "")) {
            val response = loadSearchRecommend.invoke()
            if (response.success) {
                _searchRecommendItems.value = response.data.parseUI()
            } else {
                _loadFail.value = response.message
            }
        }
    }

    fun loadSearchResult(searchWord: String) {
        viewModelScope.launch(ceh(_loadFail, "")) {
            _searchResultIsEmpty.value = false
            runCatching {
                val response = loadSearchResult.invoke(searchWord)
                Log.e("ayhan", "loadSearchResult : $response")
                if (response.success) {
                    prevSearchWord = searchWord
                    if (response.data.bucketlist.isEmpty() && response.data.users.isEmpty()) {
                        _searchResultIsEmpty.value = true
                    } else {
                        _searchResultIsEmpty.value = false
                        _searchResultItems.value = response.data.parseUI()
                    }

                } else {
                    _loadFail.value = response.message
                    _searchResultIsEmpty.value = true
                }
            }.getOrElse {
                _loadFail.value = ""
                _searchResultIsEmpty.value = true
            }
        }
    }

    fun deleteRecentWord(deleteWord: String) {
        viewModelScope.launch(ceh(_loadFail, "")) {
            runCatching {
                val response = deleteRecentWord.invoke(deleteWord)
                if (response.success) {
                    val originRecentWords =
                        _searchRecommendItems.value?.recentWords?.toMutableList()
                    originRecentWords?.remove(deleteWord)
                    _searchRecommendItems.value = _searchRecommendItems.value?.copy(
                        recentWords = originRecentWords.orEmpty()
                    )
                } else {
                    _loadFail.value = response.message
                }
            }.getOrElse {
                _loadFail.value = ""
            }
        }
    }

    fun requestFollow(userId: String, isAlreadyFollowed: Boolean) {
        viewModelScope.launch(ceh(_actionFail, null)) {
            runCatching {
                if (isAlreadyFollowed) {
                    val response = requestUnFollow.invoke(userId)
                    if (response.success) {
                        loadSearchResult(prevSearchWord)
                    } else {
                        _actionFail.call()
                    }
                } else {
                    val response = requestFollow.invoke(userId)
                    if (response.success) {
                        loadSearchResult(prevSearchWord)
                    } else {
                        _actionFail.call()
                    }
                }
            }
        }
    }

    private fun loadRecommendListDummy(): RecommendList {
        //  return berryBucketApi.loadRecommendList()

        val bucketItem = listOf(
            SearchBucketItem(
                bucketId = "1",
                thumbnail = "1",
                title = "버킷리스트 타이틀 2줄 가이드\n버킷리스트 타이틀 2줄일 경우",
                writerId = "32",
                isCopied = false
            ),
            SearchBucketItem(
                bucketId = "2",
                title = "버킷리스트 타이틀",
                writerId = "32",
                isCopied = true
            ),
            SearchBucketItem(
                bucketId = "3",
                title = "버킷리스트 타이틀 2줄 가이드\n버킷리스트 타이틀 2줄일 경우",
                writerId = "32",
                isCopied = false
            ),
            SearchBucketItem(
                bucketId = "4",
                title = "버킷리스트 타이틀",
                writerId = "32",
                isCopied = true
            )
        )

        val popularItem = RecommendItem(
            type = RecommendType.POPULAR,
            tagList = listOf("여행", "공부", "문화"),
            items = bucketItem
        )

        val recommendItem = RecommendItem(
            type = RecommendType.RECOMMEND,
            tagList = listOf("제주도", "1박2일", "가족여행"),
            items = bucketItem
        )

        return RecommendList(listOf(popularItem, recommendItem))
    }
}
