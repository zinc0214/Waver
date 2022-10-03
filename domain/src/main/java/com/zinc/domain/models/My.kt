package com.zinc.domain.models

import com.zinc.common.models.BadgeType

data class TopProfile(
    val nickName: String,
    val profileImg: String?,
    val percent: Float,
    val badgeType: BadgeType?,
    val titlePosition: String?,
    val bio: String?,
    val followerCount: String,
    val followingCount: String
)
