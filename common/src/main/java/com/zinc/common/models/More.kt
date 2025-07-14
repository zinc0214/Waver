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
data class LoadMyWaveBadgeResponse(
    val data: List<MyBadge>,
    val success: Boolean,
    val code: String,
    val message: String
)

@Serializable
data class LoadMyWaveInfoResponse(
    val data: MyWaverInfo,
    val success: Boolean,
    val code: String,
    val message: String
)

@Serializable
data class MyWaverInfo(
    val totalBadgeCount: Int,
    val totalLikeCount: Int,
    val totalBucketCount: Int,
    val badgeImgUrl: String
)

@Serializable
data class MyBadge(
    val title: String,
    val imgUrl: String,
    val step: Step
) {
    enum class Step {
        STEP0, STEP1, STEP2, STEP3;

        fun text() = when (this) {
            STEP0 -> "0단계"
            STEP1 -> "1단계"
            STEP2 -> "2단계"
            STEP3 -> "3단계"
        }
    }
}
