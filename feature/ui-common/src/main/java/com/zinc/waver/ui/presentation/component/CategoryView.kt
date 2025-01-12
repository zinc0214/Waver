package com.zinc.waver.ui.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zinc.common.models.YesOrNo
import com.zinc.waver.model.WriteCategoryInfo
import com.zinc.waver.ui.design.theme.Main5
import com.zinc.waver.ui.util.dpToSp

@Composable
fun CategoryView(categoryInfo: WriteCategoryInfo) {
    MyText(
        text = categoryInfo.name,
        fontSize = dpToSp(15.dp),
        color = Main5,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun CategoryPreview() {
    CategoryView(categoryInfo = WriteCategoryInfo(id = 0, name = "카테고리", defaultYn = YesOrNo.N))
}