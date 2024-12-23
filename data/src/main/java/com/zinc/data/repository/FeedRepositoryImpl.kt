package com.zinc.data.repository

import com.zinc.common.models.KeywordResponse
import com.zinc.data.api.WaverApi
import com.zinc.domain.models.FeedListResponse
import com.zinc.domain.models.SavedKeywordItemsRequest
import com.zinc.domain.repository.FeedRepository
import javax.inject.Inject

internal class FeedRepositoryImpl @Inject constructor(
    private val waverApi: WaverApi
) : FeedRepository {
    override suspend fun loadSelectableFeedKeyWords(): KeywordResponse {
        return waverApi.loadKeywords()
    }

    override suspend fun loadFeedItems(): FeedListResponse {
        return waverApi.loadFeedItems()
    }

    override suspend fun savedKeywordItems(request: SavedKeywordItemsRequest) =
        waverApi.savedFeedKeywords(request)
}