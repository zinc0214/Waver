package com.zinc.common.models

import kotlinx.serialization.Serializable

@Serializable
data class BlockedUserResponse(
    val data: List<BlockedUserInfo>,
    val success: Boolean,
    val code: String,
    val message: String
) {
    @Serializable
    data class BlockedUserInfo(
        val blockedUserId: Int,
        val blockedUserName: String,
        val blockedImgUrl: String
    )
}