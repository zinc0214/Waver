package com.zinc.berrybucket.ui.presentation.write

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.zinc.berrybucket.R
import com.zinc.berrybucket.ui.compose.theme.Gray10
import com.zinc.berrybucket.ui.compose.theme.Gray3
import com.zinc.berrybucket.ui.compose.theme.Gray6
import com.zinc.berrybucket.ui.compose.theme.Main4
import com.zinc.berrybucket.ui.presentation.common.IconButton
import com.zinc.berrybucket.ui.presentation.common.IconToggleButton
import com.zinc.berrybucket.ui.presentation.write.BottomOptionType.*

@Composable
fun WriteAppBar(
    modifier: Modifier,
    rightText: Int,
    clickEvent: (WriteAppBarClickEvent) -> Unit,
    nextButtonClickable: Boolean
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        val (closeButton, moreButton, divider) = createRefs()

        IconButton(
            image = R.drawable.btn40close,
            contentDescription = stringResource(id = R.string.closeDesc),
            modifier = Modifier
                .padding(start = 14.dp, top = 6.dp, bottom = 6.dp)
                .size(40.dp)
                .constrainAs(closeButton) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
            onClick = {
                clickEvent(WriteAppBarClickEvent.CloseClicked)
            }
        )

        Text(
            modifier = Modifier
                .constrainAs(moreButton) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .padding(start = 10.dp, end = 18.dp, top = 10.dp, bottom = 10.dp)
                .clickable(
                    enabled = nextButtonClickable,
                    onClick = { clickEvent(WriteAppBarClickEvent.NextClicked) }
                )
                .padding(start = 10.dp, end = 10.dp, top = 6.dp, bottom = 6.dp),
            text = stringResource(id = rightText),
            color = if (nextButtonClickable) Main4 else Gray6
        )

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(divider) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }, color = Gray3
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WriteTitleView(
    modifier: Modifier,
    title: String,
    onImeAction: (String) -> Unit
) {
    val hintText = stringResource(id = R.string.writeTitleHintText)
    val keyboardController = LocalSoftwareKeyboardController.current
    var titleText by remember { mutableStateOf(TextFieldValue("")) }

    BasicTextField(
        modifier = modifier,
        value = titleText,
        textStyle = TextStyle(
            color = Gray10,
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium
        ),
        onValueChange = { titleText = it },
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            onImeAction(titleText.text)
            keyboardController?.hide()
        }),
        decorationBox = { innerTextField ->
            Row {
                if (titleText.text.isEmpty()) {
                    Text(text = hintText, color = Gray6, fontSize = 24.sp)
                }
                innerTextField()  //<-- Add this
            }
        },
    )
}

@Composable
fun BottomOptionView(
    modifier: Modifier = Modifier,
    currentClickedOptions: List<BottomOptionType>,
    optionClicked: (Pair<BottomOptionType, Boolean>) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {

        Divider(
            modifier = Modifier.fillMaxWidth(),
            color = Gray3
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            BottomOptionType.values().forEach {
                BottomOptionIcon(
                    currentClickedOptions = currentClickedOptions,
                    type = it,
                    optionClicked = optionClicked
                )
            }
        }
    }

}


@Composable
private fun BottomOptionIcon(
    currentClickedOptions: List<BottomOptionType>,
    type: BottomOptionType,
    optionClicked: (Pair<BottomOptionType, Boolean>) -> Unit
) {
    val isClicked = currentClickedOptions.find { it == type } != null
    IconToggleButton(
        modifier = Modifier.size(40.dp),
        checked = isClicked,
        onCheckedChange = {
            optionClicked(type to it)
        },
        image = when (type) {
            MEMO -> {
                if (isClicked) R.drawable.btn_40_memo_on else R.drawable.btn_40_memo_off
            }
            IMAGE -> {
                if (isClicked) R.drawable.btn_40_gallery_on else R.drawable.btn_40_gallery_off
            }
            CATEGORY -> {
                if (isClicked) R.drawable.btn_40_calendar_on else R.drawable.btn_40_calendar_off
            }
            D_DAY -> {
                if (isClicked) R.drawable.btn_40_category_on else R.drawable.btn_40_category_off
            }
            GOAL -> {
                if (isClicked) R.drawable.btn_40_taget_count_on else R.drawable.btn_40_taget_count_off
            }
        },
        contentDescription = stringResource(
            id = when (type) {
                MEMO -> R.string.addMemoDesc
                IMAGE -> R.string.addImageDesc
                CATEGORY -> R.string.addCategoryDesc
                D_DAY -> R.string.addDdayDesc
                GOAL -> R.string.addGoalDesc
            }
        )
    )
}

sealed class WriteAppBarClickEvent {
    object CloseClicked : WriteAppBarClickEvent()
    object NextClicked : WriteAppBarClickEvent()
}

enum class BottomOptionType {
    MEMO, IMAGE, CATEGORY, D_DAY, GOAL
}