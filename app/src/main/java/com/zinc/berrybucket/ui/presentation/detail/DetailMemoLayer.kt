package com.zinc.berrybucket.ui.presentation.detail

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.zinc.berrybucket.ui.compose.theme.Gray7

@Composable
fun DetailMemoLayer(modifier: Modifier = Modifier, memo: String) {
    Text(
        text = memo,
        color = Gray7,
        fontSize = 15.sp,
        modifier = modifier
    )
}


