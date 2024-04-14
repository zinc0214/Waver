package com.zinc.domain.models

import com.zinc.common.models.BucketStatus
import com.zinc.common.models.BucketType
import com.zinc.common.models.ExposureStatus

data class OtherBucketListResponse(
    val success: Boolean,
    val code: String,
    val message: String,
    val data: OtherBucketListData
) {
    data class OtherBucketListData(
        val bucketlist: List<BucketList>,
        val totalCount: Int,
        val completedCount: Int,
        val progressCount: Int
    )

    data class BucketList(
        val id: Int,
        val bucketType: BucketType,
        val title: String,
        val exposureStatus: ExposureStatus,
        val status: BucketStatus,
        val dDay: Int,
        val userCount: Int,
        val goalCount: Int
    )
}

data class OtherFollowDataResponse(
    val success: Boolean,
    val code: String,
    val message: String,
    val data: OtherFollowData
) {
    data class OtherFollowData(
        val followingCount: Int,
        val followerCount: Int
    )
}