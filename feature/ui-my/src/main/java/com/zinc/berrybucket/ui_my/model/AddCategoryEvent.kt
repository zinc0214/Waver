package com.zinc.berrybucket.ui_my.model

import com.zinc.berrybucket.model.UICategoryInfo

sealed interface AddCategoryEvent {
    object Close : AddCategoryEvent
    data class AddNewAddCategory(val name: String) : AddCategoryEvent
}

sealed interface EditCategoryNameEvent {
    object Close : EditCategoryNameEvent
    data class EditCategoryName(val categoryInfo: UICategoryInfo) : EditCategoryNameEvent
}

sealed interface CategoryEditOptionEvent {
    data class EditCategoryName(val categoryInfo: UICategoryInfo) : CategoryEditOptionEvent
    data class DeleteCategory(val categoryInfo: UICategoryInfo) : CategoryEditOptionEvent
    data class ReorderedCategory(val categoryList: List<UICategoryInfo>) : CategoryEditOptionEvent
}