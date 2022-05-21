package com.zinc.data.repository

import com.zinc.data.api.BerryBucketApi
import com.zinc.common.models.AllBucketList
import com.zinc.common.models.DdayBucketList
import com.zinc.common.models.MyProfileInfo
import com.zinc.common.models.MyState
import com.zinc.domain.repository.MyRepository
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