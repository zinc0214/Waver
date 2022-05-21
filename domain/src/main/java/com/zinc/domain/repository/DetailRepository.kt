package com.zinc.domain.repository

import com.zinc.common.models.DetailInfo

interface DetailRepository {
    suspend fun loadBucketDetail(id: String): DetailInfo
}
