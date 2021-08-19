package com.zinc.mybury_2.compose.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zinc.mybury_2.compose.theme.BaseTheme
import com.zinc.mybury_2.compose.theme.Gray1
import com.zinc.mybury_2.compose.theme.Gray11
import com.zinc.mybury_2.compose.theme.Main3

@Composable
fun BucketCard(
    title: String,
    titleColor: Color = Gray11,
    backgroundColor: Color = Gray1,
    borderColor: Color = Main3,
    borderWidth: Dp = 2.dp
) {
    Row(
        modifier = Modifier
            .background(backgroundColor)
            .border(border = BorderStroke(borderWidth, borderColor))
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(
                modifier = Modifier
                    .height(21.dp)
            )
            Text(
                text = title,
                color = titleColor,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 16.dp)
            )
            Spacer(
                modifier = Modifier
                    .height(21.dp)
            )
        }
        Box(
            contentAlignment = Alignment.CenterEnd,
            modifier = Modifier
                .padding(end = 16.dp)
                .wrapContentWidth(Alignment.End)
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            BucketCircularProgressBar()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CardPreview() {
    BaseTheme {
        BucketCard(
            title = "버킷버킷한 버킷리스트 버킷버킷한 버킷리스트 버킷버킷한 버킷리스트"
        )
    }
}