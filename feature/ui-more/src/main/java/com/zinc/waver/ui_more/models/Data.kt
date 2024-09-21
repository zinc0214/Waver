package com.zinc.waver.ui_more.models

import androidx.compose.ui.graphics.painter.Painter
import com.zinc.waver.ui_more.R

data class UIMoreMyProfileInfo(
    val name: String, // 프로필네임
    val imgUrl: String, // 프로필이미지
    val badgeUrl: String,
    val badgeTitle: String,
    val bio: String
)

enum class MoreItemType {
    MY_WAVE, PROFILE, ALARM, BLOCK, QNA, APP_INFO, LOGOUT, WAVE_PLUS
}

data class UIMoreItemData(
    val text: String,
    val type: MoreItemType
)

interface AlarmSettingType {

    fun title(): Int
    fun desc(): Int?

    enum class AlarmSettingServiceType : AlarmSettingType {
        FOLLOWER, LIKE, D_DAY, COMMENT, FRIENDS;

        override fun title(): Int {
            return when (this) {
                FOLLOWER -> R.string.alarmSettingNewFollower
                LIKE -> R.string.alarmSettingLike
                D_DAY -> R.string.alarmSettingDday
                COMMENT -> R.string.alarmSettingComment
                FRIENDS -> R.string.alarmSettingFriends
            }
        }

        override fun desc(): Int? {
            return when (this) {
                FOLLOWER, LIKE -> null
                D_DAY -> R.string.alarmSettingDdayDesc
                COMMENT -> R.string.alarmSettingCommentDesc
                FRIENDS -> R.string.alarmSettingFriendsDesc
            }
        }
    }

    enum class AlarmSettingBenefitType : AlarmSettingType {
        EVENT, UPDATE;

        override fun title(): Int {
            return when (this) {
                EVENT -> R.string.alarmSettingEvent
                UPDATE -> R.string.alarmSettingUpdate
            }
        }

        override fun desc(): Int? {
            return when (this) {
                EVENT, UPDATE -> null
            }
        }
    }
}


data class AlarmSwitchState(
    val type: AlarmSettingType,
    var isOn: Boolean
)

data class BlockMemberData(
    val profileUrl: String,
    val nickName: String,
    val id: String
)

enum class AppInfoItemType {
    USE_TERMS, PERSONAL_TERMS, OPEN_SOURCE
}

data class AppInfoItemData(
    val text: String,
    val type: AppInfoItemType
)

data class MyWaveInfo(
    val badgeCount: Int,
    val likedCount: Int,
    val bucketCount: Int,
    val badgeUrl: String,
    val badgeTitle: String,
    val badgeList: List<BadgeInfo>
) {
    data class BadgeInfo(
        val url: String,
        val name: String,
        val grade: Int
    )
}

data class WaverPlusOption(
    val imgResource: Painter,
    val title: String,
    val content: String
)