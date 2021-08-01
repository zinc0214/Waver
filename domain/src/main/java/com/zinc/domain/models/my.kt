package com.zinc.domain.models

data class MyProfileInfo(
    val nickName: String,
    val profileImg: String,
    val badgeType: BadgeType,
    val titlePosition: String,
    val bio: String
)

data class MyState(
    val followerCount: Int,
    val followingCount: Int,
    val hasAlarm: Boolean
)

data class MyBucketList(
    val list: List<BucketInfo>
)

data class DdayBucketList(
    val list: List<BucketInfo>
)

enum class BadgeType {
    TRIP1, TRIP2, TRIP3
}

