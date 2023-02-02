package com.zinc.berrybucket.ui_more

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zinc.berrybucket.ui_more.components.BlockMemberView
import com.zinc.berrybucket.ui_more.components.BlockTitle
import com.zinc.berrybucket.ui_more.models.BlockMemberData

@Composable
fun BlockSettingScreen(onBackPressed: () -> Unit) {

    val members = buildList {
        repeat(20) { i ->
            add(
                BlockMemberData(profileUrl = "123", nickName = "멜꽁이", id = "abc + $i")
            )
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        BlockTitle {
            onBackPressed()
        }
        LazyColumn(contentPadding = PaddingValues(top = 16.dp)) {
            items(items = members, key = { member ->
                member.id
            }, itemContent = { member ->
                BlockMemberView(member)
            })
        }
    }
}