package com.zinc.common.models

import kotlinx.serialization.Serializable

@Serializable
data class MyProfileResponse(
    val data: MyProfileInfo,
    val success: Boolean,
    val code: String,
    val message: String
)

@Serializable
data class MyProfileInfo(
    val name: String, // 프로필네임
    val imgUrl: String, // 프로필이미지
    val badgeType: BadgeType,
    val badgeTitle: String,
    val bio: String,
    val followingCount: String,
    val followerCount: String,
    val bucketInfo: MyProfileBucketInfo
)

@Serializable
data class MyProfileBucketInfo(
    val totalCount: Int,
    val completedCount: Int,
    val progressCount: Int
) {
    fun grade(): Float = if (totalCount == 0) 0.0f else (totalCount / completedCount).toFloat()
}

@Serializable
data class MyState(
    val followerCount: String,
    val followingCount: String,
    val hasAlarm: Boolean
)

@Serializable
data class DdayBucketList(
    val bucketList: List<BucketInfoSimple>
)

@Serializable
data class AllBucketListResponse(
    val data: AllBucketList,
    val success: Boolean,
    val code: String,
    val message: String
)

@Serializable
data class AllBucketList(
    val totalCount: Int,
    val processingCount: Int,
    val completedCount: Int,
    val bucketlist: List<BucketInfoSimple>
)

@Serializable
data class AllBucketListRequest(
    val dDayBucketOnly: String, // d-day 버킷여부
    val isPassed: String,
    val status: BucketStatus?,
    val sort: AllBucketListSortType
)

enum class AllBucketListSortType {
    ORDERED, CREATED, CREATED_DESC, UPDATED, UPDATED_DESC
}

@Serializable
enum class BucketStatus {
    COMPLETE, PROGRESS
}

