package com.zinc.domain.repository

import com.zinc.common.models.BucketDetailResponse

interface DetailRepository {
    suspend fun loadBucketDetail(token: String, id: String, isMine: Boolean): BucketDetailResponse
}
