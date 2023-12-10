package com.zinc.data.repository

import com.zinc.common.models.KeywordResponse
import com.zinc.data.api.BerryBucketApi
import com.zinc.domain.models.FeedListResponse
import com.zinc.domain.models.SavedKeywordItemsRequest
import com.zinc.domain.repository.FeedRepository
import javax.inject.Inject

internal class FeedRepositoryImpl @Inject constructor(
    private val berryBucketApi: BerryBucketApi
) : FeedRepository {
    override suspend fun loadSelectableFeedKeyWords(token: String): KeywordResponse {
        return berryBucketApi.loadKeywords(token)
    }

    override suspend fun loadFeedItems(token: String): FeedListResponse {
        return berryBucketApi.loadFeedItems(token)
    }

    override suspend fun savedKeywordItems(token: String, request: SavedKeywordItemsRequest) =
        berryBucketApi.savedFeedKeywords(token, request)
}