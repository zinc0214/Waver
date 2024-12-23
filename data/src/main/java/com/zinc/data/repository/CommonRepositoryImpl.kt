package com.zinc.data.repository

import com.zinc.common.models.CommonResponse
import com.zinc.data.api.WaverApi
import com.zinc.domain.repository.CommonRepository
import javax.inject.Inject

internal class CommonRepositoryImpl @Inject constructor(
    private val waverApi: WaverApi
) : CommonRepository {
    override suspend fun saveBucketLike(id: String): CommonResponse {
        return waverApi.saveBucketLike(id)
    }

    override suspend fun copyOtherBucket(id: String): CommonResponse {
        return waverApi.copyOtherBucket(id)
    }
}