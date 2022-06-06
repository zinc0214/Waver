package com.zinc.domain.repository

import com.zinc.common.models.RecommendList
import com.zinc.common.models.SearchRecommendCategory

interface SearchRepository {
    suspend fun loadSearchRecommendCategoryItems(): List<SearchRecommendCategory>
    suspend fun loadRecommendList(): RecommendList
}