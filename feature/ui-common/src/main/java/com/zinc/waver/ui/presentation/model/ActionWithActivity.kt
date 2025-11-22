package com.zinc.waver.ui.presentation.model

import com.zinc.waver.model.AddImageType
import com.zinc.waver.model.LoadedImageInfo

sealed class ActionWithActivity {
    data class AddImage(
        val type: AddImageType, val failed: () -> Unit, val succeed: (LoadedImageInfo) -> Unit
    ) : ActionWithActivity()

    data class CheckPermission(
        val isAllGranted: (Boolean) -> Unit
    ) : ActionWithActivity()

    data object AppFinish : ActionWithActivity()

    data object GoToQNAEmail : ActionWithActivity()

    data object Logout : ActionWithActivity()

    data class InAppBilling(val type: WaverPlusType) : ActionWithActivity()
}