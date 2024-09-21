package com.zinc.waver.ui_more.components

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
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.zinc.waver.ui.design.theme.Gray1
import com.zinc.waver.ui_more.R
import com.zinc.waver.ui_more.models.WavePlusInfo

@Composable
internal fun WavePlusTopView(options: List<WavePlusInfo.Option>, modifier: Modifier = Modifier) {
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
            options.forEach {
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
