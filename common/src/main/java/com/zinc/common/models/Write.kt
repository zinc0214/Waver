package com.zinc.common.models

import kotlinx.serialization.Serializable
import java.io.File

@Serializable
data class AddBucketListRequest(
    val bucketType: BucketType,
    val exposureStatus: ExposureStatus, // 공개여부
    val title: String,
    val content: String, // 내용
    val memo: String?, // 메모
    val tags: String?, // 태그 목록(최대 5) - ","로 구분
    val friendUserIds: List<String>?, // 함께할 친구 ID - 함께하기인 경우 최소 1명 필수(최대 5)
    val scrapYn: YesOrNo, // 스크랩 여부
    val images: List<File>? = emptyList(), // 이미지 목록(최대 3)
    val targetDate: String?, // 목표완료일(yyyy-MM-dd)
    val goalCount: Int = 0, //  목표 횟수
    val categoryId: Int = 0 // 카테고리 ID
)

enum class BucketType {
    ORIGINAL,
    TOGETHER,
    CHALLENGE
}

enum class ExposureStatus {
    PUBLIC,
    FOLLOWER,
    PRIVATE
}

@Serializable
data class AddBucketListResponse(
    val id: String,
    val content: String,
    val status: String,
    val dDay: Int
)
