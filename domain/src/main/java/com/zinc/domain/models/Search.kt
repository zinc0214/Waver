package com.zinc.domain.models

import com.zinc.common.models.BucketStatus
import com.zinc.common.models.BucketType
import com.zinc.common.models.ExposureStatus
import com.zinc.common.models.YesOrNo

data class SearchResultResponse(
    val data: SearchResult,
    val success: Boolean,
    val code: String,
    val message: String
) {
    data class SearchResult(
        val bucketlist: List<SearchResultBucketList>,
        val users: List<SearchResultUser>
    )

    data class SearchResultBucketList(
        val id: Int,
        val userId: String,
        val title: String,
        val scrapYn: YesOrNo?
    )

    data class SearchResultUser(
        val id: Int,
        val name: String,
        val followYn: YesOrNo
    )
}

data class SearchRecommendResponse(
    val data: RecommendData?,
    val success: Boolean,
    val code: String,
    val message: String
) {

    data class RecommendData(
        val recentSearch: List<String>,
        val recommendKeywords: List<RecommendItem>
    )

    data class RecommendItem(
        val id: Int,
        val name: String,
        val count: Int
    )
}

data class SearchPopularAndRecommendResponse(
    val data: RecommendData,
    val success: Boolean,
    val code: String,
    val message: String
) {
    data class RecommendData(
        val popularKeyword: List<String>,
        val popularList: List<BucketItem>,
        val recommendKeyword: List<String>,
        val recommendList: List<BucketItem>
    ) {
        data class BucketItem(
            val id: Int,
            val userId: String,
            val bucketType: BucketType,
            val title: String,
            val exposureStatus: ExposureStatus,
            val status: BucketStatus,
            val userCount: Int,
            val goalCount: Int,
            val likeCount: Int,
            val scrapYn: YesOrNo?,
            val keyword: List<RecommendKeyword>
        )

        data class RecommendKeyword(
            val code: String,
            val name: String
        )
    }
}