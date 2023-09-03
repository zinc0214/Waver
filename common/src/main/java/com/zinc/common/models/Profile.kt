package com.zinc.common.models

data class OtherProfileInfo(
    val id: String,
    val imgUrl: String?,
    val name: String,
    val mutualFollow: Boolean
)

data class MyProfileResponse(
    val data: MyProfileInfo,
    val success: Boolean,
    val code: String,
    val message: String
)

data class MyProfileInfo(
    val email: String,
    val name: String,
    val imgUrl: String?,
    val bio: String,
    val badgeTitle: String,
    val badgeImgUrl: String
)