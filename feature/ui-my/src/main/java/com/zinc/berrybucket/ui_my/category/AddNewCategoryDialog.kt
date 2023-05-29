package com.zinc.berrybucket.ui_my.category

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.zinc.berrybucket.ui.design.theme.Gray5
import com.zinc.berrybucket.ui.dialog.SingleTextFieldDialogEvent
import com.zinc.berrybucket.ui.dialog.SingleTextFieldDialogView
import com.zinc.berrybucket.ui.dialog.TextFieldArrangment
import com.zinc.berrybucket.ui_my.model.AddNewCategoryEvent
import com.zinc.berrybucket.ui_common.R as CommonR

@Composable
fun AddNewCategoryDialog(
    event: (AddNewCategoryEvent) -> Unit
) {

    val updatedText = remember { mutableStateOf("") }

    SingleTextFieldDialogView(
        titleText = CommonR.string.addNewCategoryTitle,
        prevText = "",
        filedHintText = CommonR.string.addNewCategoryHint,
        rightButtonText = CommonR.string.addDesc,
        saveNotAvailableToastText = CommonR.string.addNewCategoryNotAvailable,
        textFieldArrangement = TextFieldArrangment.START,
        fieldTextSize = 14.dp,
        disableTextColor = Gray5,
        keyboardType = KeyboardType.Text,
        enableCondition = { updatedText.value.isNotEmpty() },
        saveAvailableCondition = { updatedText.value.isNotEmpty() },
        event = {
            when (it) {
                SingleTextFieldDialogEvent.Close -> {
                    event.invoke(AddNewCategoryEvent.Close)
                }

                is SingleTextFieldDialogEvent.TextChangedField -> {
                    updatedText.value = it.text
                }

                is SingleTextFieldDialogEvent.Update -> {
                    event.invoke(AddNewCategoryEvent.AddNewCategory(updatedText.value))

                }
            }
        }
    )
}