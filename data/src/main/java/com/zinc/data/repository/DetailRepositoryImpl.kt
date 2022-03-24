package com.zinc.data.repository

import com.zinc.data.api.BerryBucketApi
import com.zinc.data.models.DetailInfo
import javax.inject.Inject

internal class DetailRepositoryImpl @Inject constructor(
    private val berryBucketApi: BerryBucketApi
) : DetailRepository {

    override suspend fun loadBucketDetail(id: String): DetailInfo {
        return berryBucketApi.loadBucketDetail(id)
    }
}