package com.zinc.berrybucket.model

import androidx.annotation.Nullable

data class FeedInfo(
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