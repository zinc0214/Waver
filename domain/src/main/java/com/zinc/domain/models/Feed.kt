package com.zinc.domain.models

import com.zinc.common.models.YesOrNo

data class SavedKeywordItemsRequest(
    val keywordCodes: List<String>
)

data class FeedListResponse(
    val success: Boolean,
    val code: String,
    val message: String,
    val data: List<FeedItemResponse>
) {
    data class FeedItemResponse(
        val id: String,
        val status: FeedItemBucketStatus,
        val title: String,
        val images: List<String>,
        val user: UserInfo,
        val like: Int,
        val commentCount: Int,
        val scrapYn: YesOrNo?,
        val likeYn: YesOrNo?

    ) {
        enum class FeedItemBucketStatus {
            COMPLETE, PROGRESS
        }

        data class UserInfo(
            val id: String,
            val name: String,
            val imgUrl: String?,
            val badgeTitle: String?,
            val badgeImgUrl: String?
        )
    }
}

