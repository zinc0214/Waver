package com.zinc.domain.repository

import com.zinc.common.models.AllBucketListRequest
import com.zinc.common.models.AllBucketListResponse
import com.zinc.common.models.CategoryInfo
import com.zinc.common.models.DdayBucketList
import com.zinc.common.models.MyProfileResponse
import com.zinc.common.models.MyState

interface MyRepository {
    suspend fun loadMyProfileInfo(token: String): MyProfileResponse
    suspend fun loadMyDdayBucketList(): DdayBucketList
    suspend fun loadCategoryList(): List<CategoryInfo>
    suspend fun loadMyState(): MyState
    suspend fun loadAllBucketList(
        token: String,
        allBucketListRequest: AllBucketListRequest
    ): AllBucketListResponse
}