package com.zinc.berrybucket.ui_other.model

interface OtherHomeEvent {
    data class GoToOtherBucket(val id: Int) : OtherHomeEvent
    data object GoToBack : OtherHomeEvent
}