package com.zinc.domain.repository

import com.zinc.common.models.AddBucketCommentRequest
import com.zinc.common.models.BucketDetailResponse
import com.zinc.common.models.CommonResponse
import com.zinc.common.models.ProfileResponse

interface DetailRepository {
    suspend fun loadBucketDetail(id: String, isMine: Boolean): BucketDetailResponse
    suspend fun addBucketComment(request: AddBucketCommentRequest): CommonResponse
    suspend fun requestGoalCountUpdate(id: String, goalCount: Int): CommonResponse
    suspend fun loadProfile(isMine: Boolean, writerId: String?): ProfileResponse
    suspend fun deleteBucketComment(id: String): CommonResponse
    suspend fun hideBucketComment(id: String): CommonResponse
    suspend fun deleteMyBucket(id: String): CommonResponse
}
