package com.zinc.berrybucket.model

import androidx.annotation.Nullable

data class FeedInfo(
    val profileImage: String,
    val badgeImage: String,
    val titlePosition: String,
    val nickName: String,
    @Nullable val imageList: List<String>? = null,
    val isProcessing: Boolean,
    val title: String,
    val liked: Boolean,
    val likeCount: String,
    val commentCount: String,
    val copied: Boolean,
) {
    fun hasImage(): Boolean {
        return !imageList.isNullOrEmpty()
    }
}

fun FeedInfo.profileInfo(): ProfileInfo {
    return ProfileInfo(
        profileImage = this.profileImage,
        badgeImage = this.badgeImage,
        titlePosition = this.titlePosition,
        nickName = this.nickName
    )
}