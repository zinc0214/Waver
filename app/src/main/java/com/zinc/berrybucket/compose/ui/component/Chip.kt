package com.zinc.berrybucket.compose.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zinc.berrybucket.compose.theme.*

@Composable
fun RoundChip(modifier: Modifier = Modifier, text: String, isSelected: Boolean) {
    Card(
        modifier = modifier,
        border = BorderStroke(color = if (isSelected) Main4 else Gray4, width = 1.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = 0.dp
    ) {
        Text(
            text = text,
            color = if (isSelected) Main4 else Gray6,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
        )
    }
}
