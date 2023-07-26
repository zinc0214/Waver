package com.zinc.common.models

import kotlinx.serialization.Serializable

@Serializable
data class BucketDetailResponse(
    val data: DetailInfo,
    val success: Boolean,
    val code: String,
    val message: String
)


@Serializable
data class DetailInfo(
    val id: String,
    val title: String,
    val memo: String?,
    val status: Status,
    val pin: String,
    val scrapYn: String,
    val categoryName: String,
    val goalCount: Int,
    val userCount: Int,
    val completedDt: String?,
    val keywords: List<String>?,
    val images: List<String>?
) {
    enum class Status {
        PROGRESS, COMPLETE
    }
}