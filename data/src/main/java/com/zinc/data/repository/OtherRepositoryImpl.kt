package com.zinc.data.repository

import com.zinc.common.models.ProfileResponse
import com.zinc.data.api.BerryBucketApi
import com.zinc.domain.models.OtherBucketListResponse
import com.zinc.domain.models.OtherFollowDataResponse
import com.zinc.domain.repository.OtherRepository
import javax.inject.Inject

class OtherRepositoryImpl @Inject constructor(
    private val berryBucketApi: BerryBucketApi
) : OtherRepository {
    override suspend fun loadOtherProfile(
        token: String,
        otherUserId: String
    ): ProfileResponse {
        return berryBucketApi.loadOtherProfileInfo(token, otherUserId)
    }

    override suspend fun loadOtherBucketList(
        token: String,
        otherUserId: String
    ): OtherBucketListResponse {
        return berryBucketApi.loadOtherBucketList(token, otherUserId)
    }

    override suspend fun loadOtherFollow(
        token: String,
        otherUserId: String
    ): OtherFollowDataResponse {
        return berryBucketApi.loadOtherFollowData(token, otherUserId)
    }
}