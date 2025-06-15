package com.zinc.domain.repository

import com.zinc.common.models.ProfileResponse
import com.zinc.domain.models.OtherBucketListResponse
import com.zinc.domain.models.OtherFollowDataResponse
import com.zinc.domain.models.OtherHomeResponse

interface OtherRepository {
    suspend fun loadOtherProfile(otherUserId: String): ProfileResponse
    suspend fun loadOtherBucketList(otherUserId: String): OtherBucketListResponse
    suspend fun loadOtherFollow(otherUserId: String): OtherFollowDataResponse
    suspend fun loadOtherHome(otherUserId: String): OtherHomeResponse
}