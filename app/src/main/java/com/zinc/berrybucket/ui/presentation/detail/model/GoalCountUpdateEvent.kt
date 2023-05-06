package com.zinc.berrybucket.ui.presentation.detail.model

interface GoalCountUpdateEvent {
    object Close : GoalCountUpdateEvent
    data class CountUpdate(val count: String) : GoalCountUpdateEvent
}