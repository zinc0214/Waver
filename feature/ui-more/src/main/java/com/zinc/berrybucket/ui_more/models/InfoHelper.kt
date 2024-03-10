package com.zinc.berrybucket.ui_more.models

import com.zinc.common.models.ProfileInfo

fun ProfileInfo.toUi() = UIMoreMyProfileInfo(
    name = name,
    imgUrl = imgUrl.orEmpty(),
    badgeUrl = badgeImgUrl.orEmpty(),
    badgeTitle = badgeTitle.orEmpty(),
    bio = bio.orEmpty()
)