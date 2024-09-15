package com.zinc.domain.repository

import com.zinc.common.models.CommonResponse
import com.zinc.common.models.KeywordResponse
import com.zinc.domain.models.FeedListResponse
import com.zinc.domain.models.SavedKeywordItemsRequest

interface FeedRepository {
    suspend fun loadSelectableFeedKeyWords(): KeywordResponse
    suspend fun loadFeedItems(): FeedListResponse
    suspend fun savedKeywordItems(request: SavedKeywordItemsRequest): CommonResponse
    suspend fun checkSavedKeyWordItems(): CommonResponse
}