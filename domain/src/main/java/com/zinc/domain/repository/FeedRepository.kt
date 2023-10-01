package com.zinc.domain.repository

import com.zinc.common.models.FeedInfo
import com.zinc.domain.models.FeedKeywordResponse

interface FeedRepository {
    suspend fun loadFeedKeyWords(): FeedKeywordResponse
    suspend fun loadFeedItems(): List<FeedInfo>
}