package com.zinc.data.repository

import com.zinc.common.models.FeedInfo
import com.zinc.data.api.BerryBucketApi
import com.zinc.domain.models.FeedKeywordResponse
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

    override suspend fun loadFeedItems(): List<FeedInfo> {
        //return berryBucketApi.loadFeedItems()
        return listOf(
            FeedInfo(
                bucketId = "1",
                profileImage = "",
                badgeImage = "",
                "멋쟁이 여행가",
                "한아크크룽삐옹",
                listOf("1", "2"),
                true,
                "제주도를 10번은 여행을 하고 말테양",
                false,
                "10",
                "5",
                false
            ),
            FeedInfo(
                bucketId = "2",
                profileImage = "",
                badgeImage = "",
                "노래방에 미친",
                "가수팝수 구텐탁",
                listOf("1", "2"),
                false,
                "노래방에서 노래 불러서 100점을 맞아버릴것이다",
                false,
                "100",
                "50",
                false
            ),
            FeedInfo(
                bucketId = "3",
                profileImage = "",
                badgeImage = "",
                "노래방에 미친",
                "나는야 주스될거야 나는야 케첩될거야 나는야 춤을 출꺼야",
                null,
                true,
                "멋쟁이 토마토, 토마토!",
                true,
                "100",
                "50",
                true
            ),
            FeedInfo(
                bucketId = "4",
                profileImage = "",
                badgeImage = "",
                "노래방에 미친",
                "나는야 주스될거야 나는야 케첩될거야 나는야 춤을 출꺼야",
                null,
                true,
                "멋쟁이 토마토, 토마토!",
                true,
                "100",
                "50",
                true
            ),
            FeedInfo(
                bucketId = "5",
                profileImage = "",
                badgeImage = "",
                "노래방에 미친",
                "나는야 주스될거야 나는야 케첩될거야 나는야 춤을 출꺼야",
                listOf("1", "2", "3", "4"),
                true,
                "멋쟁이 토마토, 토마토!",
                true,
                "100",
                "50",
                true
            )
        )
    }
}