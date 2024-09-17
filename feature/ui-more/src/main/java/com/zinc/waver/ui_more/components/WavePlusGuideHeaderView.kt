package com.zinc.waver.ui_more.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zinc.waver.ui.design.theme.Gray1
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.ui_more.R

@Composable
internal fun WavePlusGuideHeaderView(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(id = R.drawable.bg_membership),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )

        Column {

            Spacer(modifier = Modifier.height(27.dp))

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
                text = stringResource(id = R.string.wavePlusGuide),
                fontSize = dpToSp(dp = 22.dp),
                color = Gray1,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 32.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HeaderPreview() {
    WavePlusGuideHeaderView()
}
