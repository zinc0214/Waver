package com.zinc.domain.usecases.feed

import com.zinc.domain.repository.FeedRepository
import javax.inject.Inject

class LoadFeedItems @Inject constructor(
    private val feedRepository: FeedRepository
) {
    suspend operator fun invoke(token: String) = feedRepository.loadFeedItems(token)
}