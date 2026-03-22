package com.zinc.waver.ui_detail.model

sealed interface ShowParentScreenType {
    data object Login : ShowParentScreenType
    data object Join : ShowParentScreenType
    data object Main : ShowParentScreenType
    data object Alarm : ShowParentScreenType
    data object BadgeInfo : ShowParentScreenType
}