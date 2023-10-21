package com.zinc.data.repository

import com.zinc.data.api.BerryBucketApi
import com.zinc.domain.models.FeedKeywordResponse
import com.zinc.domain.models.FeedListResponse
import com.zinc.domain.models.SavedKeywordItemsRequest
import com.zinc.domain.repository.FeedRepository
import javax.inject.Inject

internal class FeedRepositoryImpl @Inject constructor(
    private val berryBucketApi: BerryBucketApi
) : FeedRepository {
    override suspend fun loadFeedKeyWords(): FeedKeywordResponse {
        return berryBucketApi.loadFeedKeyword()
//        return listOf(
//            FeedKeyWord("1", "제주도"),
//            FeedKeyWord("2", "맛집탐방"),
//            FeedKeyWord("3", "넷플릭스"),
//            FeedKeyWord("4", "데이트"),
//            FeedKeyWord("5", "영화영화"),
//            FeedKeyWord("6", "제주도"),
//            FeedKeyWord("7", "맛집탐방"),
//            FeedKeyWord("8", "넷플릭스"),
//            FeedKeyWord("9", "데이트"),
//            FeedKeyWord("10", "영화영화"),
//            FeedKeyWord("11", "제주도"),
//            FeedKeyWord("12", "맛집탐방"),
//            FeedKeyWord("13", "넷플릭스"),
//            FeedKeyWord("14", "데이트"),
//            FeedKeyWord("15", "영화영화"),
//            FeedKeyWord("16", "영화영화"),
//            FeedKeyWord("17", "제주도"),
//            FeedKeyWord("18", "맛집탐방"),
//            FeedKeyWord("19", "넷플릭스"),
//            FeedKeyWord("20", "데이트"),
//            FeedKeyWord("21", "영화영화")
//        )
    }

    override suspend fun loadFeedItems(token: String): FeedListResponse {
        return berryBucketApi.loadFeedItems(token)
    }

    override suspend fun savedKeywordItems(token: String, request: SavedKeywordItemsRequest) =
        berryBucketApi.savedFeedKeywords(token, request)
}