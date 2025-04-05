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
        val blockedImgUrl: String?
    )
}

@Serializable
data class LoadMyWaveInfoResponse(
    val data: MyWaveInfo,
    val success: Boolean,
    val code: String,
    val message: String
)

@Serializable
data class MyWaveInfo(
    val totalBadgeCount: Int,
    val totalLikeCount: Int,
    val totalBucketCount: Int,
    val currentBadge: Int,
    val badges: List<MyBadge>
)

@Serializable
data class MyBadge(
    val id: Int,
    val name: String,
    val step: Int
)

