package com.zinc.waver.ui_more.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zinc.waver.ui.design.theme.Gray1
import com.zinc.waver.ui.presentation.component.IconButton
import com.zinc.waver.ui_more.components.WavePlusPayJoinButtonView
import com.zinc.waver.ui_more.components.WavePlusPayView
import com.zinc.waver.ui_more.components.WavePlusTopView
import com.zinc.waver.ui_more.models.WavePlusInfo
import com.zinc.waver.ui_common.R as CommonR

@Composable
fun WavePlusGuideScreen(onBackPressed: () -> Unit) {

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Gray1)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xff2375e9))
        ) {
            IconButton(image = CommonR.drawable.btn_40_close,
                contentDescription = stringResource(id = CommonR.string.closeDesc),
                modifier = Modifier
                    .padding(start = 14.dp, top = 32.dp)
                    .size(40.dp),
                colorFilter = ColorFilter.tint(color = Gray1),
                onClick = { onBackPressed() })
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {

            WavePlusTopView(dummy)

            WavePlusPayView(info)

            Spacer(modifier = Modifier.height(45.dp))

            WavePlusPayJoinButtonView({

            })
        }
    }
}

private val dummy = buildList {
    add(
        WavePlusInfo.Option(
            url = "", title = "모든", subTitle = "광고제거", content = "클린한 웨이버 피드"
        )
    )
    add(
        WavePlusInfo.Option(

            url = "", title = "무제한", subTitle = "함께하기 가능", content = "친구들과 더 많은 챌린지"
        )
    )
    add(
        WavePlusInfo.Option(
            url = "", title = "더 많은", subTitle = "사진 업로드", content = "나만의 버킷리스트"
        )
    )
    add(
        WavePlusInfo.Option(
            url = "", title = "무제한", subTitle = "버킷리스트", content = "나만의 다양한 챌린지드"
        )
    )
}

private val info = WavePlusInfo(
    options = dummy,
    yearPerPay = "10,000원",
    yearBenefitText = "1주일 무료 + 웨이버 플러스",
    yearPay = "50,000원",
    monthPay = "12,000원",
    monthBenefitText = "웨이버 플러스"
)


@Preview
@Composable
private fun WavePlusGuidePreview() {
    WavePlusGuideScreen {}
}