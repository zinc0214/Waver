package com.zinc.data.repository

import android.util.Log
import com.zinc.common.models.AllBucketListRequest
import com.zinc.common.models.AllBucketListResponse
import com.zinc.common.models.CommonResponse
import com.zinc.common.models.FollowResponse
import com.zinc.common.models.HomeProfileResponse
import com.zinc.data.api.WaverApi
import com.zinc.domain.repository.MyRepository
import javax.inject.Inject

internal class MyRepositoryImpl @Inject constructor(
    private val waverApi: WaverApi
) : MyRepository {
    override suspend fun loadMyHomeProfileInfo(): HomeProfileResponse {
        return waverApi.loadMyProfileInfo()
    }

    override suspend fun loadAllBucketList(
        allBucketListRequest: AllBucketListRequest
    ): AllBucketListResponse {
        Log.e("ayhan", "loadAllBucketList: $allBucketListRequest")
        return waverApi.loadAllBucketList(
            dDayBucketOnly = allBucketListRequest.dDayBucketOnly,
            isPassed = allBucketListRequest.isPassed,
            status = allBucketListRequest.status,
            sort = allBucketListRequest.sort,
            categoryId = null
        )
    }

    override suspend fun loadFollowList(): FollowResponse {
        return waverApi.loadFollowList()
    }

    override suspend fun requestUnfollow(userId: String): CommonResponse {
        return waverApi.requestUnfollow(userId)
    }

    override suspend fun requestFollow(userId: String): CommonResponse {
        return waverApi.requestFollow(userId)
    }

    override suspend fun searchAllBucketList(query: String): AllBucketListResponse {
        return waverApi.searchAllBucketList(query)
    }

    override suspend fun searchDdayBucketList(query: String): AllBucketListResponse {
        return waverApi.searchDdayBucketList()
    }

    override suspend fun achieveMyBucket(id: String): CommonResponse {
        return waverApi.achieveBucket(id)
    }

    override suspend fun requestUserBlock(userId: String): CommonResponse {
        return waverApi.requestUserBlock(userId)
    }
}