package com.zinc.waver.ui_write.model

import com.zinc.waver.ui.presentation.model.ActionWithActivity

sealed interface WriteEvent {
    data object GoToBack : WriteEvent
    data object GoToAddCategory : WriteEvent
    data class ActivityAction(val acton: ActionWithActivity) : WriteEvent
}