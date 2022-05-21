package com.zinc.domain.repository

import com.zinc.common.models.FeedInfo
import com.zinc.common.models.FeedKeyWord

interface FeedRepository {
    suspend fun loadFeedKeyWords(): List<FeedKeyWord>
    suspend fun loadFeedItems() : List<FeedInfo>
}