package com.zinc.data.repository

import android.util.Log
import com.zinc.common.models.AllBucketListRequest
import com.zinc.common.models.AllBucketListResponse
import com.zinc.common.models.CommonResponse
import com.zinc.common.models.FollowResponse
import com.zinc.common.models.HomeProfileResponse
import com.zinc.common.models.MyState
import com.zinc.data.api.BerryBucketApi
import com.zinc.domain.repository.MyRepository
import javax.inject.Inject

internal class MyRepositoryImpl @Inject constructor(
    private val berryBucketApi: BerryBucketApi
) : MyRepository {
    override suspend fun loadMyHomeProfileInfo(token: String): HomeProfileResponse {
        return berryBucketApi.loadMyProfileInfo(token)
    }

    override suspend fun loadAllBucketList(
        token: String,
        allBucketListRequest: AllBucketListRequest
    ): AllBucketListResponse {
        Log.e("ayhan", "loadAllBucketList: $allBucketListRequest")
        return berryBucketApi.loadAllBucketList(
            token = token,
            dDayBucketOnly = allBucketListRequest.dDayBucketOnly,
            isPassed = allBucketListRequest.isPassed,
            status = allBucketListRequest.status,
            sort = allBucketListRequest.sort,
            categoryId = null
        )
    }

    override suspend fun loadFollowList(token: String): FollowResponse {
        return berryBucketApi.loadFollowList(token)
    }

    override suspend fun requestUnfollow(token: String, userId: String): CommonResponse {
        return berryBucketApi.requestUnfollow(token, userId)
    }

    override suspend fun requestFollow(token: String, userId: String): CommonResponse {
        return berryBucketApi.requestFollow(token, userId)
    }

    override suspend fun searchAllBucketList(token: String, query: String): AllBucketListResponse {
        return berryBucketApi.searchAllBucketList(token, query)
    }

    override suspend fun searchDdayBucketList(token: String, query: String): AllBucketListResponse {
        return berryBucketApi.searchDdayBucketList(token)
    }

    override suspend fun achieveMyBucket(token: String, id: String): CommonResponse {
        return berryBucketApi.achieveBucket(token, id)
    }

    override suspend fun loadMyState(): MyState {
        return berryBucketApi.loadMyState()
    }
}