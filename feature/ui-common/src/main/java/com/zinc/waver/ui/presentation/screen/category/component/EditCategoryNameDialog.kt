package com.zinc.waver.ui.presentation.screen.category.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.zinc.waver.model.UICategoryInfo
import com.zinc.waver.ui.design.theme.Gray5
import com.zinc.waver.ui.presentation.component.dialog.SingleTextFieldDialogEvent
import com.zinc.waver.ui.presentation.component.dialog.SingleTextFieldDialogView
import com.zinc.waver.ui.presentation.component.dialog.TextFieldAlignment
import com.zinc.waver.ui.presentation.screen.category.model.EditCategoryNameEvent
import com.zinc.waver.ui_common.R as CommonR

@Composable
fun EditCategoryNameDialog(
    originCategoryInfo: UICategoryInfo,
    event: (EditCategoryNameEvent) -> Unit
) {

    val updatedText = remember { mutableStateOf(originCategoryInfo.name) }

    SingleTextFieldDialogView(
        titleText = stringResource(id = CommonR.string.addNewCategoryTitle),
        prevText = originCategoryInfo.name,
        filedHintText = stringResource(id = CommonR.string.editCategoryNameTitle),
        rightButtonText = CommonR.string.confirm,
        saveNotAvailableToastText = stringResource(id = CommonR.string.editCategoryNameNotAvailable),
        textFieldAlignment = TextFieldAlignment.START,
        fieldTextSize = 14.dp,
        disableTextColor = Gray5,
        keyboardType = KeyboardType.Text,
        enableCondition = { updatedText.value.isNotEmpty() },
        saveAvailableCondition = { updatedText.value.isNotEmpty() },
        event = {
            when (it) {
                SingleTextFieldDialogEvent.Close -> {
                    event.invoke(EditCategoryNameEvent.Close)
                }

                is SingleTextFieldDialogEvent.TextChangedField -> {
                    updatedText.value = it.text
                }

                is SingleTextFieldDialogEvent.Update -> {
                    event.invoke(EditCategoryNameEvent.EditCategoryName(originCategoryInfo.copy(name = updatedText.value)))

                }
            }
        }
    )
}