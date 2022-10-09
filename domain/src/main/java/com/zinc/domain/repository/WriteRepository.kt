package com.zinc.domain.repository

import com.zinc.common.models.AddBucketListRequest
import com.zinc.common.models.AddBucketListResponse

interface WriteRepository {
    suspend fun addNewBucketList(
        token: String,
        addBucketListRequest: AddBucketListRequest
    ): AddBucketListResponse
}