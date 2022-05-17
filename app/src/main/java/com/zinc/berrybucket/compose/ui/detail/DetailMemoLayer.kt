package com.zinc.berrybucket.compose.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zinc.berrybucket.compose.theme.Gray7

@Composable
fun DetailMemoLayer(modifier: Modifier = Modifier, memo: String) {
    Text(
        text = memo,
        color = Gray7,
        fontSize = 15.sp,
        modifier = modifier
    )
}


