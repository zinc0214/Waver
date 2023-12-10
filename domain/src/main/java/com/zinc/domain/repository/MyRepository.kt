package com.zinc.domain.repository

import com.zinc.common.models.AllBucketListRequest
import com.zinc.common.models.AllBucketListResponse
import com.zinc.common.models.CommonResponse
import com.zinc.common.models.FollowResponse
import com.zinc.common.models.HomeProfileResponse

interface MyRepository {
    suspend fun loadMyHomeProfileInfo(token: String): HomeProfileResponse
    suspend fun loadAllBucketList(
        token: String,
        allBucketListRequest: AllBucketListRequest
    ): AllBucketListResponse

    suspend fun loadFollowList(
        token: String
    ): FollowResponse

    suspend fun requestUnfollow(
        token: String,
        userId: String
    ): CommonResponse

    suspend fun requestFollow(
        token: String,
        userId: String
    ): CommonResponse

    suspend fun searchAllBucketList(
        token: String,
        query: String
    ): AllBucketListResponse

    suspend fun searchDdayBucketList(
        token: String,
        query: String
    ): AllBucketListResponse

    suspend fun achieveMyBucket(
        token: String,
        id: String
    ): CommonResponse
}