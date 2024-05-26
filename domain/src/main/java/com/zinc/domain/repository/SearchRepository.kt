package com.zinc.domain.repository

import com.zinc.common.models.CommonResponse
import com.zinc.domain.models.SearchRecommendResponse
import com.zinc.domain.models.SearchResultResponse

interface SearchRepository {
    //suspend fun loadRecommendList(token: String): SearchRecommendResponse // 이거아냐ㅠ
    suspend fun loadSearchResult(searchWord: String): SearchResultResponse
    suspend fun loadSearchRecommendList(): SearchRecommendResponse
    suspend fun deleteSearchRecentWord(word: String): CommonResponse
}