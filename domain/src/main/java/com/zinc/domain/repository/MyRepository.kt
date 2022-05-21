package com.zinc.domain.repository

import com.zinc.common.models.AllBucketList
import com.zinc.common.models.DdayBucketList
import com.zinc.common.models.MyProfileInfo
import com.zinc.common.models.MyState

interface MyRepository {
    suspend fun loadMyProfileInfo(): MyProfileInfo
    suspend fun loadMyDdayBucketList(): DdayBucketList
    suspend fun loadAllBucketList(): AllBucketList
    suspend fun loadMyState(): MyState
}