package com.zinc.berrybucket.ui_write.presentation.options

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.hana.berrybucket.ui_write.R
import com.zinc.berrybucket.ui.design.theme.Gray10
import com.zinc.berrybucket.ui.design.theme.Gray6
import com.zinc.berrybucket.ui.presentation.component.MyText
import com.zinc.berrybucket.ui.presentation.component.MyTextField
import com.zinc.berrybucket.ui.util.dpToSp
import com.zinc.berrybucket.ui_write.presentation.WriteAppBar
import com.zinc.berrybucket.ui_write.presentation.WriteAppBarClickEvent

@Composable
fun MemoScreen(
    modifier: Modifier = Modifier,
    originMemo: String,
    memoChanged: (String) -> Unit,
    closeClicked: () -> Unit
) {

    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (appBar, textField) = createRefs()

        val nextButtonClickable = remember { mutableStateOf(false) }
        val currentMemo = remember { mutableStateOf(originMemo) }
        val hintText = stringResource(id = R.string.writeMemoHintText)

        WriteAppBar(modifier = Modifier
            .fillMaxWidth()
            .constrainAs(appBar) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top)
            },
            nextButtonClickable = nextButtonClickable.value,
            rightText = R.string.writeGoToFinish,
            clickEvent = {
                when (it) {
                    WriteAppBarClickEvent.CloseClicked -> {
                        closeClicked()
                    }
                    WriteAppBarClickEvent.NextClicked -> {
                        memoChanged(currentMemo.value)
                    }
                }
            })

        MyTextField(
            modifier = modifier
                .constrainAs(textField) {
                    top.linkTo(appBar.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
                .padding(start = 28.dp, end = 28.dp, top = 24.dp, bottom = 64.dp),
            value = currentMemo.value,
            textStyle = TextStyle(
                color = Gray10,
                fontSize = dpToSp(24.dp),
                fontWeight = FontWeight.Medium,
            ),
            onValueChange = { currentMemo.value = it },
            decorationBox = { innerTextField ->
                Row {
                    if (currentMemo.value.isEmpty()) {
                        MyText(text = hintText, color = Gray6, fontSize = dpToSp(24.dp))
                        nextButtonClickable.value = false
                    } else {
                        nextButtonClickable.value = true
                    }
                    innerTextField()
                }
            },
        )
    }
}

