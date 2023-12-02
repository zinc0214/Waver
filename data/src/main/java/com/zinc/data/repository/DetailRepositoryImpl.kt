package com.zinc.data.repository

import com.zinc.common.models.BucketDetailResponse
import com.zinc.data.api.BerryBucketApi
import com.zinc.domain.repository.DetailRepository
import javax.inject.Inject

internal class DetailRepositoryImpl @Inject constructor(
    private val berryBucketApi: BerryBucketApi
) : DetailRepository {

    override suspend fun loadBucketDetail(
        token: String,
        id: String,
        isMine: Boolean
    ): BucketDetailResponse {
        return if (isMine) berryBucketApi.loadBucketDetail(
            token, id
        ) else berryBucketApi.loadOtherBucketDetail(token, id)
    }
}