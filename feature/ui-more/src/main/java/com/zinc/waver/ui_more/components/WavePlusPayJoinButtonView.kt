package com.zinc.waver.ui_more.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zinc.waver.ui.design.theme.Gray1
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.ui_more.R

@Composable
internal fun WavePlusPayJoinButtonView(
    joinButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier
        .fillMaxWidth()
        .clickable {
            joinButtonClicked()
        }) {

        Image(
            painter = painterResource(id = R.drawable.bottom_wave),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )

        MyText(
            text = stringResource(id = R.string.startWavePlus),
            modifier = Modifier
                .padding(top = 28.dp, bottom = 18.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = Gray1,
            fontSize = dpToSp(dp = 18.dp),
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview
@Composable
private fun WavePlusPayJoinButtonPreview() {
    WavePlusPayJoinButtonView(joinButtonClicked = {})
}