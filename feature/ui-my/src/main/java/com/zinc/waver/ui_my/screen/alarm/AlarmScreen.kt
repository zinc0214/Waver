package com.zinc.waver.ui_my.screen.alarm

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zinc.waver.ui_my.viewModel.AlarmViewModel

@Composable
fun AlarmScreen(onBackPressed: () -> Unit) {
    val viewModel: AlarmViewModel = hiltViewModel()
    val alarmList by viewModel.alarmList.observeAsState()

    viewModel.loadAlarmList()

    Column(modifier = Modifier.fillMaxSize()) {
        AlarmTitle {
            onBackPressed()
        }

        alarmList?.let { alarmList ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 22.dp, end = 28.dp),
                contentPadding = PaddingValues(vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                items(items = alarmList.alarmList,
                    key = { item -> item.title + item.type + item.bucketId },
                    itemContent = { item ->
                        AlarmItemView(alarmItem = item)
                    })
            }
        }
    }
}