package com.zinc.berrybucket.model

enum class AllType {
    MY, FEED, SEARCH, MORE
}

data class ProfileInfo(
    val profileImage: String,
    val badgeImage: String,
    val titlePosition: String,
    val nickName: String
) : DetailType()