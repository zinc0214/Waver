package com.zinc.berrybucket.ui.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zinc.common.models.*
import com.zinc.domain.usecases.search.LoadRecommendList
import com.zinc.domain.usecases.search.LoadSearchRecommendCategoryItems
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val loadSearchRecommendCategoryItems: LoadSearchRecommendCategoryItems,
    private val loadRecommendList: LoadRecommendList
) : ViewModel() {

    private val _recommendCategoryItems = MutableLiveData<List<SearchRecommendCategory>>()
    val recommendCategoryItems: LiveData<List<SearchRecommendCategory>> get() = _recommendCategoryItems

    private val _recommendList = MutableLiveData<RecommendList>()
    val recommendList: LiveData<RecommendList> get() = _recommendList

    private val _searchRecommendItems = MutableLiveData<SearchRecommendItems>()
    val searchRecommendItems: LiveData<SearchRecommendItems> get() = _searchRecommendItems

    private val _searchResultItems = MutableLiveData<SearchResultItems>()
    val searchResultItems: LiveData<SearchResultItems> get() = _searchResultItems

    fun loadSearchRecommendCategoryItems() {
        viewModelScope.launch {
            runCatching {
                loadSearchRecommendCategoryItems.invoke().apply {
                    _recommendCategoryItems.value = this
                }
            }.getOrElse {

            }
        }
    }

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
            recommendWords = listOf(KeyWordItem("1", "여행", "22"))
        )
    }

    fun loadSearchResult(searchWord: String) {
        _searchResultItems.value = SearchResultItems(
            bucketItems = listOf(
                SearchBucketItem(id = "1", title = "버킷리스트 타이틀", isCopied = false),
                SearchBucketItem(id = "2", title = "버킷리스트 타이틀", isCopied = false),
                SearchBucketItem(
                    id = "3",
                    title = "버킷리스트 타이틀버킷리스트 타이버킷리스트 타이버킷리스트타이버킷리스트 타이버킷리스트타이버킷리스트 타이버킷리스트타이버킷리스트 타이버킷리스트타이버킷리스트 타이버킷리스트 타이",
                    isCopied = true
                ),
                SearchBucketItem(id = "4", title = "버킷리스트 타이틀버킷리스트", isCopied = true)
            ),
            userItems = listOf(
                UserItem(
                    userId = "A", profileImageUrl = "aaa", nickName = "아가가나날라", isFollowed = true
                ),
                UserItem(
                    userId = "B", profileImageUrl = "aaa", nickName = "아가가나날라", isFollowed = true
                ),
                UserItem(
                    userId = "C", profileImageUrl = "aaa", nickName = "아가가나날라", isFollowed = true
                ),
                UserItem(
                    userId = "D", profileImageUrl = "aaa", nickName = "아가가나날라", isFollowed = true
                ),
                UserItem(
                    userId = "E", profileImageUrl = "aaa", nickName = "아가가나날라", isFollowed = true
                ),
                UserItem(
                    userId = "F", profileImageUrl = "aaa", nickName = "아가가나날라", isFollowed = true
                )
            )
        )
    }
}