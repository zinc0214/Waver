package com.zinc.common.models

import kotlinx.serialization.Serializable

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
