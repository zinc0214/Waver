package com.zinc.berrybucket.compose.ui.my

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zinc.berrybucket.compose.ui.common.BucketListView
import com.zinc.berrybucket.compose.ui.common.FilterAndSearchImageView
import com.zinc.berrybucket.model.ItemClicked
import com.zinc.berrybucket.model.MyClickEvent
import com.zinc.berrybucket.model.TabType
import com.zinc.berrybucket.model.parseToUI
import com.zinc.berrybucket.presentation.my.viewModel.MyViewModel


@Composable
fun DdayBucketLayer(
    viewModel: MyViewModel,
    clickEvent: (MyClickEvent) -> Unit
) {

    viewModel.loadDdayBucketList()
    val dDayBucketList by viewModel.ddayBucketList.observeAsState()

    dDayBucketList?.let {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            DdayFilterAndSearchImageView(clickEvent = clickEvent)
            Spacer(modifier = Modifier.height(16.dp))
            BucketListView(it.bucketList.parseToUI(), TabType.D_DAY,
                itemClicked = {
                    clickEvent.invoke(ItemClicked(it))
                })
        }
    }
}

@Composable
private fun DdayFilterAndSearchImageView(
    modifier: Modifier = Modifier,
    clickEvent: (MyClickEvent) -> Unit
) {
    FilterAndSearchImageView(
        modifier = modifier
            .fillMaxWidth(),
        clickEvent = { clickEvent.invoke(it) },
        tabType = TabType.ALL
    )
}