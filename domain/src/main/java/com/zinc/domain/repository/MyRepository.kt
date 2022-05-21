package com.zinc.domain.repository

import com.zinc.common.models.*

interface MyRepository {
    suspend fun loadMyProfileInfo(): MyProfileInfo
    suspend fun loadMyDdayBucketList(): DdayBucketList
    suspend fun loadAllBucketList(): AllBucketList
    suspend fun loadCategoryList(): List<Category>
    suspend fun loadMyState(): MyState
}