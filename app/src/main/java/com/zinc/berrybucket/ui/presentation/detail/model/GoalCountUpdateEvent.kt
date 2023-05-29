package com.zinc.berrybucket.ui.presentation.detail.model

sealed interface GoalCountUpdateEvent {
    object Close : GoalCountUpdateEvent
    data class CountUpdate(val count: String) : GoalCountUpdateEvent
}