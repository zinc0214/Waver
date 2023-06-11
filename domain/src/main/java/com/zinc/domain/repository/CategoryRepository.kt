package com.zinc.domain.repository

import com.zinc.common.models.CategoryInfo
import com.zinc.common.models.CommonResponse

interface CategoryRepository {
    suspend fun loadCategoryList(token: String): List<CategoryInfo>
    suspend fun addNewCategory(token: String, name: String): CommonResponse
}
