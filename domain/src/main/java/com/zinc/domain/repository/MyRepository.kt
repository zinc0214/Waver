package com.zinc.domain.repository

import com.zinc.common.models.AllBucketListRequest
import com.zinc.common.models.AllBucketListResponse
import com.zinc.common.models.CommonResponse
import com.zinc.common.models.FollowResponse
import com.zinc.common.models.HomeProfileResponse

interface MyRepository {
    suspend fun loadMyHomeProfileInfo(): HomeProfileResponse
    suspend fun loadAllBucketList(
        allBucketListRequest: AllBucketListRequest
    ): AllBucketListResponse

    suspend fun loadFollowList(): FollowResponse

    suspend fun requestUnfollow(userId: String): CommonResponse

    suspend fun requestFollow(userId: String): CommonResponse

    suspend fun searchAllBucketList(query: String): AllBucketListResponse

    suspend fun searchDdayBucketList(query: String): AllBucketListResponse

    suspend fun achieveMyBucket(id: String): CommonResponse
}