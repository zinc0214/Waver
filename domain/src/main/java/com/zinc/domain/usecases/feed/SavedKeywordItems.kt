package com.zinc.domain.usecases.feed

import com.zinc.common.models.CommonResponse
import com.zinc.domain.models.SavedKeywordItemsRequest
import com.zinc.domain.repository.FeedRepository
import javax.inject.Inject

class SavedKeywordItems @Inject constructor(
    private val feedRepository: FeedRepository
) {
    suspend operator fun invoke(list: List<Int>): CommonResponse {
        val request = SavedKeywordItemsRequest(keywordIds = list)
        return feedRepository.savedKeywordItems(request)
    }
}