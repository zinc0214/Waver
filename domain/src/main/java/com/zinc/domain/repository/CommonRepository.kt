package com.zinc.domain.repository

import com.zinc.common.models.CommonResponse

interface CommonRepository {
    suspend fun saveBucketLike(token: String, id: String): CommonResponse
}