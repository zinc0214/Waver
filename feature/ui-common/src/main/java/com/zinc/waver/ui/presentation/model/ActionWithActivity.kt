package com.zinc.waver.ui.presentation.model

import com.zinc.waver.model.AddImageType
import com.zinc.waver.model.UserSelectedImageInfo

sealed class ActionWithActivity {
    data class AddImage(
        val type: AddImageType, val failed: () -> Unit, val succeed: (UserSelectedImageInfo) -> Unit
    ) : ActionWithActivity()

    data class CheckPermission(
        val isAllGranted: (Boolean) -> Unit
    ) : ActionWithActivity()

    data object AppFinish : ActionWithActivity()

    data object GoToQNAEmail : ActionWithActivity()

    data object Logout : ActionWithActivity()

    data object InAppBilling : ActionWithActivity()
}