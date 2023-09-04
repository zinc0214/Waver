package com.zinc.domain.repository

import com.zinc.common.models.CommonResponse
import com.zinc.common.models.LoadCategoryResponse

interface CategoryRepository {
    suspend fun loadCategoryList(token: String): LoadCategoryResponse
    suspend fun addNewCategory(token: String, name: String): CommonResponse
    suspend fun editCategoryName(token: String, id: Int, name: String): CommonResponse
    suspend fun removeCategory(token: String, id: Int): CommonResponse
    suspend fun reorderCategory(token: String, orderedIds: List<String>): CommonResponse
    suspend fun searchCategoryList(token: String, query: String): LoadCategoryResponse
}

