package com.zinc.common.models

import kotlinx.serialization.Serializable
import org.jetbrains.annotations.Nullable

@Serializable
data class FeedKeyWord(
    val id: String,
    val ketWord: String
)

@Serializable
data class FeedInfo(
    val bucketId: String,
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