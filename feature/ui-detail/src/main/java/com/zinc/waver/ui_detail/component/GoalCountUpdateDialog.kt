package com.zinc.waver.ui_detail.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.zinc.waver.ui.presentation.component.dialog.SingleTextFieldDialogEvent
import com.zinc.waver.ui.presentation.component.dialog.SingleTextFieldDialogView
import com.zinc.waver.ui.presentation.component.dialog.TextFieldAlignment
import com.zinc.waver.ui_detail.model.GoalCountUpdateEvent
import com.zinc.waver.ui_common.R as CommonR

@Composable
fun GoalCountUpdateDialog(
    currentCount: String,
    event: (GoalCountUpdateEvent) -> Unit
) {

    var updateGoalCount by remember { mutableStateOf(currentCount) }

    SingleTextFieldDialogView(
        titleText = stringResource(id = CommonR.string.optionGoalCount),
        prevText = updateGoalCount,
        filedHintText = "2",
        saveNotAvailableToastText = stringResource(id = CommonR.string.countIsNotValidToast),
        textFieldAlignment = TextFieldAlignment.CENTER,
        keyboardType = KeyboardType.Number,
        enableCondition = { currentCount != updateGoalCount && updateGoalCount != "0" && updateGoalCount != "1" && updateGoalCount.isNotEmpty() },
        saveAvailableCondition = { updateGoalCount != "0" && updateGoalCount != "1" && updateGoalCount.isNotEmpty() },
        event = {
            when (it) {
                SingleTextFieldDialogEvent.Close -> event.invoke(GoalCountUpdateEvent.Close)
                is SingleTextFieldDialogEvent.TextChangedField -> {
                    updateGoalCount = it.text
                }

                is SingleTextFieldDialogEvent.Update -> event.invoke(
                    GoalCountUpdateEvent.CountUpdate(
                        updateGoalCount.toInt()
                    )
                )
            }
        }
    )
}