package com.zinc.berrybucket.compose.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zinc.berrybucket.compose.theme.Gray1


@Composable
fun ScrollableAppBar(modifier: Modifier = Modifier, contents: @Composable BoxScope.() -> Unit) {
    Surface(elevation = 0.5.dp) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(52.dp)
                .background(color = Gray1),
            content = {
                contents()
            }
        )
    }
}