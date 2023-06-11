package com.zinc.data.model

import com.zinc.common.models.CategoryInfo
import kotlinx.serialization.Serializable

data class LoadCategoryResponse(
    val data: List<CategoryInfo>,
    val success: Boolean,
    val code: String,
    val message: String
)

@Serializable
data class AddNewCategoryRequest(
    val name: String
)