package com.zinc.domain.repository

import com.zinc.domain.models.SearchRecommendResponse
import com.zinc.domain.models.SearchResultResponse

interface SearchRepository {
    //suspend fun loadRecommendList(token: String): SearchRecommendResponse // 이거아냐ㅠ
    suspend fun loadSearchResult(token: String, searchWord: String): SearchResultResponse
    suspend fun loadSearchRecommendList(token: String): SearchRecommendResponse
}