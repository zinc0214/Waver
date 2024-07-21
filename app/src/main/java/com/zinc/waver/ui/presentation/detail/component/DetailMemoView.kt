package com.zinc.waver.ui.presentation.detail.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zinc.waver.ui.design.theme.Gray7
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.util.dpToSp

@Composable
fun DetailMemoView(modifier: Modifier = Modifier, memo: String) {
    MyText(
        text = memo,
        color = Gray7,
        fontSize = dpToSp(15.dp),
        modifier = modifier
    )
}


