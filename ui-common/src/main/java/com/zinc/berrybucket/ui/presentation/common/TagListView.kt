package com.zinc.berrybucket.ui.presentation.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zinc.berrybucket.ui.design.theme.Main3
import com.zinc.berrybucket.ui.util.dpToSp

@Composable
fun TagListView(modifier: Modifier = Modifier, tagList: List<String>) {
    Row(modifier = modifier) {
        tagList.forEach { tag ->
            TagView(modifier = Modifier.padding(end = 5.dp), tag)
        }
    }
}

@Composable
private fun TagView(modifier: Modifier = Modifier, tag: String) {
    Text(
        text = "# $tag",
        color = Main3,
        fontSize = dpToSp(15.dp),
        modifier = modifier
    )
}