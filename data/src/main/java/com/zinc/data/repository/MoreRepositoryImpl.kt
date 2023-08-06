package com.zinc.data.repository

import com.zinc.common.models.MyProfileResponse
import com.zinc.data.api.BerryBucketApi
import com.zinc.domain.repository.MoreRepository
import javax.inject.Inject

internal class MoreRepositoryImpl @Inject constructor(
    private val berryBucketApi: BerryBucketApi
) : MoreRepository {
    override suspend fun loadProfileInfo(token: String): MyProfileResponse {
        return berryBucketApi.loadMyProfile(token)
    }
}