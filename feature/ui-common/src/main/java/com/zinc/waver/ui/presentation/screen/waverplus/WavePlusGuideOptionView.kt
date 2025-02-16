package com.zinc.waver.ui.presentation.screen.waverplus

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zinc.waver.ui.design.theme.Gray1
import com.zinc.waver.ui.design.theme.Gray6
import com.zinc.waver.ui.design.theme.Main2
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.presentation.model.WaverPlusOption
import com.zinc.waver.ui.util.HtmlText2
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.ui_common.R

@Composable
internal fun WavePlusGuideOptionView(option: WaverPlusOption, modifier: Modifier = Modifier) {
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
                painter = option.imgResource,
                contentDescription = null,
                modifier = Modifier.size(56.dp),
                contentScale = ContentScale.FillWidth
            )

            Column(modifier = Modifier.padding(start = 40.dp)) {
                HtmlText2(
                    html = option.title,
                    fontSize = 15.dp
                )

                MyText(
                    text = option.content,
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
        option = WaverPlusOption(
            painterResource(id = R.drawable.img_together_unlimited),
            title = stringResource(id = R.string.wavePlusOption3_1), content = "클린한 웨이버 피드"
        ), modifier = Modifier
    )
}