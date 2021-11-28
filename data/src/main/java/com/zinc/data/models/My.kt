package com.zinc.data.models

import kotlinx.serialization.Serializable

@Serializable
data class MyProfileInfo(
    val nickName: String,
    val profileImg: String,
    val profileGrade: Float,
    val badgeType: BadgeType,
    val titlePosition: String,
    val bio: String
)

@Serializable
data class MyState(
    val followerCount: String,
    val followingCount: String,
    val hasAlarm: Boolean
)

@Serializable
data class DdayBucketList(
    val bucketList: List<BucketInfo>
)

@Serializable
data class AllBucketList(
    val processingCount: String,
    val succeedCount: String,
    val bucketList: List<BucketInfo>
)

