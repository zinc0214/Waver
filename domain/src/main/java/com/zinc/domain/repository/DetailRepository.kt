package com.zinc.domain.repository

import com.zinc.common.models.AddBucketCommentRequest
import com.zinc.common.models.BucketDetailResponse
import com.zinc.common.models.CommonResponse
import com.zinc.common.models.ProfileResponse

interface DetailRepository {
    suspend fun loadBucketDetail(token: String, id: String, isMine: Boolean): BucketDetailResponse
    suspend fun addBucketComment(token: String, request: AddBucketCommentRequest): CommonResponse
    suspend fun requestGoalCountUpdate(token: String, id: String, goalCount: Int): CommonResponse
    suspend fun loadProfile(token: String, isMine: Boolean, writerId: String?): ProfileResponse
    suspend fun deleteBucketComment(token: String, id: String): CommonResponse
}
