package com.zinc.waver.ui_write.model

import com.zinc.waver.model.WriteOptionsType2

data class FriendsOptionInfo(
    val friendList: List<Friend>,
    val enableType: WriteOptionsType2.FRIENDS.EnableType
) {
    data class Friend(
        val userId: Int,
        val name: String
    )

    enum class EnableType {
        Enable, NoWaverPlus, Disable;
    }
}

data class OpenOptionInfo(
    val openType: OpenType
) {
    enum class OpenType {
        PUBLIC, PRIVATE, FRIEND
    }
}

data class ScrapOptionInfo(
    val isEnable: Boolean
)