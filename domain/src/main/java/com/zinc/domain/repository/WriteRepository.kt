package com.zinc.domain.repository

import com.zinc.common.models.AddBucketListRequest
import com.zinc.common.models.CommonResponse

interface WriteRepository {
    suspend fun addNewBucketList(
        token: String,
        addBucketListRequest: AddBucketListRequest,
        isForUpdate: Boolean
    ): CommonResponse
}