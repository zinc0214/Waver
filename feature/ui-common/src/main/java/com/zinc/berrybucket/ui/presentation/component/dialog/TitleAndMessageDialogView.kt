package com.zinc.berrybucket.ui.presentation.component.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zinc.berrybucket.ui.design.theme.Gray1
import com.zinc.berrybucket.ui.design.theme.Gray10
import com.zinc.berrybucket.ui.design.theme.Main4
import com.zinc.berrybucket.ui.presentation.component.MyText
import com.zinc.berrybucket.ui.util.dpToSp
import com.zinc.berrybucket.ui_common.R

@Composable
fun TitleAndMessageDialogView(
    title: String,
    message: String,
    dismissAvailable: Boolean = true,
    dismissEvent: () -> Unit
) {
    DialogView(
        dismissOnBackPress = dismissAvailable,
        dismissOnClickOutside = dismissAvailable,
        usePlatformDefaultWidth = false,
        dismissEvent = {
            dismissEvent()
        },
        content = {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp),
                shape = RoundedCornerShape(8.dp),
                backgroundColor = Gray1,
                elevation = 3.dp
            ) {

                Column(modifier = Modifier.fillMaxWidth()) {

                    // Title 영역
                    MyText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp, vertical = 28.dp),
                        text = title,
                        fontSize = dpToSp(dp = 15.dp),
                        color = Gray10,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    // Message 영역
                    MyText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        text = message,
                        fontSize = dpToSp(dp = 12.dp),
                        color = Gray10,
                        textAlign = TextAlign.Center
                    )


                    Box(
                        modifier = Modifier
                            .height(56.dp)
                            .fillMaxWidth()
                            .clickable {
                                dismissEvent()
                            }

                    ) {
                        MyText(
                            modifier = Modifier.align(Alignment.Center),
                            text = stringResource(id = R.string.confirm),
                            color = Main4,
                            textAlign = TextAlign.Center,
                            fontSize = dpToSp(16.dp)
                        )
                    }
                }
            }
        }
    )
}

@Composable
@Preview
private fun TitleAndMessageDialogPreview() {
    TitleAndMessageDialogView(
        title = "추가실패",
        message = "뾰잉",
        dismissAvailable = false,
        dismissEvent = {}

    )
}