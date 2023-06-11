package com.zinc.domain.models

import com.zinc.common.models.BadgeType

data class TopProfile(
    val name: String,
    val imgUrl: String?,
    val percent: Float,
    val badgeType: BadgeType?,
    val badgeTitle: String?,
    val bio: String?,
    val followerCount: String,
    val followingCount: String
)
