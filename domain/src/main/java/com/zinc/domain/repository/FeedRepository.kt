package com.zinc.domain.repository

import com.zinc.common.models.CommonResponse
import com.zinc.common.models.FeedInfo
import com.zinc.domain.models.FeedKeywordResponse
import com.zinc.domain.models.SavedKeywordItemsRequest

interface FeedRepository {
    suspend fun loadFeedKeyWords(): FeedKeywordResponse
    suspend fun loadFeedItems(): List<FeedInfo>
    suspend fun savedKeywordItems(token: String, request: SavedKeywordItemsRequest): CommonResponse
}