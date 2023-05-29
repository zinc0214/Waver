package com.zinc.berrybucket.ui_my.model

sealed interface AddNewCategoryEvent {
    object Close : AddNewCategoryEvent
    data class AddNewCategory(val name: String) : AddNewCategoryEvent
}