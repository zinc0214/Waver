package com.zinc.common.models

import kotlinx.serialization.Serializable
import java.io.File

@Serializable
data class AddBucketListRequest(
    val bucketType: BucketType,
    val content: String, // 내용
    val memo: String?, // 메모
    val tags: List<String> = emptyList(), // 태그 목록 (최대 5)
    val images: List<File> = emptyList(), // 이미지 목록(최대 3)
    val targetDate: String?, // 목표완료일(yyyy-MM-dd)
    val goalCount: Int = 0, //  목표 횟수
    val categoryId: Int // 카테고리 ID
)

enum class BucketType {
    ORIGINAL,
    TOGETHER,
    CHALLENGE
}

@Serializable
data class AddBucketListResponse(
    val id: String,
    val content: String,
    val status: String,
    val dDay: Int
)
