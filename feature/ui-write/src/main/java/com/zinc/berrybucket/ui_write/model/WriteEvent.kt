package com.zinc.berrybucket.ui_write.model

import com.zinc.berrybucket.model.WriteTotalInfo
import com.zinc.berrybucket.ui.presentation.model.ActionWithActivity

sealed interface Write1Event {
    object GoToBack : Write1Event
    data class GoToWrite2(val info: WriteTotalInfo) : Write1Event
    object GoToAddCategory : Write1Event
    data class ActivityAction(val acton: ActionWithActivity) : Write1Event
}