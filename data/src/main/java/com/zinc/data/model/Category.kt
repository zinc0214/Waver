package com.zinc.data.model

import com.zinc.common.models.CategoryInfo

data class LoadCategoryResponse(
    val data: List<CategoryInfo>,
    val success: Boolean,
    val code: String,
    val message: String
)