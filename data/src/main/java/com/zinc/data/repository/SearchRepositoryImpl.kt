package com.zinc.data.repository

import com.zinc.data.api.BerryBucketApi
import com.zinc.domain.models.SearchRecommendResponse
import com.zinc.domain.models.SearchResultResponse
import com.zinc.domain.repository.SearchRepository
import javax.inject.Inject

internal class SearchRepositoryImpl @Inject constructor(
    private val berryBucketApi: BerryBucketApi
) : SearchRepository {

//    override suspend fun loadRecommendList(token: String): SearchRecommendResponse {
//        return berryBucketApi.loadRecommendList(token)
//    }

    override suspend fun loadSearchResult(token: String, searchWord: String): SearchResultResponse {
        return berryBucketApi.loadSearchResult(token, searchWord)
    }

    override suspend fun loadSearchRecommendList(token: String): SearchRecommendResponse {
        return berryBucketApi.loadSearchRecommend(token)
    }
}