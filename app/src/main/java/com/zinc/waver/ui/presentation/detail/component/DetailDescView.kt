package com.zinc.waver.ui.presentation.detail.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zinc.common.models.BucketStatus
import com.zinc.waver.model.DetailDescType
import com.zinc.waver.ui.design.theme.Error2
import com.zinc.waver.ui.design.theme.Gray10
import com.zinc.waver.ui.design.theme.Main4
import com.zinc.waver.ui.design.theme.Main5
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.presentation.component.TagListView
import com.zinc.waver.ui.presentation.detail.model.StatusInfo
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.ui_common.R as CommonR

@Composable
fun DetailDescView(detailDescInfo: DetailDescType.CommonDetailDescInfo) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp)
            .padding(top = 23.dp)
    ) {
        StatusView(
            modifier = Modifier.padding(bottom = 26.dp),
            dday = detailDescInfo.dDay,
            status = detailDescInfo.status
        )

        detailDescInfo.keywordList?.let { tagList ->
            TagListView(
                modifier = Modifier.padding(bottom = 26.dp),
                tagList = tagList.map { it.text }
            )
        }

        TitleView(
            title = detailDescInfo.title
        )
    }
}

@Composable
private fun StatusView(modifier: Modifier = Modifier, dday: String?, status: BucketStatus) {

    val statusInfo = if (dday.isNullOrBlank().not()) {
        StatusInfo(
            backgroundImg = CommonR.drawable.ststus_dday_img,
            text = dday.orEmpty(),
            textColor = Error2
        )

    } else if (status == BucketStatus.PROGRESS) {
        StatusInfo(
            backgroundImg = CommonR.drawable.status_process_img,
            text = stringResource(id = CommonR.string.proceedingText),
            textColor = Main4
        )
    } else {
        StatusInfo(
            backgroundImg = CommonR.drawable.stauts_success_img,
            text = stringResource(id = CommonR.string.succeedText),
            textColor = Main5
        )
    }

    Box(
        modifier = modifier,
        content = {
            Image(
                painter = painterResource(statusInfo.backgroundImg),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(61.dp, 21.dp)
                    .align(Alignment.Center)
            )
            MyText(
                text = statusInfo.text,
                color = statusInfo.textColor,
                fontSize = dpToSp(13.dp),
                modifier = Modifier.align(Alignment.Center)
            )
        }
    )
}

@Composable
private fun TitleView(modifier: Modifier = Modifier, title: String) {
    MyText(
        text = title,
        color = Gray10,
        fontSize = dpToSp(24.dp),
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}
