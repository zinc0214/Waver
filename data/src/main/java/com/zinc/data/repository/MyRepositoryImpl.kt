package com.zinc.data.repository

import com.zinc.data.api.BerryBucketApi
import com.zinc.data.models.AllBucketList
import com.zinc.data.models.DdayBucketList
import com.zinc.data.models.MyProfileInfo
import com.zinc.data.models.MyState
import javax.inject.Inject

internal class MyRepositoryImpl @Inject constructor(
    private val berryBucketApi: BerryBucketApi
) : MyRepository {
    override suspend fun loadMyProfileInfo(): MyProfileInfo {
        return berryBucketApi.loadMyProfileInfo()
    }

    override suspend fun loadMyDdayBucketList(): DdayBucketList {
        return berryBucketApi.loadMyDdayBucketList()
    }

    override suspend fun loadAllBucketList(): AllBucketList {
        return berryBucketApi.loadAllBucketList()
    }

    override suspend fun loadMyState(): MyState {
        return berryBucketApi.loadMyState()
    }
}