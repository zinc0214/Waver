package com.zinc.common.models

import kotlinx.serialization.Serializable

@Serializable
data class LoadCategoryResponse(
    val data: List<CategoryInfo>,
    val success: Boolean,
    val code: String,
    val message: String
)

@Serializable
data class CategoryInfo(
    val id: Int,
    val name: String,
    val defaultYn: YesOrNo = YesOrNo.Y,
    val bucketCount: String
)

@Serializable
data class AddNewCategoryRequest(
    val name: String
)

@Serializable
data class EditCategoryNameRequest(
    val id: Int,
    val name: String
)

@Serializable
data class ReorderedCategoryRequest(
    val categoryIds: List<String>
)