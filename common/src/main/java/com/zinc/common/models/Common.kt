package com.zinc.common.models

import kotlinx.serialization.Serializable

@Serializable
data class BucketInfo(
    val id: String,
    val title: String,
    val memo: String? = null,
    val category: String? = null,
    val currentCount: Int,
    val goalCount: Int? = null,
    val dDay: String? = null,
    val isOpen: Boolean,
    val imageList: List<String>? = null,
    val tag: List<String>? = null,
    val dDdayCount: Int? = null
)

@Serializable
data class BucketInfoSimple(
    val type: String = "",
    val id: String,
    val title: String,
    var currentCount: Int = 0,
    val goalCount: Int = 0,
    val dDay: Int? = null,
    val status: BucketStatus,
    val detailType: DetailType
)

@Serializable
enum class BucketStatus {
    COMPLETE, PROGRESS
}

@Serializable
enum class DetailType {
    MY_CLOSE, MY_OPEN, OTHER_OPEN
}

@Serializable
data class CategoryInfo(
    val id: Int,
    val name: String,
    val defaultYn: YesOrNo = YesOrNo.Y,
    val bucketlistCount: String
)

@Serializable
data class CommentList(
    val commentList: List<CommentInfo>
)

@Serializable
data class CommentInfo(
    val profileImageView: String,
    val nickName: String,
    val comment: String,
    val time: String
)

enum class BadgeType {
    TRIP1, TRIP2, TRIP3
}

enum class YesOrNo {
    Y, N;

    fun isYes() = this == Y
    fun isNo() = this == N
}