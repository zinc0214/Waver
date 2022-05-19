package com.zinc.berrybucket.compose.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.IconToggleButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.zinc.berrybucket.R
import com.zinc.berrybucket.compose.theme.Gray1
import com.zinc.berrybucket.compose.theme.Gray3
import com.zinc.berrybucket.compose.theme.Gray6
import com.zinc.berrybucket.compose.theme.Gray9
import com.zinc.berrybucket.compose.util.Keyboard
import com.zinc.berrybucket.compose.util.keyboardAsState

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CommentEditTextView(
    modifier: Modifier,
    onImeAction: (String) -> Unit
) {
    val isKeyboardStatus by keyboardAsState()
    val hintText = stringResource(id = R.string.commentHintText)
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var commentText by remember { mutableStateOf(TextFieldValue("")) }
    val likedState = remember { mutableStateOf(false) }

    if (isKeyboardStatus == Keyboard.Closed) {
        focusManager.clearFocus()
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
            }) {
            Icon(
                painter = if (likedState.value) painterResource(id = R.drawable.btn_32_like_on)
                else painterResource(id = R.drawable.btn_32_like_off),
                contentDescription = stringResource(id = R.string.likeButtonDesc),
            )
        }

        ConstraintLayout(
            modifier = Modifier
                .padding(start = 8.dp, top = 8.dp, bottom = 8.dp, end = 16.dp)
                .background(color = Gray1, shape = RoundedCornerShape(8.dp))
                .fillMaxWidth()
        ) {
            val (textField, addButton) = createRefs()

            BasicTextField(
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
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Start
                ),
                onValueChange = { commentText = it },
                maxLines = 3,
//                keyboardOptions = KeyboardOptions.Default.copy(
//                    autoCorrect = true
//                ),
//                keyboardActions = KeyboardActions {
//                    onImeAction(commentText.text)
//                    keyboardController?.hide()
//                    focusManager.clearFocus()
//                    Log.e("ayahn", "action : onANy")
//                },
                decorationBox = { innerTextField ->
                    if (commentText.text.isBlank()) {
                        Text(text = hintText, color = Gray6, fontSize = 14.sp)
                    }
                    Row {
                        innerTextField()  //<-- Add this
                    }
                },
            )

            IconButton(
                modifier = Modifier
                    .padding(4.dp)
                    .then(Modifier.size(28.dp))
                    .constrainAs(addButton) {
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .alpha(if (isKeyboardStatus == Keyboard.Opened) 1f else 0f),
                onClick = {
                    focusManager.clearFocus()
                }) {
                Icon(
                    painter =
                    if (commentText.text.isBlank())
                        painterResource(id = R.drawable.comment_send_off)
                    else painterResource(
                        id = R.drawable.comment_send_on
                    ),
                    contentDescription = stringResource(id = R.string.commentSendButtonDesc)
                )
            }
        }
    }
}