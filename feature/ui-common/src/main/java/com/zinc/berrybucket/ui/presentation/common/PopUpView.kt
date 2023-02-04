package com.zinc.berrybucket.ui.presentation.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.zinc.berrybucket.ui.design.theme.Gray1
import com.zinc.berrybucket.ui.design.theme.Gray10
import com.zinc.berrybucket.ui.design.theme.Gray4
import com.zinc.berrybucket.ui.design.theme.Gray7
import com.zinc.berrybucket.ui.design.theme.Gray9
import com.zinc.berrybucket.ui.util.dpToSp

@Composable
fun PopUpView(
    modifier: Modifier = Modifier,
    title: String,
    cancelText: String? = null,
    positiveText: String? = null,
    cancelClicked: (() -> Unit)? = null,
    positiveClicked: (() -> Unit)? = null,
    onDismissRequest: (() -> Unit)? = null,
    clickable: Boolean = true
) {
    Dialog(
        properties = DialogProperties(
            dismissOnBackPress = clickable,
            dismissOnClickOutside = clickable
        ),
        onDismissRequest = {
            if (onDismissRequest != null) onDismissRequest()
        }) {
        Surface(
            modifier = Modifier
                .width(300.dp)
                .wrapContentHeight(),
            shape = RoundedCornerShape(3.dp),
            color = Gray1
        ) {
            PopUpView(
                title = title,
                cancelText = cancelText,
                positiveText = positiveText,
                cancelClicked = cancelClicked,
                positiveClicked = positiveClicked
            )
        }

    }
}

@Composable
private fun PopUpView(
    title: String,
    cancelText: String? = null,
    positiveText: String? = null,
    cancelClicked: (() -> Unit)? = null,
    positiveClicked: (() -> Unit)? = null,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {

        MyText(
            text = title,
            color = Gray9,
            fontSize = dpToSp(dp = 16.dp),
            modifier = Modifier.padding(horizontal = 28.dp, vertical = 40.dp)
        )

        Divider(color = Gray4)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
        ) {
            if (cancelText != null) {
                Box(
                    modifier = Modifier
                        .weight(1f, true)
                        .fillMaxHeight()
                        .clickable {
                            cancelClicked?.let { it() }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    MyText(
                        text = cancelText,
                        fontSize = dpToSp(dp = 16.dp),
                        color = Gray7
                    )
                }
            }

            if (cancelText != null) {
                Divider(
                    color = Gray4, modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                )
            }

            if (positiveText != null) {
                Box(
                    modifier = Modifier
                        .weight(1f, true)
                        .fillMaxHeight()
                        .clickable {
                            positiveClicked?.let { it() }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    MyText(
                        text = positiveText,
                        fontSize = dpToSp(dp = 16.dp),
                        color = Gray10
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun PopUpPreview() {
    PopUpView(
        title = "확인해볼끼요?",
        cancelText = "취소",
        cancelClicked = {}
    )
}