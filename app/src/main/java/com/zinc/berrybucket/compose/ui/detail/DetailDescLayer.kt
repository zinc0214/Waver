package com.zinc.berrybucket.compose.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zinc.berrybucket.R
import com.zinc.berrybucket.compose.theme.Error2
import com.zinc.berrybucket.compose.theme.Gray10
import com.zinc.berrybucket.compose.theme.Main3
import com.zinc.berrybucket.model.DetailDescInfo

@Composable
fun DetailDescLayer(detailDescInfo: DetailDescInfo) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {
        Column {
            DdayView(
                modifier = Modifier.padding(top = 24.dp, start = 14.dp),
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
                fontSize = 14.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    )
}

@Composable
private fun TagListView(modifier: Modifier = Modifier, tagList: List<String>) {
    Row(modifier = modifier) {
        tagList.forEach { tag ->
            TagView(modifier = Modifier.padding(end = 5.dp), tag)
        }
    }
}

@Composable
private fun TagView(modifier: Modifier = Modifier, tag: String) {
    Text(
        text = "# $tag",
        color = Main3,
        fontSize = 15.sp,
        modifier = modifier
    )
}

@Composable
private fun TitleView(modifier: Modifier = Modifier, title: String) {
    Text(
        text = title,
        color = Gray10,
        fontSize = 24.sp,
        modifier = modifier
    )
}
