package com.zinc.domain.repository

import com.zinc.common.models.CommonResponse
import com.zinc.common.models.KeywordResponse
import com.zinc.domain.models.FeedListResponse
import com.zinc.domain.models.SavedKeywordItemsRequest

interface FeedRepository {
    suspend fun loadSelectableFeedKeyWords(token: String): KeywordResponse
    suspend fun loadFeedItems(token: String): FeedListResponse
    suspend fun savedKeywordItems(token: String, request: SavedKeywordItemsRequest): CommonResponse
}