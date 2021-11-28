package com.zinc.berrybucket.compose.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zinc.berrybucket.R
import com.zinc.berrybucket.compose.theme.Gray4
import com.zinc.berrybucket.model.MyClickEvent
import com.zinc.berrybucket.model.TabType

@Composable
fun FilterAndSearchImageView(
    modifier: Modifier = Modifier,
    clickEvent: (MyClickEvent) -> Unit,
    tabType: TabType
) {
    Row(
        modifier = modifier.padding(top = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Image(
            painter = painterResource(R.drawable.btn_32_filter),
            contentDescription =
            if (tabType == TabType.ALL)
                stringResource(R.string.allFilterImageViewDesc)
            else stringResource(
                R.string.ddayFilterImageViewDesc
            ),
            modifier = Modifier
                .size(32.dp, 32.dp)
                .padding(0.dp)
                .clickable {
                    clickEvent(MyClickEvent.FilterClicked)
                },
        )
        Divider(
            color = Gray4,
            modifier = Modifier
                .padding(start = 7.5.dp, end = 7.5.dp)
                .height(16.dp)
                .width(1.dp)
                .align(Alignment.CenterVertically)
        )
        Image(
            painter = painterResource(R.drawable.btn_32_search),
            contentDescription =
            if (tabType == TabType.ALL)
                stringResource(R.string.allSearchImageViewDesc)
            else
                stringResource(R.string.ddaySearchImageViewDesc),
            modifier = Modifier
                .size(32.dp, 32.dp)
                .padding(0.dp)
                .clickable {
                    clickEvent(MyClickEvent.SearchClicked)
                },
        )
    }
}