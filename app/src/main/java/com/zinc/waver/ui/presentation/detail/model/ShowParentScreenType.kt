package com.zinc.waver.ui.presentation.detail.model

sealed interface ShowParentScreenType {
    data object Login : ShowParentScreenType
    data object Join : ShowParentScreenType
    data object Main : ShowParentScreenType
}