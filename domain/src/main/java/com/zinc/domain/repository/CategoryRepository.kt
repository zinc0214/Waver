package com.zinc.domain.repository

import com.zinc.common.models.Category

interface CategoryRepository {
    suspend fun loadCategoryList(): List<Category>
}
