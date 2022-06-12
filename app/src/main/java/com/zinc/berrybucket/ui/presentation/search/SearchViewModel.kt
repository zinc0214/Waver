package com.zinc.berrybucket.ui.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zinc.common.models.RecommendList
import com.zinc.common.models.SearchRecommendCategory
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
}