package com.zinc.waver.ui_write.presentation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.zinc.waver.model.WriteOptionsType1
import com.zinc.waver.model.WriteOptionsType1.CATEGORY
import com.zinc.waver.model.WriteOptionsType1.D_DAY
import com.zinc.waver.model.WriteOptionsType1.GOAL
import com.zinc.waver.model.WriteOptionsType1.IMAGE
import com.zinc.waver.model.WriteOptionsType1.MEMO
import com.zinc.waver.ui.design.theme.Gray10
import com.zinc.waver.ui.design.theme.Gray2
import com.zinc.waver.ui.design.theme.Gray3
import com.zinc.waver.ui.design.theme.Gray6
import com.zinc.waver.ui.design.theme.Gray7
import com.zinc.waver.ui.design.theme.Main4
import com.zinc.waver.ui.presentation.component.IconButton
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.presentation.component.MyTextField
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.ui_write.R
import com.zinc.waver.ui_common.R as CommonR

@Composable
fun WriteAppBar(
    modifier: Modifier,
    rightText: Int,
    @DrawableRes leftIcon: Int = CommonR.drawable.btn_40_close,
    clickEvent: (WriteAppBarClickEvent) -> Unit,
    nextButtonClickable: Boolean,
    isShowDivider: Boolean = true
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 52.dp)
    ) {
        val (closeButton, moreButton, divider) = createRefs()

        IconButton(
            image = leftIcon,
            contentDescription = stringResource(id = com.zinc.waver.ui_common.R.string.closeDesc),
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
            })

        MyText(
            modifier = Modifier
                .constrainAs(moreButton) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .padding(start = 10.dp, end = 18.dp, top = 10.dp, bottom = 10.dp)
                .clickable(
                    enabled = nextButtonClickable,
                    onClick = { clickEvent(WriteAppBarClickEvent.NextClicked) })
                .padding(start = 10.dp, end = 10.dp, top = 6.dp, bottom = 6.dp),
            text = stringResource(id = rightText),
            color = if (nextButtonClickable) Main4 else Gray6,
            fontSize = dpToSp(18.dp),
        )

        if (isShowDivider) {
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
}

@Composable
fun WriteTitleFieldView(
    modifier: Modifier,
    title: String,
    shouldFocus: Boolean = false,
    textChanged: (String) -> Unit
) {
    val hintText = stringResource(id = R.string.writeTitleHintText)
    var titleText by remember { mutableStateOf(TextFieldValue(title)) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(shouldFocus) {
        if (shouldFocus) {
            try {
                focusRequester.requestFocus()
                keyboardController?.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    MyTextField(
        modifier = modifier
            .focusRequester(focusRequester),
        value = titleText,
        textStyle = TextStyle(
            color = Gray10,
            fontSize = dpToSp(24.dp),
            fontWeight = FontWeight.Medium
        ),
        onValueChange = {
            titleText = it
            textChanged(titleText.text)
        },
        singleLine = true,
        decorationBox = { innerTextField ->
            Row {
                if (titleText.text.isEmpty()) {
                    MyText(text = hintText, color = Gray6, fontSize = dpToSp(24.dp))
                }
                innerTextField()
            }
        }
    )
}

@Composable
fun MemoOptionView(
    modifier: Modifier = Modifier,
    memoText: String
) {
    Box(
        modifier = modifier
            .background(color = Gray2)
            .fillMaxWidth()
    ) {
        MyText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 28.dp)
                .align(Alignment.Center),
            text = memoText,
            color = Gray7,
            fontSize = dpToSp(14.dp),
            maxLines = 5,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun BottomOptionView(
    modifier: Modifier = Modifier,
    currentClickedOptions: Set<WriteOptionsType1>,
    optionClicked: (WriteOptionsType1) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {

        Divider(
            modifier = Modifier.fillMaxWidth(), color = Gray3
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            WriteOptionsType1.values().forEach {
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
    currentClickedOptions: Set<WriteOptionsType1>,
    type: WriteOptionsType1,
    optionClicked: (WriteOptionsType1) -> Unit
) {
    val isUsed = currentClickedOptions.find { it == type } != null
    IconButton(
        modifier = Modifier.size(40.dp),
        onClick = { optionClicked(type) },
        image = when (type) {
            MEMO -> {
                if (isUsed) R.drawable.btn_40_memo_on else R.drawable.btn_40_memo_off
            }

            IMAGE -> {
                if (isUsed) R.drawable.btn_40_gallery_on else R.drawable.btn_40_gallery_off
            }

            CATEGORY -> {
                if (isUsed) R.drawable.btn_40_category_on else R.drawable.btn_40_category_off
            }

            D_DAY -> {
                if (isUsed) R.drawable.btn_40_calendar_on else R.drawable.btn_40_calendar_off
            }

            GOAL -> {
                if (isUsed) R.drawable.btn_40_taget_count_on else R.drawable.btn_40_taget_count_off
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