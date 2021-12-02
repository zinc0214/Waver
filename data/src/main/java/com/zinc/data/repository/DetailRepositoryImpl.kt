package com.zinc.data.repository

import com.zinc.data.api.DetailApi
import com.zinc.data.models.DetailInfo
import javax.inject.Inject

class DetailRepositoryImpl @Inject constructor(
    private val detailApi: DetailApi
) : DetailRepository {

    override suspend fun loadBucketDetail(id: String): DetailInfo {
        return detailApi.loadBucketDetail(id)
    }
}