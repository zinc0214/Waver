package com.zinc.berrybucket.ui.presentation.write

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zinc.berrybucket.ui.compose.theme.Gray10

@Composable
fun WriteTitleView(
    modifier: Modifier = Modifier,
    title: String
) {
    Text(
        modifier = modifier
            .padding(start = 28.dp, top = 28.dp, end = 28.dp)
            .fillMaxWidth(),
        text = title,
        fontSize = 24.sp,
        color = Gray10
    )
}