package com.zinc.common.models

data class OtherProfileInfo(
    val id: String,
    val imgUrl: String?,
    val name: String,
    val isAlreadyFollowing: YesOrNo
)