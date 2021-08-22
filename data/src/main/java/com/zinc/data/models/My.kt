package com.zinc.data.models

import com.zinc.domain.models.BadgeType
import kotlinx.serialization.Serializable

@Serializable
data class MyProfileInfo(
        val nickName: String,
        val profileImg: String,
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
