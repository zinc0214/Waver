package com.zinc.common.models

import kotlinx.serialization.Serializable

@Serializable
data class CategoryInfo(
    val id: Int,
    val name: String,
    val defaultYn: YesOrNo = YesOrNo.Y,
    val bucketlistCount: String
)