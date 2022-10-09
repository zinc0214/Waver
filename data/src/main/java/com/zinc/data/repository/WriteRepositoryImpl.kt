package com.zinc.data.repository

import com.zinc.common.models.AddBucketListRequest
import com.zinc.data.api.BerryBucketApi
import com.zinc.domain.repository.WriteRepository
import javax.inject.Inject

internal class WriteRepositoryImpl @Inject constructor(
    private val berryBucketApi: BerryBucketApi
) : WriteRepository {
    override suspend fun addNewBucketList(
        token: String,
        addBucketListRequest: AddBucketListRequest
    ) = berryBucketApi.addNewBucketList(token, addBucketListRequest)

}