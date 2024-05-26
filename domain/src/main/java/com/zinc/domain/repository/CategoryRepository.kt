package com.zinc.domain.repository

import com.zinc.common.models.AllBucketListResponse
import com.zinc.common.models.AllBucketListSortType
import com.zinc.common.models.CommonResponse
import com.zinc.common.models.LoadCategoryResponse

interface CategoryRepository {
    suspend fun loadCategoryList(): LoadCategoryResponse
    suspend fun addNewCategory(name: String): CommonResponse
    suspend fun editCategoryName(id: Int, name: String): CommonResponse
    suspend fun removeCategory(id: Int): CommonResponse
    suspend fun reorderCategory(orderedIds: List<String>): CommonResponse
    suspend fun searchCategoryList(query: String): LoadCategoryResponse
    suspend fun loadCategoryBucketList(
        categoryId: String,
        sort: AllBucketListSortType
    ): AllBucketListResponse
}

