package com.zinc.data.repository

import android.util.Log
import com.zinc.common.models.AllBucketListRequest
import com.zinc.common.models.AllBucketListResponse
import com.zinc.common.models.CommonResponse
import com.zinc.common.models.FollowResponse
import com.zinc.common.models.HomeProfileResponse
import com.zinc.data.api.BerryBucketApi
import com.zinc.domain.repository.MyRepository
import javax.inject.Inject

internal class MyRepositoryImpl @Inject constructor(
    private val berryBucketApi: BerryBucketApi
) : MyRepository {
    override suspend fun loadMyHomeProfileInfo(): HomeProfileResponse {
        return berryBucketApi.loadMyProfileInfo()
    }

    override suspend fun loadAllBucketList(
        allBucketListRequest: AllBucketListRequest
    ): AllBucketListResponse {
        Log.e("ayhan", "loadAllBucketList: $allBucketListRequest")
        return berryBucketApi.loadAllBucketList(
            dDayBucketOnly = allBucketListRequest.dDayBucketOnly,
            isPassed = allBucketListRequest.isPassed,
            status = allBucketListRequest.status,
            sort = allBucketListRequest.sort,
            categoryId = null
        )
    }

    override suspend fun loadFollowList(): FollowResponse {
        return berryBucketApi.loadFollowList()
    }

    override suspend fun requestUnfollow(userId: String): CommonResponse {
        return berryBucketApi.requestUnfollow(userId)
    }

    override suspend fun requestFollow(userId: String): CommonResponse {
        return berryBucketApi.requestFollow(userId)
    }

    override suspend fun searchAllBucketList(query: String): AllBucketListResponse {
        return berryBucketApi.searchAllBucketList(query)
    }

    override suspend fun searchDdayBucketList(query: String): AllBucketListResponse {
        return berryBucketApi.searchDdayBucketList()
    }

    override suspend fun achieveMyBucket(id: String): CommonResponse {
        return berryBucketApi.achieveBucket(id)
    }
}