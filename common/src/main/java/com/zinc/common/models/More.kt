package com.zinc.common.models

import kotlinx.serialization.Serializable

@Serializable
data class MoreMyProfileInfo(
    val name: String, // 프로필네임
    val imgUrl: String, // 프로필이미지
    val badgeType: BadgeType,
    val badgeTitle: String,
    val bio: String
)