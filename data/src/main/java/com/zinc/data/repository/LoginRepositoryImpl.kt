package com.zinc.data.repository

import com.zinc.common.models.JoinRequest
import com.zinc.common.models.JoinResponse
import com.zinc.data.api.BerryBucketApi
import com.zinc.domain.repository.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val berryBucketApi: BerryBucketApi
) : LoginRepository {
    override suspend fun joinBerryBucket(joinRequest: JoinRequest): JoinResponse {
        return berryBucketApi.joinBerryBucket(joinRequest)
    }

}