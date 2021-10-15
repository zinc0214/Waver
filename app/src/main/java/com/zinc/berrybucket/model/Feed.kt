package com.zinc.berrybucket.model

data class FeedInfo(
    val titlePosition: String,
    val nickName: String,
    val isProcessing: Boolean,
    val title: String,
    val liked: Boolean,
    val likeCount: String,
    val commentCount: String,
    val copied: Boolean,
)