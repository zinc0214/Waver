package com.zinc.common.models

import kotlinx.serialization.Serializable

@Serializable
data class BucketInfoSimple(
    val id: String,
    val bucketType: BucketType,
    val title: String,
    val status: BucketStatus,
    val dDay: Int? = null,
    var userCount: Int = 0,
    val goalCount: Int = 0,
    val detailType: DetailType? = DetailType.MY_OPEN
)

@Serializable
enum class DetailType {
    MY_CLOSE, MY_OPEN, OTHER_OPEN
}
