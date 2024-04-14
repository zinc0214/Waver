package com.zinc.domain.repository

import com.zinc.common.models.ProfileResponse
import com.zinc.domain.models.OtherBucketListResponse
import com.zinc.domain.models.OtherFollowDataResponse

interface OtherRepository {
    suspend fun loadOtherProfile(token: String, otherUserId: String): ProfileResponse
    suspend fun loadOtherBucketList(token: String, otherUserId: String): OtherBucketListResponse
    suspend fun loadOtherFollow(token: String, otherUserId: String): OtherFollowDataResponse
}