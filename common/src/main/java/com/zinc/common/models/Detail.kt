package com.zinc.common.models

import kotlinx.serialization.Serializable

@Serializable
data class BucketDetailResponse(
    val data: DetailInfo,
    val success: Boolean,
    val code: String,
    val message: String
)

@Serializable
data class DetailInfo(
    val id: String,
    val userId: String,
    val title: String,
    val memo: String?,
    val exposureStatus: ExposureStatus,
    val status: CompleteStatus,
    val pin: String,
    val scrapYn: YesOrNo,
    val isMine: YesOrNo,
    val categoryId: Int,
    val categoryName: String,
    val goalCount: Int,
    val userCount: Int,
    val completedDt: String?,
    val keywordIds: List<Int>?,
    val keywords: List<String>?,
    val friends: List<Friend>?,
    val images: List<String>?,
    val comment: List<Comment>?
) {
    enum class CompleteStatus {
        PROGRESS, COMPLETE
    }


    enum class ExposureStatus {
        PUBLIC, FOLLOWER, PRIVATE
    }

    @Serializable
    data class Comment(
        val id: String,
        val isMyComment: YesOrNo,
        val userId: String,
        val imgUrl: String?,
        val name: String,
        val content: String,
        val isBlocked: YesOrNo
    )

    @Serializable
    data class Friend(
        val name: String,
        val id: String
    )
}

data class AddBucketCommentRequest(
    val bucketlistId: Int,
    val content: String,
    val mentionIds: String
)

