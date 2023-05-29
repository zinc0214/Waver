package com.zinc.berrybucket.ui_my.category

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zinc.berrybucket.ui.design.theme.Gray1
import com.zinc.berrybucket.ui.design.theme.Main2
import com.zinc.berrybucket.ui.design.theme.Main3
import com.zinc.berrybucket.ui.presentation.component.MyText
import com.zinc.berrybucket.ui.presentation.component.dashedBorder
import com.zinc.berrybucket.ui.util.dpToSp
import com.zinc.berrybucket.ui_my.R

@Preview
@Composable
fun CategoryAddView() {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .dashedBorder(
                width = 1.dp,
                color = Main2,
                shape = MaterialTheme.shapes.medium,
                on = 4.dp,
                off = 4.dp
            )
            .background(Gray1), contentAlignment = Alignment.Center
    ) {
        MyText(
            textAlign = TextAlign.Center,
            text = stringResource(id = R.string.categoryAdd),
            fontSize = dpToSp(14.dp),
            color = Main3,
            modifier = Modifier.padding(vertical = 22.dp, horizontal = 20.dp)
        )
    }
}