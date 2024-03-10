package com.zinc.common.models

import kotlinx.serialization.Serializable

@Serializable
data class OtherProfileInfo(
    val id: String,
    val imgUrl: String?,
    val name: String,
    val mutualFollow: Boolean
)

@Serializable
data class ProfileResponse(
    val data: ProfileInfo,
    val success: Boolean,
    val code: String,
    val message: String
)

@Serializable
data class ProfileInfo(
    val email: String,
    val name: String,
    val imgUrl: String?,
    val bio: String?,
    val badgeTitle: String?,
    val badgeImgUrl: String?
)