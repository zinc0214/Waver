package com.zinc.common.models

import kotlinx.serialization.Serializable

@Serializable
data class HomeProfileResponse(
    val data: HomeProfileInfo,
    val success: Boolean,
    val code: String,
    val message: String
)

@Serializable
data class HomeProfileInfo(
    val id: String,
    val name: String, // 프로필네임
    val imgUrl: String?, // 프로필이미지
    val badgeImgUrl: String?,
    val badgeTitle: String,
    val bio: String,
    val followingCount: String,
    val followerCount: String,
    val bucketInfo: HomeProfileBucketInfo
)

@Serializable
data class HomeProfileBucketInfo(
    val totalCount: Int,
    val completedCount: Int,
    val progressCount: Int
) {
    fun grade(): Float =
        if (totalCount == 0) 0.0f else (completedCount.toFloat() / totalCount.toFloat())
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
    val progressCount: Int,
    val completedCount: Int,
    val bucketlist: List<BucketInfoSimple>
)

@Serializable
data class AllBucketListRequest(
    val dDayBucketOnly: String?, // d-day 버킷여부
    val isPassed: String?, // d-day 지난 버킷 조회 여부
    val status: BucketStatus?, // 진행상태
    val sort: AllBucketListSortType
)

enum class AllBucketListSortType {
    ORDERED, CREATED, CREATED_DESC, UPDATED, UPDATED_DESC
}

@Serializable
enum class BucketStatus {
    COMPLETE, PROGRESS
}

@Serializable
enum class DdaySortType {
    MINUS, D_DAY, PLUS
}

@Serializable
data class FollowResponse(
    val data: FollowData,
    val success: Boolean,
    val code: String,
    val message: String
)

@Serializable
data class FollowData(
    val followingUsers: List<OtherProfileInfo>,
    val followerUsers: List<OtherProfileInfo>
)

@Serializable
data class BucketInfoSimple(
    val id: String,
    val bucketType: BucketType,
    val title: String,
    val exposureStatus: ExposureStatus,
    val status: BucketStatus,
    val dDay: Int? = null,
    var userCount: Int = 0,
    val goalCount: Int = 0
)
