package com.zinc.berrybucket.ui.presentation.screen.category.model

import com.zinc.berrybucket.model.UICategoryInfo

sealed interface AddCategoryEvent {
    data object Close : AddCategoryEvent
    data class AddNewAddCategory(val name: String) : AddCategoryEvent
}

sealed interface EditCategoryNameEvent {
    data object Close : EditCategoryNameEvent
    data class EditCategoryName(val categoryInfo: UICategoryInfo) : EditCategoryNameEvent
}

sealed interface CategoryEditOptionEvent {
    data class EditCategoryName(val categoryInfo: UICategoryInfo) : CategoryEditOptionEvent
    data class DeleteCategory(val categoryInfo: UICategoryInfo) : CategoryEditOptionEvent
    data class ReorderedCategory(val categoryList: List<UICategoryInfo>) : CategoryEditOptionEvent
}