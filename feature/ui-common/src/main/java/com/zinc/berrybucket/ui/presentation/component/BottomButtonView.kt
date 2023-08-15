package com.zinc.berrybucket.ui.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zinc.berrybucket.model.DialogButtonInfo
import com.zinc.berrybucket.ui.design.theme.Gray1
import com.zinc.berrybucket.ui.design.theme.Gray6
import com.zinc.berrybucket.ui.design.theme.Main4
import com.zinc.berrybucket.ui.util.dpToSp
import com.zinc.berrybucket.ui_common.R

@Composable
fun BottomButtonView(
    negative: DialogButtonInfo? = DialogButtonInfo(
        text = R.string.cancel, color = Gray6
    ),
    positive: DialogButtonInfo = DialogButtonInfo(
        text = R.string.apply, color = Main4
    ),
    negativeEvent: (() -> Unit)? = null,
    positiveEvent: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .background(color = Gray1)
    ) {
        Divider(
            modifier = Modifier
                .height(0.6.dp)
                .fillMaxWidth()
                .background(color = colorResource(id = R.color._dbdbdb))
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            //  val (leftContent, divider, rightContent) = createRefs()

            if (negativeEvent != null && negative != null) {
                Box(
                    modifier = Modifier
                        .weight(3f, true)
                        .fillMaxHeight()
                        .fillMaxHeight()
                        .clickable {
                            negativeEvent()
                        },

                    ) {
                    MyText(
                        modifier = Modifier.align(Alignment.Center),
                        text = stringResource(id = negative.text),
                        color = negative.color,
                        textAlign = TextAlign.Center,
                        fontSize = dpToSp(16.dp)
                    )
                }

                Divider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(0.6.dp)
                        .background(
                            color = colorResource(
                                id = R.color._dbdbdb
                            )
                        )
                )
            }


            Box(
                modifier = Modifier
                    .weight(3f, true)
                    .fillMaxHeight()
                    .clickable {
                        positiveEvent()
                    }

            ) {
                MyText(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(id = positive.text),
                    color = positive.color,
                    textAlign = TextAlign.Center,
                    fontSize = dpToSp(16.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun BottomButtonPreview() {
    BottomButtonView(
        negative = null,
        positive = DialogButtonInfo(
            text = R.string.apply, color = Main4
        ),
        positiveEvent = {}
    )
}