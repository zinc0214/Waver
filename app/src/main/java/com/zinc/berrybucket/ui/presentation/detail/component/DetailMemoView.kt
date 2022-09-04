package com.zinc.berrybucket.ui.presentation.detail.component

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zinc.berrybucket.ui.design.theme.Gray7
import com.zinc.berrybucket.util.dpToSp

@Composable
fun DetailMemoView(modifier: Modifier = Modifier, memo: String) {
    Text(
        text = memo,
        color = Gray7,
        fontSize = dpToSp(15.dp),
        modifier = modifier
    )
}


