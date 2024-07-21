package com.zinc.waver.ui_other.model

interface OtherHomeEvent {
    data class GoToOtherBucket(val bucketId: Int, val writerId: String) : OtherHomeEvent
    data object GoToBack : OtherHomeEvent
}