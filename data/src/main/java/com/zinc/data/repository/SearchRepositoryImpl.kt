package com.zinc.data.repository

import com.zinc.common.models.CommonResponse
import com.zinc.data.api.WaverApi
import com.zinc.domain.models.SearchPopularAndRecommendResponse
import com.zinc.domain.models.SearchRecommendResponse
import com.zinc.domain.models.SearchResultResponse
import com.zinc.domain.repository.SearchRepository
import javax.inject.Inject

internal class SearchRepositoryImpl @Inject constructor(
    private val waverApi: WaverApi
) : SearchRepository {
    override suspend fun loadSearchPopularAndRecommend(): SearchPopularAndRecommendResponse {
        return waverApi.loadSearchPopularAndRecommend()
    }

    override suspend fun loadSearchResult(searchWord: String): SearchResultResponse {
        return waverApi.loadSearchResult(searchWord)
    }

    override suspend fun loadSearchRecommendList(): SearchRecommendResponse {
        return waverApi.loadSearchRecommend()
    }

    override suspend fun deleteSearchRecentWord(word: String): CommonResponse {
        return waverApi.deleteSearchRecentWord(word)
    }
}