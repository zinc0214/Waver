package com.zinc.berrybucket.ui.presentation.detail.model

sealed interface GoalCountUpdateEvent {
    data object Close : GoalCountUpdateEvent
    data class CountUpdate(val count: Int) : GoalCountUpdateEvent
}