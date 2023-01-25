package com.zinc.berrybucket.ui_more.models

import com.zinc.common.models.BadgeType

data class UIMoreMyProfileInfo(
    val name: String, // 프로필네임
    val imgUrl: String, // 프로필이미지
    val badgeType: BadgeType,
    val badgeTitle: String,
    val bio: String
)

enum class MoreItemType {
    ALARM, BLOCK, QNA, APP_INFO, LOGOUT
}

data class UIMoreItemData(
    val text: String,
    val type: MoreItemType
)