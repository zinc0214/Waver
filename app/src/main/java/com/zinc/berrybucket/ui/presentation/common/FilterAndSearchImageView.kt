package com.zinc.berrybucket.ui.presentation.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zinc.berrybucket.R
import com.zinc.berrybucket.model.MyPagerClickEvent
import com.zinc.berrybucket.model.MyTabType
import com.zinc.berrybucket.ui.design.theme.Gray4

@Composable
fun FilterAndSearchImageView(
    modifier: Modifier = Modifier, clickEvent: (MyPagerClickEvent) -> Unit, tabType: MyTabType
) {
    Row(
        modifier = modifier.padding(top = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(
            onClick = {
                clickEvent(MyPagerClickEvent.FilterClicked)
            },
            modifier = Modifier.size(32.dp),
            image = R.drawable.btn_32_filter,
            contentDescription = if (tabType == MyTabType.ALL) stringResource(R.string.allFilterImageViewDesc)
            else stringResource(
                R.string.ddayFilterImageViewDesc
            )
        )

        Divider(
            color = Gray4,
            modifier = Modifier
                .padding(start = 7.5.dp, end = 7.5.dp)
                .height(16.dp)
                .width(1.dp)
                .align(Alignment.CenterVertically)
        )

        IconButton(
            onClick = {
                clickEvent(MyPagerClickEvent.SearchClicked(tabType = tabType))
            },
            modifier = Modifier.size(32.dp),
            image = R.drawable.btn_32_search,
            contentDescription = if (tabType == MyTabType.ALL) stringResource(R.string.allSearchImageViewDesc)
            else stringResource(R.string.ddaySearchImageViewDesc)
        )

    }
}

@Preview
@Composable
private fun FilterAndSearchImagePreview() {
    FilterAndSearchImageView(clickEvent = {}, tabType = MyTabType.ALL)
}