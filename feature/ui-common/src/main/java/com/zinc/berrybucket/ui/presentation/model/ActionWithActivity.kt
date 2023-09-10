package com.zinc.berrybucket.ui.presentation.model

import com.zinc.berrybucket.model.AddImageType
import com.zinc.berrybucket.model.UserSelectedImageInfo

sealed class ActionWithActivity {
    data class AddImage(
        val type: AddImageType, val failed: () -> Unit, val succeed: (UserSelectedImageInfo) -> Unit
    ) : ActionWithActivity()

    data class CheckPermission(
        val isAllGranted: (Boolean) -> Unit
    ) : ActionWithActivity()

    object AppFinish : ActionWithActivity()
}