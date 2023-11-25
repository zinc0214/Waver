package com.zinc.berrybucket.ui.presentation.screen.category.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.zinc.berrybucket.ui.design.theme.Gray5
import com.zinc.berrybucket.ui.presentation.component.dialog.SingleTextFieldDialogEvent
import com.zinc.berrybucket.ui.presentation.component.dialog.SingleTextFieldDialogView
import com.zinc.berrybucket.ui.presentation.component.dialog.TextFieldAlignment
import com.zinc.berrybucket.ui.presentation.screen.category.model.AddCategoryEvent
import com.zinc.berrybucket.ui_common.R as CommonR

@Composable
fun AddNewCategoryDialog(
    event: (AddCategoryEvent) -> Unit
) {

    val updatedText = remember { mutableStateOf("") }

    SingleTextFieldDialogView(
        titleText = stringResource(id = CommonR.string.addNewCategoryTitle),
        prevText = "",
        filedHintText = stringResource(id = CommonR.string.addNewCategoryHint),
        rightButtonText = CommonR.string.addDesc,
        saveNotAvailableToastText = stringResource(id = CommonR.string.addNewCategoryNotAvailable),
        textFieldAlignment = TextFieldAlignment.START,
        fieldTextSize = 14.dp,
        disableTextColor = Gray5,
        keyboardType = KeyboardType.Text,
        enableCondition = { updatedText.value.isNotEmpty() },
        saveAvailableCondition = { updatedText.value.isNotEmpty() },
        event = {
            when (it) {
                SingleTextFieldDialogEvent.Close -> {
                    event.invoke(AddCategoryEvent.Close)
                }

                is SingleTextFieldDialogEvent.TextChangedField -> {
                    updatedText.value = it.text
                }

                is SingleTextFieldDialogEvent.Update -> {
                    event.invoke(AddCategoryEvent.AddNewAddCategory(updatedText.value))

                }
            }
        }
    )
}