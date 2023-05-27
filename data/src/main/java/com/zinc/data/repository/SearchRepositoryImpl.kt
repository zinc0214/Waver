package com.zinc.data.repository

import com.zinc.common.models.RecommendItem
import com.zinc.common.models.RecommendList
import com.zinc.common.models.SearchBucketItem
import com.zinc.data.api.BerryBucketApi
import com.zinc.domain.repository.SearchRepository
import javax.inject.Inject

internal class SearchRepositoryImpl @Inject constructor(
    private val berryBucketApi: BerryBucketApi
) : SearchRepository {

    override suspend fun loadRecommendList(): RecommendList {
        //  return berryBucketApi.loadRecommendList()

        val bucketItem = listOf(
            SearchBucketItem(
                id = "1",
                thumbnail = "1",
                title = "버킷리스트 타이틀 2줄 가이드\n버킷리스트 타이틀 2줄일 경우",
                isCopied = false
            ),
            SearchBucketItem(
                id = "2",
                title = "버킷리스트 타이틀",
                isCopied = true
            ),
            SearchBucketItem(
                id = "3",
                title = "버킷리스트 타이틀 2줄 가이드\n버킷리스트 타이틀 2줄일 경우",
                isCopied = false
            ),
            SearchBucketItem(
                id = "4",
                title = "버킷리스트 타이틀",
                isCopied = true
            )
        )

        val popularItem = RecommendItem(
            type = "popular",
            tagList = listOf("여행", "공부", "문화"),
            items = bucketItem
        )

        val recommendItem = RecommendItem(
            type = "recommend",
            tagList = listOf("제주도", "1박2일", "가족여행"),
            items = bucketItem
        )

        return RecommendList(listOf(popularItem, recommendItem))
    }
}