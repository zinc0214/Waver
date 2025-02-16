package com.zinc.waver.ui.presentation.screen.waverplus

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.zinc.waver.ui.design.theme.Gray1
import com.zinc.waver.ui.presentation.model.WaverPlusOption
import com.zinc.waver.ui_common.R

@Composable
internal fun WavePlusTopView(modifier: Modifier = Modifier) {

    val optionsList = buildList {
        add(
            WaverPlusOption(
                imgResource = painterResource(id = R.drawable.img_ad_remove),
                title = stringResource(id = R.string.wavePlusOption1_1),
                content = stringResource(
                    id = R.string.wavePlusOption1_2
                )
            )
        )
        add(
            WaverPlusOption(
                imgResource = painterResource(id = R.drawable.img_together_unlimited),
                title = stringResource(id = R.string.wavePlusOption2_1),
                content = stringResource(
                    id = R.string.wavePlusOption2_2
                )
            )
        )
        add(
            WaverPlusOption(
                imgResource = painterResource(id = R.drawable.img_ad_remove),
                title = stringResource(id = R.string.wavePlusOption3_1),
                content = stringResource(
                    id = R.string.wavePlusOption3_2
                )
            )
        )
        add(
            WaverPlusOption(
                imgResource = painterResource(id = R.drawable.img_list_unlimited),
                title = stringResource(id = R.string.wavePlusOption4_1),
                content = stringResource(
                    id = R.string.wavePlusOption4_2
                )
            )
        )
    }


    ConstraintLayout(modifier = modifier.background(Gray1)) {
        val (header, content, logo) = createRefs()

        WavePlusGuideHeaderView(
            modifier = Modifier.constrainAs(header) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(content) {
                    top.linkTo(header.bottom)
                    linkTo(
                        top = header.bottom,
                        bottom = parent.bottom,
                        topMargin = (-45).dp,
                        bias = 0f
                    )
                }
                .padding(bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            optionsList.forEach {
                WavePlusGuideOptionView(it)
            }
        }


        Image(
            painter = painterResource(id = R.drawable.img_wave_plus),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 45.dp)
                .size(width = 360.dp, height = 310.dp)
                .constrainAs(logo) {
                    top.linkTo(content.top)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                },
            contentScale = ContentScale.FillWidth
        )

    }
}

@Preview
@Composable
private fun WavePlusTopPreview() {
    WavePlusTopView()
}