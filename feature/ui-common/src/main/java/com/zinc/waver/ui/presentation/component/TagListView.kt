package com.zinc.waver.ui.presentation.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zinc.waver.ui.design.theme.Main3
import com.zinc.waver.ui.util.dpToSp

@Composable
fun TagListView(modifier: Modifier = Modifier, tagList: List<String>) {
    Row(modifier = modifier) {
        tagList.forEach { tag ->
            TagView(modifier = Modifier.padding(end = 5.dp), tag)
        }
    }
}

@Composable
private fun TagView(modifier: Modifier = Modifier.width(52.dp), tag: String) {
    MyText(
        text = "# $tag",
        color = Main3,
        fontSize = dpToSp(13.dp),
        modifier = modifier,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        fontWeight = FontWeight.Medium
    )
}

@Preview
@Composable
private fun TagPreview() {
    TagView(tag = "우오아아앙dkdkdkdkdk")
}