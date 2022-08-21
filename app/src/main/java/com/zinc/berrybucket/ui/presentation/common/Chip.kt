package com.zinc.berrybucket.ui.presentation.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zinc.berrybucket.ui.compose.theme.Gray4
import com.zinc.berrybucket.ui.compose.theme.Gray6
import com.zinc.berrybucket.ui.compose.theme.Main4
import com.zinc.berrybucket.util.dpToSp

@Composable
fun RoundChip(
    modifier: Modifier = Modifier,
    chipRadius: Dp = 16.dp,
    textModifier: Modifier = Modifier,
    selectedColor: Color = Main4,
    unSelectedColor: Color = Gray6,
    text: String,
    isSelected: Boolean
) {
    Card(
        border = BorderStroke(color = if (isSelected) Main4 else Gray4, width = 1.dp),
        shape = RoundedCornerShape(chipRadius),
        modifier = modifier.clip(RoundedCornerShape(chipRadius)),
        elevation = 0.dp
    ) {
        Text(
            text = text,
            color = if (isSelected) selectedColor else unSelectedColor,
            fontSize = dpToSp(15.dp),
            modifier = textModifier,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun RoundChipPreview() {
    RoundChip(
        modifier = Modifier.defaultMinSize(minWidth = 90.dp, minHeight = 48.dp),
        textModifier = Modifier.padding(horizontal = 8.dp, vertical = 14.dp),
        text = "칩테스트",
        isSelected = false
    )
}