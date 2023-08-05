package com.zinc.berrybucket.model

import com.zinc.common.models.FeedInfo

fun FeedInfo.profileInfo(): MyProfileInfoUi {
    return MyProfileInfoUi(
        profileImage = this.profileImage,
        badgeImage = this.badgeImage,
        titlePosition = this.titlePosition,
        nickName = this.nickName
    )
}