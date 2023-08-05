package com.zinc.berrybucket.model

enum class AllType {
    MY, FEED, SEARCH, MORE
}

data class MyProfileInfoUi(
    val profileImage: String?,
    val badgeImage: String,
    val titlePosition: String,
    val nickName: String
) : DetailDescType() {
    fun toUi() = UiProfileInfo(
        profileImage = this.profileImage,
        badgeImage = this.badgeImage,
        titlePosition = this.titlePosition,
        nickName = this.nickName
    )
}

sealed class IconClickEvent