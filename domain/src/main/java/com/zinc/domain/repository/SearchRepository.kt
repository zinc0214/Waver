package com.zinc.domain.repository

import com.zinc.common.models.RecommendList
import com.zinc.domain.models.SearchResultResponse

interface SearchRepository {
    suspend fun loadRecommendList(): RecommendList
    suspend fun loadSearchResult(token: String, searchWord: String): SearchResultResponse
}