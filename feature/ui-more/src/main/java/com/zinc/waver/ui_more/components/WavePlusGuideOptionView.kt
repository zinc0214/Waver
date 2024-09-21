package com.zinc.waver.ui_more.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.zinc.waver.ui.design.theme.Gray1
import com.zinc.waver.ui.design.theme.Gray6
import com.zinc.waver.ui.design.theme.Gray9
import com.zinc.waver.ui.design.theme.Main2
import com.zinc.waver.ui.design.theme.Main4
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.ui_more.models.WavePlusInfo
import com.zinc.waver.ui_common.R as CommonR

@Composable
internal fun WavePlusGuideOptionView(options: WavePlusInfo.Option, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(horizontal = 30.dp)
            .width(300.dp)
            .background(color = Gray1, shape = RoundedCornerShape(8.dp))
            .border(width = 0.6.dp, color = Main2.copy(0.6f), shape = RoundedCornerShape(8.dp))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = options.url,
                    error = painterResource(CommonR.drawable.testimg),
                    placeholder = painterResource(CommonR.drawable.testimg)
                ),
                contentDescription = null,
                modifier = Modifier.size(56.dp),
                contentScale = ContentScale.FillWidth
            )

            Column(modifier = Modifier.padding(start = 40.dp)) {
                MyText(text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Gray9)) {
                        append(options.title + " ")
                    }
                    withStyle(style = SpanStyle(color = Main4)) {
                        append(options.subTitle)
                    }
                }, fontSize = dpToSp(dp = 15.dp))

                MyText(
                    text = options.content,
                    modifier = Modifier.padding(top = 3.dp),
                    fontSize = dpToSp(
                        dp = 15.dp
                    ),
                    color = Gray6
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WavePlusGuideOptionPreView() {
    WavePlusGuideOptionView(
        options = WavePlusInfo.Option(
            url = "", title = "모든", subTitle = "광고제거", content = "클린한 웨이버 피드"
        ), modifier = Modifier
    )
}