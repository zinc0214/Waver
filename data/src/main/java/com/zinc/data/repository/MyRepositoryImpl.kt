package com.zinc.data.repository

import com.zinc.data.api.MyApi
import com.zinc.data.models.AllBucketList
import com.zinc.data.models.DdayBucketList
import com.zinc.data.models.MyProfileInfo
import com.zinc.data.models.MyState
import javax.inject.Inject

class MyRepositoryImpl @Inject constructor(
    private val myApi: MyApi
) : MyRepository {
    override suspend fun loadMyProfileInfo(): MyProfileInfo {
        return myApi.loadMyProfileInfo()
    }

    override suspend fun loadMyDdayBucketList(): DdayBucketList {
        return myApi.loadMyDdayBucketList()
    }

    override suspend fun loadAllBucketList(): AllBucketList {
        return myApi.loadAllBucketList()
    }

    override suspend fun loadMyState(): MyState {
        return myApi.loadMyState()
    }
}