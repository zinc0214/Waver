package com.zinc.common.models

import kotlinx.serialization.Serializable

@Serializable
data class KeywordInfo(
    val id: String,
    val name: String
)

data class KeywordResponse(
    val data: List<KeywordInfo>?,
    val success: Boolean,
    val code: String,
    val message: String
)