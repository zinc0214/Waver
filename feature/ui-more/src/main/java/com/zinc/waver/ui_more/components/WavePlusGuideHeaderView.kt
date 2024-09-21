package com.zinc.waver.ui_more.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.zinc.waver.ui.design.theme.Gray1
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.ui_more.R

@Composable
internal fun WavePlusGuideHeaderView(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF2375e9))
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_membership),
            contentDescription = null,
            modifier = Modifier
                .size(width = 340.dp, height = 350.dp)
                .align(Alignment.Center),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .padding(top = 99.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            MyText(
                text = stringResource(id = R.string.wavePlus),
                fontSize = dpToSp(dp = 13.dp),
                color = Gray1,
                modifier = Modifier
                    .padding(horizontal = 28.dp)
                    .background(color = Gray1.copy(alpha = 0.1f), shape = RoundedCornerShape(18.dp))
                    .border(
                        width = 1.dp,
                        color = Gray1.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(18.dp)
                    )
                    .padding(start = 10.dp, end = 10.dp, top = 2.dp, bottom = 3.dp)
            )

            MyText(
                text = stringResource(id = R.string.wavePlusGuide1),
                fontSize = dpToSp(dp = 22.dp),
                color = Gray1,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )

            Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                ConstraintLayout {
                    val (text, divider) = createRefs()

                    MyText(
                        text = stringResource(id = R.string.wavePlusGuide2),
                        fontSize = dpToSp(dp = 22.dp),
                        color = Gray1,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .constrainAs(text) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                    )

                    Divider(
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .constrainAs(divider) {
                                top.linkTo(text.bottom)
                                start.linkTo(text.start)
                                end.linkTo(text.end)
                                width = Dimension.fillToConstraints
                            },
                        thickness = 2.dp,
                        color = Gray1
                    )
                }


                MyText(
                    text = " " + stringResource(id = R.string.wavePlusGuide3),
                    fontSize = dpToSp(dp = 22.dp),
                    color = Gray1,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HeaderPreview() {
    WavePlusGuideHeaderView()
}
