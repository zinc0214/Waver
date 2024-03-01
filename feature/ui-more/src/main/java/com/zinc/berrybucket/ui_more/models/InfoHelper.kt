package com.zinc.berrybucket.ui_more.models

import com.zinc.common.models.MyProfileInfo

fun MyProfileInfo.toUi() = UIMoreMyProfileInfo(
    name = name,
    imgUrl = imgUrl.orEmpty(),
    badgeUrl = badgeImgUrl,
    badgeTitle = badgeTitle,
    bio = bio.orEmpty()
)