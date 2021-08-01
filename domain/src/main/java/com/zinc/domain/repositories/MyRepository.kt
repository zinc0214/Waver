package com.zinc.domain.repositories

import com.zinc.domain.models.MyBucketList
import com.zinc.domain.models.MyProfileInfo
import com.zinc.domain.models.MyState
import kotlinx.coroutines.flow.Flow

interface MyRepository {
    suspend fun loadMyProfileInfo(): Flow<MyProfileInfo>
    suspend fun loadMyState(): Flow<MyState>
    suspend fun loadMyDefaultBucketList(): Flow<List<MyBucketList>>
    suspend fun loadMyDdayBucketList(): Flow<List<MyBucketList>>
}
