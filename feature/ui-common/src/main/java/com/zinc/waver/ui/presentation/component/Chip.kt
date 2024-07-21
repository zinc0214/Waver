package com.zinc.waver.ui.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zinc.waver.ui.design.theme.Gray4
import com.zinc.waver.ui.design.theme.Gray6
import com.zinc.waver.ui.design.theme.Main4
import com.zinc.waver.ui.util.dpToSp

@Composable
fun RoundChip(
    modifier: Modifier = Modifier,
    chipRadius: Dp = 16.dp,
    textModifier: Modifier = Modifier,
    selectedTextColor: Color = Main4,
    unSelectedTextColor: Color = Gray6,
    selectedBorderColor: Color = Main4,
    unSelectedBorderColor: Color = Gray4,
    text: String,
    fontSize: Dp = 15.dp,
    fontWeight: FontWeight,
    isSelected: Boolean
) {
    Card(
        border = BorderStroke(
            color = if (isSelected) selectedBorderColor else unSelectedBorderColor,
            width = 1.dp
        ),
        shape = RoundedCornerShape(chipRadius),
        modifier = modifier.clip(RoundedCornerShape(chipRadius)),
        elevation = 0.dp
    ) {
        MyText(
            text = text,
            color = if (isSelected) selectedTextColor else unSelectedTextColor,
            fontSize = dpToSp(fontSize),
            modifier = textModifier,
            fontWeight = fontWeight,
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
        isSelected = false,
        fontWeight = FontWeight.Bold
    )
}