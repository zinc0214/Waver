package com.zinc.berrybucket.ui.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.berrybucket.CommonViewModel
import com.zinc.berrybucket.model.RecommendItem
import com.zinc.berrybucket.model.RecommendList
import com.zinc.berrybucket.model.RecommendType
import com.zinc.berrybucket.model.SearchBucketItem
import com.zinc.berrybucket.model.SearchRecommendItems
import com.zinc.berrybucket.model.SearchResultItems
import com.zinc.berrybucket.model.parseUI
import com.zinc.berrybucket.util.SingleLiveEvent
import com.zinc.datastore.login.LoginPreferenceDataStoreModule
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
                _recommendList.value = loadRecommendListDummy()
            }
        }
    }

    fun loadSearchRecommendItems() {
        viewModelScope.launch(CEH(_loadFail, true)) {
            accessToken.value?.let { token ->
                runCatching {
                    val response = loadSearchRecommend.invoke(token)
                    if (response.success) {
                        _searchRecommendItems.value = response.data.parseUI()
                    } else {
                        _loadFail.value = true
                    }
                }.getOrElse {
                    _loadFail.value = true
                }
            }
        }
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

    fun deleteRecentWord(deleteWord: String) {
        val originRecentWords = _searchRecommendItems.value?.recentWords?.toMutableList()
        originRecentWords?.remove(deleteWord)
        _searchRecommendItems.value = _searchRecommendItems.value?.copy(
            recentWords = originRecentWords.orEmpty()
        )
    }

    private fun loadRecommendListDummy(): RecommendList {
        //  return berryBucketApi.loadRecommendList()

        val bucketItem = listOf(
            SearchBucketItem(
                id = "1",
                thumbnail = "1",
                title = "버킷리스트 타이틀 2줄 가이드\n버킷리스트 타이틀 2줄일 경우",
                isCopied = false
            ),
            SearchBucketItem(
                id = "2",
                title = "버킷리스트 타이틀",
                isCopied = true
            ),
            SearchBucketItem(
                id = "3",
                title = "버킷리스트 타이틀 2줄 가이드\n버킷리스트 타이틀 2줄일 경우",
                isCopied = false
            ),
            SearchBucketItem(
                id = "4",
                title = "버킷리스트 타이틀",
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
