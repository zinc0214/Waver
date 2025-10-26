package com.zinc.waver.ui.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
    Box(
        modifier = modifier
            .background(color = Color.White, RoundedCornerShape(chipRadius))
            .border(
                color = if (isSelected) selectedBorderColor else unSelectedBorderColor,
                width = 1.dp,
                shape = RoundedCornerShape(chipRadius)
            )
            .clip(RoundedCornerShape(chipRadius)),
        contentAlignment = Alignment.Center
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

@Preview
@Composable
private fun RoundChipPreview2() {
    RoundChip(
        modifier = Modifier
            .widthIn(min = 80.dp)
            .heightIn(min = 30.dp)
            .clip(RoundedCornerShape(15.dp)),
        text = "칩테스트",
        isSelected = true,
        fontWeight = FontWeight.Bold
    )
}