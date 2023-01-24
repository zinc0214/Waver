package com.zinc.berrybucket.ui.presentation.write.bottomScreens

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zinc.berrybucket.R
import com.zinc.berrybucket.model.BottomButtonClickEvent
import com.zinc.berrybucket.ui.design.theme.Gray1
import com.zinc.berrybucket.ui.design.theme.Gray10
import com.zinc.berrybucket.ui.design.theme.Gray4
import com.zinc.berrybucket.ui.design.theme.Gray7
import com.zinc.berrybucket.ui.design.theme.Main3
import com.zinc.berrybucket.ui.presentation.common.BottomButtonView
import com.zinc.berrybucket.ui.presentation.common.MyText
import com.zinc.berrybucket.ui.presentation.common.MyTextField
import com.zinc.berrybucket.ui.util.dpToSp

@Composable
fun GoalCountBottomScreen(
    originCount: String = "0",
    canceled: () -> Unit,
    confirmed: (String) -> Unit
) {
    var editedGoalCount by remember { mutableStateOf(TextFieldValue(originCount)) }
    Column(modifier = Modifier.fillMaxWidth()) {
        MyText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 28.dp),
            text = stringResource(id = R.string.optionGoalCount),
            fontSize = dpToSp(dp = 15.dp),
            color = Gray10,
            textAlign = TextAlign.Center
        )

        MyTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 36.dp, start = 68.dp, end = 68.dp)
                .heightIn(min = 56.dp)
                .background(
                    color = Gray1,
                    shape = RoundedCornerShape(4.dp)
                )
                .border(
                    width = 1.dp,
                    shape = RoundedCornerShape(4.dp),
                    color = if (originCount == editedGoalCount.text || editedGoalCount.text == "0") Gray4 else Main3
                ),
            value = editedGoalCount,
            textStyle = TextStyle(
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = dpToSp(dp = 22.dp)
            ),
            onValueChange = {
                editedGoalCount = it
            },
            maxLines = 1,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            ),
            decorationBox = { innerTextField ->
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    if (editedGoalCount.text.isEmpty()) {
                        MyText(
                            modifier = Modifier.fillMaxWidth(),
                            text = "0",
                            color = if (originCount == editedGoalCount.text || editedGoalCount.text == "0") Gray7 else Gray10,
                            fontSize = dpToSp(22.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                    innerTextField()  //<-- Add this
                }
            }
        )

        BottomButtonView(
            rightText = R.string.confirm,
            clickEvent = {
                when (it) {
                    BottomButtonClickEvent.LeftButtonClicked -> canceled()
                    BottomButtonClickEvent.RightButtonClicked -> confirmed(editedGoalCount.text)
                }
            })
    }
}

@Preview
@Composable
private fun GoalCountBottomScreenPreview() {
    GoalCountBottomScreen(
        originCount = "100040", canceled = {}, confirmed = {}
    )
}