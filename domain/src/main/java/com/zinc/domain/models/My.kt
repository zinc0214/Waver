package com.zinc.domain.models

import com.zinc.data.models.BadgeType

data class MyProfileInfo(
    val nickName: String,
    val profileImg: String,
    val badgeType: BadgeType,
    val titlePosition: String,
    val bio: String
)
