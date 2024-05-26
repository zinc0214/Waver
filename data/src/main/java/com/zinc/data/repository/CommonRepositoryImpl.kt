package com.zinc.data.repository

import com.zinc.common.models.CommonResponse
import com.zinc.data.api.BerryBucketApi
import com.zinc.domain.repository.CommonRepository
import javax.inject.Inject

internal class CommonRepositoryImpl @Inject constructor(
    private val berryBucketApi: BerryBucketApi
) : CommonRepository {
    override suspend fun saveBucketLike(id: String): CommonResponse {
        return berryBucketApi.saveBucketLike(id)
    }
}