package com.zinc.domain.repository

import com.zinc.data.models.DetailInfo

interface DetailRepository {
    suspend fun loadBucketDetail(id: String): DetailInfo
}
