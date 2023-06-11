package com.zinc.berrybucket.ui_my.model

import com.zinc.common.models.CategoryInfo

sealed interface AddCategoryEvent {
    object Close : AddCategoryEvent
    data class AddNewAddCategory(val name: String) : AddCategoryEvent
}

sealed interface EditCategoryNameEvent {
    object Close : EditCategoryNameEvent
    data class EditCategoryName(val categoryInfo: CategoryInfo) : EditCategoryNameEvent
}

sealed interface CategoryEditOptionEvent {
    data class EditCategoryName(val categoryInfo: CategoryInfo) : CategoryEditOptionEvent
    data class DeleteCategory(val categoryInfo: CategoryInfo) : CategoryEditOptionEvent
    data class ReorderedCategory(val categoryList: List<CategoryInfo>) : CategoryEditOptionEvent
}