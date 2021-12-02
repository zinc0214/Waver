package com.zinc.data.models

import kotlinx.serialization.Serializable

@Serializable
data class DetailInfo(
    val profileImage: String,
    val badgeImage: String,
    val titlePosition: String,
    val nickName: String,
    val title: String,
    val memo: String
)