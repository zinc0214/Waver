package com.zinc.domain.repository

import com.zinc.common.models.AllBucketListRequest
import com.zinc.common.models.AllBucketListResponse
import com.zinc.common.models.CommonResponse
import com.zinc.common.models.DdayBucketList
import com.zinc.common.models.FollowResponse
import com.zinc.common.models.HomeProfileResponse
import com.zinc.common.models.MyState

interface MyRepository {
    suspend fun loadMyHomeProfileInfo(token: String): HomeProfileResponse
    suspend fun loadMyDdayBucketList(): DdayBucketList
    suspend fun loadMyState(): MyState
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
}