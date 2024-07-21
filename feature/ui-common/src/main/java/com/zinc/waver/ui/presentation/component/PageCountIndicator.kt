package com.zinc.waver.ui.presentation.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zinc.waver.ui.design.theme.Gray1
import com.zinc.waver.ui.design.theme.Gray11
import com.zinc.waver.ui.util.dpToSp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PageCountIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = Gray11.copy(alpha = 0.4f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(vertical = 3.dp, horizontal = 8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        MyText(
            text = "${pagerState.currentPage + 1}/${pagerState.pageCount}",
            color = Gray1,
            fontSize = dpToSp(12.dp),
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
private fun PageCountIndicatorView() {
    PageCountIndicator(pagerState = rememberPagerState {
        10
    })
}