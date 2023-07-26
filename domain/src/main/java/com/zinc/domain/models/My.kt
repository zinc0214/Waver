package com.zinc.domain.models

data class TopProfile(
    val name: String,
    val imgUrl: String?,
    val percent: Float,
    val badgeType: String?,
    val badgeTitle: String?,
    val bio: String?,
    val followerCount: String,
    val followingCount: String
)
