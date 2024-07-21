package com.zinc.data.repository

import com.zinc.common.models.ProfileResponse
import com.zinc.data.api.WaverApi
import com.zinc.domain.models.OtherBucketListResponse
import com.zinc.domain.models.OtherFollowDataResponse
import com.zinc.domain.repository.OtherRepository
import javax.inject.Inject

class OtherRepositoryImpl @Inject constructor(
    private val waverApi: WaverApi
) : OtherRepository {
    override suspend fun loadOtherProfile(
        otherUserId: String
    ): ProfileResponse {
        return waverApi.loadOtherProfileInfo(otherUserId)
    }

    override suspend fun loadOtherBucketList(
        otherUserId: String
    ): OtherBucketListResponse {
        return waverApi.loadOtherBucketList(otherUserId)
    }

    override suspend fun loadOtherFollow(
        otherUserId: String
    ): OtherFollowDataResponse {
        return waverApi.loadOtherFollowData(otherUserId)
    }
}