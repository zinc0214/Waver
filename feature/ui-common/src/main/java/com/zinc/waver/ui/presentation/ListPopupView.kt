package com.zinc.waver.ui.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.zinc.waver.ui.design.theme.Gray1
import com.zinc.waver.ui.design.theme.Gray9
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.util.dpToSp

@Composable
fun ListPopupView(
    modifier: Modifier = Modifier,
    title: String,
    onDismissRequest: (() -> Unit)? = null,
    clickable: Boolean = true,
    listBlock: @Composable () -> Unit
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
            shape = RoundedCornerShape(8.dp),
            color = Gray1
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {

                MyText(
                    text = title,
                    color = Gray9,
                    fontWeight = FontWeight.Bold,
                    fontSize = dpToSp(dp = 14.dp),
                    modifier = Modifier.padding(24.dp)
                )

                listBlock()
            }
        }
    }
}