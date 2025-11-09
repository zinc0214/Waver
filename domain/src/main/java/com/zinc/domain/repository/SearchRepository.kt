package com.zinc.domain.repository

import com.zinc.common.models.CommonResponse
import com.zinc.domain.models.SearchPopularAndRecommendResponse
import com.zinc.domain.models.SearchRecommendResponse
import com.zinc.domain.models.SearchResultResponse

interface SearchRepository {
    suspend fun loadSearchPopularAndRecommend(): SearchPopularAndRecommendResponse
    suspend fun loadSearchResult(searchWord: String): SearchResultResponse
    suspend fun loadSearchRecommendList(): SearchRecommendResponse
    suspend fun deleteSearchRecentWord(word: String): CommonResponse
}