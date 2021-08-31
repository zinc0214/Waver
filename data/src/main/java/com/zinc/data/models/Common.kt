package com.zinc.data.models

import kotlinx.serialization.Serializable

data class BucketInfoSimple(
    val id: String,
    val title: String,
    val currentCount: Int,
    val goalCount: Int? = null,
    val dDay: Int? = null
) {
    val dDatText = dDay?.let {
        when {
            it == 0 -> "D-day"
            it < 0 -> "D$it"
            else -> "D+$it"
        }
    }
    val currentCountText = currentCount.toString()
    val goalCountText = goalCount?.toString()
}

@Serializable
data class BucketInfo(
    val id: String,
    val title: String,
    val memo: String? = null,
    val category: Category? = null,
    val currentCount: Int,
    val goalCount: Int? = null,
    val dDay: String? = null,
    val isOpen: Boolean,
    val imageList: List<String>? = null,
    val tag: List<String>? = null,
    val dDdayCount: Int? = null
)

@Serializable
data class Category(
    val id: Int,
    val name: String
)

data class CommentList(
    val commentList: List<CommentInfo>
)

data class CommentInfo(
    val profileImageView: String,
    val nickName: String,
    val comment: String,
    val time: String
)

enum class BadgeType {
    TRIP1, TRIP2, TRIP3
}