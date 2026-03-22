package com.zinc.waver.ui.presentation.model

import androidx.annotation.DrawableRes

data class BadgePopupInfo(
    @DrawableRes val badgeImage: Int,
    val badgeText: String,
    val badgeGrade: String
)