package com.zinc.waver.ui.presentation.component.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.zinc.waver.model.DialogButtonInfo
import com.zinc.waver.ui.design.theme.Main4
import com.zinc.waver.ui_common.R

@Composable
fun ApiFailDialog(
    title: String? = stringResource(R.string.apiFailTitle),
    message: String? = stringResource(R.string.apiFailMessage),
    dismissEvent: () -> Unit
) {

    CommonDialogView(
        title = title, message = message, dismissAvailable = false,
        rightButtonInfo = DialogButtonInfo(
            text = R.string.confirm,
            color = Main4
        ),
        rightButtonEvent = {
            dismissEvent()
        }
    )
}