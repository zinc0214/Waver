package com.zinc.domain.repository

import com.zinc.data.models.AllBucketList
import com.zinc.data.models.DdayBucketList
import com.zinc.data.models.MyProfileInfo
import com.zinc.data.models.MyState

interface MyRepository {
    suspend fun loadMyProfileInfo(): MyProfileInfo
    suspend fun loadMyDdayBucketList(): DdayBucketList
    suspend fun loadAllBucketList(): AllBucketList
    suspend fun loadMyState(): MyState
}