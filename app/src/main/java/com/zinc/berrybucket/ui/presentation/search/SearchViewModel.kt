package com.zinc.berrybucket.ui.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.berrybucket.CommonViewModel
import com.zinc.berrybucket.model.parseUI
import com.zinc.berrybucket.util.SingleLiveEvent
import com.zinc.common.models.KeyWordItem
import com.zinc.common.models.RecentItem
import com.zinc.common.models.RecommendList
import com.zinc.common.models.SearchRecommendItems
import com.zinc.common.models.SearchResultItems
import com.zinc.datastore.login.LoginPreferenceDataStoreModule
import com.zinc.domain.usecases.search.LoadRecommendList
import com.zinc.domain.usecases.search.LoadSearchResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val loadRecommendList: LoadRecommendList,
    private val loadSearchResult: LoadSearchResult,
    loginPreferenceDataStoreModule: LoginPreferenceDataStoreModule
) : CommonViewModel(loginPreferenceDataStoreModule) {

    private val _recommendList = MutableLiveData<RecommendList>()
    val recommendList: LiveData<RecommendList> get() = _recommendList

    private val _searchRecommendItems = MutableLiveData<SearchRecommendItems>()
    val searchRecommendItems: LiveData<SearchRecommendItems> get() = _searchRecommendItems

    private val _searchResultItems = MutableLiveData<SearchResultItems>()
    val searchResultItems: LiveData<SearchResultItems> get() = _searchResultItems

    private val _loadFail = SingleLiveEvent<Boolean>()
    val loadFail: LiveData<Boolean> get() = _loadFail

    fun loadRecommendList() {
        viewModelScope.launch {
            runCatching {
                loadRecommendList.invoke().apply {
                    _recommendList.value = this
                }
            }.getOrElse {

            }
        }
    }

    fun loadSearchRecommendItems() {
        _searchRecommendItems.value = SearchRecommendItems(
            recentWords = listOf(RecentItem("1", "여행"), RecentItem("2", "맛집")),
            recommendWords = listOf(
                KeyWordItem("2", "여행", "22"),
                KeyWordItem("3", "여행", "22"),
                KeyWordItem("4", "여행", "22"),
                KeyWordItem("5", "여행", "22")
            )
        )
    }

    fun loadSearchResult(searchWord: String) {
        viewModelScope.launch(CEH(_loadFail, true)) {
            accessToken.value?.let { token ->
                runCatching {
                    val response = loadSearchResult.invoke(token, searchWord)
                    if (response.success) {
                        _searchResultItems.value = response.data.parseUI()
                    } else {
                        _loadFail.value = true
                    }
                }.getOrElse {
                    _loadFail.value = true
                }
            }
        }
        // 여기
//        _searchResultItems.value = SearchResultItems(
//            bucketItems = listOf(
//                SearchBucketItem(id = "1", title = "버킷리스트 타이틀", isCopied = false),
//                SearchBucketItem(id = "2", title = "버킷리스트 타이틀", isCopied = false),
//                SearchBucketItem(
//                    id = "3",
//                    title = "버킷리스트 타이틀버킷리스트 타이버킷리스트 타이버킷리스트타이버킷리스트 타이버킷리스트타이버킷리스트 타이버킷리스트타이버킷리스트 타이버킷리스트타이버킷리스트 타이버킷리스트 타이",
//                    isCopied = true
//                ),
//                SearchBucketItem(id = "4", title = "버킷리스트 타이틀버킷리스트", isCopied = true)
//            ),
//            userItems = listOf(
//                UserItem(
//                    userId = "A", profileImageUrl = "aaa", nickName = "아가가나날라", isFollowed = true
//                ),
//                UserItem(
//                    userId = "B", profileImageUrl = "aaa", nickName = "아가가나날라", isFollowed = true
//                ),
//                UserItem(
//                    userId = "C", profileImageUrl = "aaa", nickName = "아가가나날라", isFollowed = true
//                ),
//                UserItem(
//                    userId = "D", profileImageUrl = "aaa", nickName = "아가가나날라", isFollowed = true
//                ),
//                UserItem(
//                    userId = "E", profileImageUrl = "aaa", nickName = "아가가나날라", isFollowed = true
//                ),
//                UserItem(
//                    userId = "F", profileImageUrl = "aaa", nickName = "아가가나날라", isFollowed = true
//                )
//            )
//        )
    }

    fun deleteRecentWord(deleteItem: RecentItem) {
        val originRecentWords = _searchRecommendItems.value?.recentWords?.toMutableList()
        originRecentWords?.remove(deleteItem)
        _searchRecommendItems.value = _searchRecommendItems.value?.copy(
            recentWords = originRecentWords.orEmpty()
        )
    }
}