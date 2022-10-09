package com.zinc.domain.repository

import com.zinc.common.models.CategoryInfo

interface CategoryRepository {
    suspend fun loadCategoryList(token: String): List<CategoryInfo>
}
