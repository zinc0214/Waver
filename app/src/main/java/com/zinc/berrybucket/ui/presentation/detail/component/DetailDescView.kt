package com.zinc.berrybucket.ui.presentation.detail.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.zinc.berrybucket.R
import com.zinc.berrybucket.model.CommonDetailDescInfo
import com.zinc.berrybucket.ui.design.theme.Error2
import com.zinc.berrybucket.ui.design.theme.Gray10
import com.zinc.berrybucket.ui.presentation.common.TagListView
import com.zinc.berrybucket.ui.util.dpToSp

@Composable
fun DetailDescView(detailDescInfo: CommonDetailDescInfo) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .padding(top = 30.dp)
    ) {
        DdayView(
            modifier = Modifier.padding(start = 14.dp),
            dday = detailDescInfo.dDay
        )

        TagListView(
            modifier = Modifier.padding(top = 26.dp, start = 14.dp),
            tagList = detailDescInfo.tagList
        )

        TitleView(
            modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
            title = detailDescInfo.title
        )
    }
}

@Composable
private fun DdayView(modifier: Modifier = Modifier, dday: String) {
    Box(modifier = modifier,
        content = {
            Image(
                painter = painterResource(R.drawable.detail_dday_img),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(65.dp, 22.dp)
                    .align(Alignment.Center)
            )
            Text(
                text = dday,
                color = Error2,
                fontSize = dpToSp(14.dp),
                modifier = Modifier.align(Alignment.Center)
            )
        }
    )
}

@Composable
private fun TitleView(modifier: Modifier = Modifier, title: String) {
    Text(
        text = title,
        color = Gray10,
        fontSize = dpToSp(24.dp),
        modifier = modifier
    )
}
