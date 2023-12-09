package com.zinc.berrybucket.ui.presentation.component.dialog

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.zinc.berrybucket.model.DialogButtonInfo
import com.zinc.berrybucket.ui.design.theme.Gray1
import com.zinc.berrybucket.ui.design.theme.Gray10
import com.zinc.berrybucket.ui.design.theme.Gray4
import com.zinc.berrybucket.ui.design.theme.Gray6
import com.zinc.berrybucket.ui.design.theme.Gray7
import com.zinc.berrybucket.ui.design.theme.Main3
import com.zinc.berrybucket.ui.design.theme.Main4
import com.zinc.berrybucket.ui.presentation.component.BottomButtonView
import com.zinc.berrybucket.ui.presentation.component.MyText
import com.zinc.berrybucket.ui.presentation.component.MyTextField
import com.zinc.berrybucket.ui.util.dpToSp
import com.zinc.berrybucket.ui_common.R

sealed interface SingleTextFieldDialogEvent {
    object Close : SingleTextFieldDialogEvent
    data class Update(val text: String) : SingleTextFieldDialogEvent
    data class TextChangedField(val text: String) : SingleTextFieldDialogEvent
}

enum class TextFieldAlignment {
    START, CENTER, END
}

/**
 *  1개의 TextField 를 가지는 공통 Dialog
 *
 * @param titleText 상단 타이틀
 * @param prevText 기존 Field 값
 * @param filedHintText Field Hint 값
 * @param leftButtonText 좌측 버튼 텍스트
 * @param rightButtonText 우측 버튼 텍스트
 * @param saveNotAvailableToastText 저장 불가 상태일 때 노출할 안내문구 (토스트용)
 * @param enableCondition TextField 의 텍스트 및 border 색상이 파란색으로 켜지는 조건
 * @param saveAvailableCondition 저장 가능 조건
 * @param event Dialog 이벤트
 */
@Composable
fun SingleTextFieldDialogView(
    titleText: String,
    prevText: String,
    filedHintText: String,
    @StringRes leftButtonText: Int = R.string.cancel,
    @StringRes rightButtonText: Int = R.string.apply,
    saveNotAvailableToastText: String,
    fieldTextSize: Dp = 22.dp,
    textFieldAlignment: TextFieldAlignment,
    disableTextColor: Color = Gray7,
    keyboardType: KeyboardType,
    enableCondition: () -> Boolean,
    saveAvailableCondition: () -> Boolean,
    event: (SingleTextFieldDialogEvent) -> Unit
) {

    val isNumber = keyboardType == KeyboardType.Number

    DialogView(dismissEvent = {
        event.invoke(SingleTextFieldDialogEvent.Close)
    }, content = {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            shape = RoundedCornerShape(8.dp),
            backgroundColor = Gray1,
            elevation = 3.dp
        ) {
            val updatedText = remember { mutableStateOf(TextFieldValue(prevText)) }
            val context = LocalContext.current

            Column(modifier = Modifier.fillMaxWidth()) {

                // Title 영역
                MyText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp, vertical = 28.dp),
                    text = titleText,
                    fontSize = dpToSp(dp = 15.dp),
                    color = Gray10,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                MyTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 36.dp, start = 28.dp, end = 28.dp)
                        .heightIn(min = 56.dp)
                        .background(
                            color = Gray1,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .border(
                            width = 1.dp,
                            shape = RoundedCornerShape(4.dp),
                            color = if (enableCondition()) Main3 else Gray4
                        ),
                    value = updatedText.value,
                    textStyle = TextStyle(
                        color = if (enableCondition()) Gray10 else disableTextColor,
                        textAlign = when (textFieldAlignment) {
                            TextFieldAlignment.START -> TextAlign.Start
                            TextFieldAlignment.CENTER -> TextAlign.Center
                            TextFieldAlignment.END -> TextAlign.End
                        },
                        fontWeight = FontWeight.Bold,
                        fontSize = dpToSp(dp = fieldTextSize)
                    ),
                    onValueChange = {
                        if (isNumber && it.text.isDigitsOnly()) {
                            updatedText.value = it
                            event.invoke(SingleTextFieldDialogEvent.TextChangedField(it.text))
                        } else {
                            updatedText.value = it
                            event.invoke(SingleTextFieldDialogEvent.TextChangedField(it.text))
                        }
                    },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = keyboardType
                    ),
                    decorationBox = { innerTextField ->
                        Row(
                            horizontalArrangement = when (textFieldAlignment) {
                                TextFieldAlignment.START -> Arrangement.Start
                                TextFieldAlignment.CENTER -> Arrangement.Center
                                TextFieldAlignment.END -> Arrangement.End
                            },
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {

                            if (updatedText.value.text.isEmpty()) {
                                MyText(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = filedHintText,
                                    fontWeight = FontWeight.Bold,
                                    color = if (enableCondition()) Gray10 else disableTextColor,
                                    fontSize = dpToSp(fieldTextSize),
                                    textAlign = when (textFieldAlignment) {
                                        TextFieldAlignment.START -> TextAlign.Start
                                        TextFieldAlignment.CENTER -> TextAlign.Center
                                        TextFieldAlignment.END -> TextAlign.End
                                    }
                                )
                            }
                            innerTextField()  //<-- Add this
                        }
                    }
                )

                BottomButtonView(
                    negative = DialogButtonInfo(
                        text = leftButtonText,
                        color = Gray6,
                    ),
                    negativeEvent = {
                        event(SingleTextFieldDialogEvent.Close)
                    },
                    positive = DialogButtonInfo(
                        text = rightButtonText,
                        color = Main4
                    ),
                    positiveEvent = {
                        if (saveAvailableCondition()) {
                            event(
                                SingleTextFieldDialogEvent.Update(text = updatedText.value.text)
                            )
                        } else {
                            Toast.makeText(
                                context,
                                saveNotAvailableToastText,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                )
            }
        }
    })
}