package com.zinc.data.repository

import com.zinc.data.api.BerryBucketApi
import com.zinc.common.models.DetailInfo
import com.zinc.domain.repository.DetailRepository
import javax.inject.Inject

internal class DetailRepositoryImpl @Inject constructor(
    private val berryBucketApi: BerryBucketApi
) : DetailRepository {

    override suspend fun loadBucketDetail(id: String): DetailInfo {
        return berryBucketApi.loadBucketDetail(id)
    }
}