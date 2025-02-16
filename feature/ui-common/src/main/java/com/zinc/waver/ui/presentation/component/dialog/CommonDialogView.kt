package com.zinc.waver.ui.presentation.component.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zinc.waver.model.DialogButtonInfo
import com.zinc.waver.ui.design.theme.Gray1
import com.zinc.waver.ui.design.theme.Gray10
import com.zinc.waver.ui.design.theme.Gray4
import com.zinc.waver.ui.design.theme.Gray9
import com.zinc.waver.ui.design.theme.Main4
import com.zinc.waver.ui.presentation.component.BottomButtonView
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.ui_common.R

@Composable
fun CommonDialogView(
    title: String? = null,
    message: String? = null,
    dismissAvailable: Boolean = true,
    leftButtonInfo: DialogButtonInfo? = null,
    rightButtonInfo: DialogButtonInfo,
    leftButtonEvent: (() -> Unit)? = null,
    rightButtonEvent: () -> Unit
) {
    DialogView(
        dismissOnBackPress = dismissAvailable,
        dismissOnClickOutside = dismissAvailable,
        usePlatformDefaultWidth = false,
        dismissEvent = {
            rightButtonEvent()
        },
        content = {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(3.dp),
                backgroundColor = Gray1,
                elevation = 3.dp
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    Spacer(modifier = Modifier.padding(top = 32.dp))
                    // Title 영역

                    title?.let {
                        MyText(
                            modifier = Modifier
                                .padding(horizontal = 28.dp)
                                .fillMaxWidth(),
                            text = title,
                            fontSize = dpToSp(dp = 18.dp),
                            color = Gray10,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start
                        )
                    }


                    message?.let {
                        // Message 영역
                        MyText(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 28.dp)
                                .padding(top = 16.dp),
                            text = message,
                            fontSize = dpToSp(dp = 16.dp),
                            color = Gray9,
                            textAlign = TextAlign.Start
                        )
                    }

                    Spacer(modifier = Modifier.padding(top = 32.dp))

                    Divider(color = Gray4, thickness = 1.dp)

                    BottomButtonView(
                        negative = leftButtonInfo,
                        positive = rightButtonInfo,
                        negativeEvent = { leftButtonEvent?.invoke() },
                        positiveEvent = {
                            rightButtonEvent()
                        }
                    )
                }
            }
        }
    )
}

@Composable
@Preview
private fun TitleAndMessageDialogPreview() {
    CommonDialogView(
        title = "추가실패",
        message = "뾰잉",
        dismissAvailable = false,
        rightButtonInfo = DialogButtonInfo(text = R.string.confirm, color = Main4),
        leftButtonInfo = DialogButtonInfo(text = R.string.confirm, color = Gray9),
        rightButtonEvent = {}
    )
}