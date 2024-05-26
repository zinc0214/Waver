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
    override suspend fun loadSelectableFeedKeyWords(): KeywordResponse {
        return berryBucketApi.loadKeywords()
    }

    override suspend fun loadFeedItems(): FeedListResponse {
        return berryBucketApi.loadFeedItems()
    }

    override suspend fun savedKeywordItems(request: SavedKeywordItemsRequest) =
        berryBucketApi.savedFeedKeywords(request)
}