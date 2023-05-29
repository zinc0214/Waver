package com.zinc.berrybucket.ui.presentation.component.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DialogView(
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true,
    securePolicy: SecureFlagPolicy = SecureFlagPolicy.Inherit,
    usePlatformDefaultWidth: Boolean = false,
    dismissEvent: () -> Unit,
    content: @Composable () -> Unit
) {

    val dialogProperties = DialogProperties(
        dismissOnBackPress = dismissOnBackPress,
        dismissOnClickOutside = dismissOnClickOutside,
        securePolicy = securePolicy,
        usePlatformDefaultWidth = usePlatformDefaultWidth
    )
    Dialog(
        properties = dialogProperties,
        onDismissRequest = { dismissEvent() }) {
        content()
    }
}