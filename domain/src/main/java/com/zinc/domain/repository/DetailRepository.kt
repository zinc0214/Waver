package com.zinc.domain.repository

import com.zinc.common.models.AddBucketCommentRequest
import com.zinc.common.models.BucketDetailResponse
import com.zinc.common.models.CommonResponse

interface DetailRepository {
    suspend fun loadBucketDetail(token: String, id: String, isMine: Boolean): BucketDetailResponse
    suspend fun addBucketComment(token: String, request: AddBucketCommentRequest): CommonResponse
    suspend fun requestGoalCountUpdate(token: String, id: String, goalCount: Int): CommonResponse
}
