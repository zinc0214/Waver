package com.zinc.berrybucket.ui.presentation.component.dialog

import androidx.compose.runtime.Composable

@Composable
fun ApiFailDialog(title: String, message: String, dismissEvent: () -> Unit) {

    TitleAndMessageDialogView(
        title = title, message = message, dismissAvailable = true,
        dismissEvent = {
            dismissEvent()
        }
    )
}