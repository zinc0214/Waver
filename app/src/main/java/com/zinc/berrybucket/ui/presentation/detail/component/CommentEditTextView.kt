package com.zinc.berrybucket.ui.presentation.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.zinc.berrybucket.R
import com.zinc.berrybucket.ui.design.theme.Gray1
import com.zinc.berrybucket.ui.design.theme.Gray3
import com.zinc.berrybucket.ui.design.theme.Gray6
import com.zinc.berrybucket.ui.design.theme.Gray9
import com.zinc.berrybucket.ui.design.util.Keyboard
import com.zinc.berrybucket.ui.design.util.keyboardAsState
import com.zinc.berrybucket.ui.presentation.common.IconButton
import com.zinc.berrybucket.ui.presentation.common.IconToggleButton
import com.zinc.berrybucket.ui.presentation.common.MyText
import com.zinc.berrybucket.ui.presentation.common.MyTextField
import com.zinc.berrybucket.ui.presentation.detail.model.OpenDetailEditTextViewEvent
import com.zinc.berrybucket.ui.util.dpToSp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CommentEditTextView(
    originText: String,
    modifier: Modifier,
    commentEvent: (OpenDetailEditTextViewEvent) -> Unit
) {
    val isKeyboardStatus by keyboardAsState()
    val hintText = stringResource(id = R.string.commentHintText)
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var commentText by remember { mutableStateOf(TextFieldValue(originText)) }
    val likedState = remember { mutableStateOf(false) }

    if (isKeyboardStatus == Keyboard.Closed) {
        focusManager.clearFocus()
        keyboardController?.hide()
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Gray3)
    ) {

        IconToggleButton(
            modifier = Modifier
                .padding(top = 11.dp, start = 12.dp, bottom = 11.dp)
                .size(32.dp),
            checked = likedState.value,
            onCheckedChange = {
                likedState.value = !likedState.value
            },
            image = if (likedState.value) com.zinc.berrybucket.ui_common.R.drawable.btn_32_like_on else
                com.zinc.berrybucket.ui_common.R.drawable.btn_32_like_off,
            contentDescription = stringResource(id = R.string.likeButtonDesc),
        )

        IconButton(
            onClick = { commentEvent(OpenDetailEditTextViewEvent.MentionButtonClicked) },
            modifier = Modifier
                .padding(top = 11.dp, start = 4.dp, bottom = 11.dp)
                .size(32.dp),
            image = com.zinc.berrybucket.ui_my.R.drawable.btn_32_mention,
            contentDescription = stringResource(
                id = R.string.commentButtonDesc
            )
        )

        ConstraintLayout(
            modifier = Modifier
                .padding(start = 8.dp, top = 8.dp, bottom = 8.dp, end = 16.dp)
                .background(color = Gray1, shape = RoundedCornerShape(8.dp))
                .fillMaxWidth()
        ) {
            val (textField, addButton) = createRefs()

            MyTextField(
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp, start = 10.dp, end = 8.dp)
                    .constrainAs(textField) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(addButton.start)
                        width = Dimension.fillToConstraints
                    },
                value = commentText,
                textStyle = TextStyle(
                    color = Gray9,
                    fontSize = dpToSp(14.dp),
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Start
                ),
                onValueChange = {
                    commentText = it
                    // commentEvent.invoke(OpenDetailEditTextViewEvent.TextChanged(it.text))
                },
                maxLines = 3,
                decorationBox = { innerTextField ->
                    if (commentText.text.isBlank()) {
                        MyText(text = hintText, color = Gray6, fontSize = dpToSp(14.dp))
                    }
                    Row {
                        innerTextField()  //<-- Add this
                    }
                },
            )

            IconButton(modifier = Modifier
                .padding(4.dp)
                .then(Modifier.size(28.dp))
                .constrainAs(addButton) {
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .alpha(if (isKeyboardStatus == Keyboard.Opened) 1f else 0f),
                onClick = {
                    focusManager.clearFocus()
                    commentEvent.invoke(OpenDetailEditTextViewEvent.SendComment(commentText.text))

                },
                enabled = commentText.text.isNotBlank(),
                image = if (commentText.text.isBlank()) R.drawable.comment_send_off else R.drawable.comment_send_on,
                contentDescription = stringResource(id = R.string.commentSendButtonDesc)
            )
        }
    }
}

@Composable
@Preview
private fun CommentEditTextPreView() {
    CommentEditTextView(
        modifier = Modifier,
        originText = "", commentEvent = {},
    )
}
