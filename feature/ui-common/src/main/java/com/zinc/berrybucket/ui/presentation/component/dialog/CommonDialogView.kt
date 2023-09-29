package com.zinc.berrybucket.ui.presentation.component.dialog

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
import com.zinc.berrybucket.model.DialogButtonInfo
import com.zinc.berrybucket.ui.design.theme.Gray1
import com.zinc.berrybucket.ui.design.theme.Gray10
import com.zinc.berrybucket.ui.design.theme.Gray4
import com.zinc.berrybucket.ui.design.theme.Gray9
import com.zinc.berrybucket.ui.design.theme.Main4
import com.zinc.berrybucket.ui.presentation.component.BottomButtonView
import com.zinc.berrybucket.ui.presentation.component.MyText
import com.zinc.berrybucket.ui.util.dpToSp
import com.zinc.berrybucket.ui_common.R

@Composable
fun CommonDialogView(
    title: String? = null,
    message: String? = null,
    dismissAvailable: Boolean = true,
    negative: DialogButtonInfo? = null,
    positive: DialogButtonInfo,
    negativeEvent: (() -> Unit)? = null,
    positiveEvent: () -> Unit
) {
    DialogView(
        dismissOnBackPress = dismissAvailable,
        dismissOnClickOutside = dismissAvailable,
        usePlatformDefaultWidth = false,
        dismissEvent = {
            positiveEvent()
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
                            fontSize = dpToSp(dp = 12.dp),
                            color = Gray9,
                            textAlign = TextAlign.Start
                        )
                    }

                    Spacer(modifier = Modifier.padding(top = 32.dp))

                    Divider(color = Gray4, thickness = 1.dp)

                    BottomButtonView(
                        negative = negative,
                        positive = positive,
                        negativeEvent = { negativeEvent?.invoke() },
                        positiveEvent = { positiveEvent() }
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
        positive = DialogButtonInfo(text = R.string.confirm, color = Main4),
        positiveEvent = {}
    )
}