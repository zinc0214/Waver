package com.zinc.berrybucket.compose.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zinc.berrybucket.R
import com.zinc.berrybucket.compose.theme.Gray1
import com.zinc.berrybucket.compose.theme.Main2
import com.zinc.berrybucket.compose.theme.Main3

@Composable
fun CategoryAddView() {

    Box(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .dashedBorder(
                width = 1.dp,
                color = Main2,
                shape = MaterialTheme.shapes.medium, on = 4.dp, off = 4.dp
            )
            .background(Gray1)
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = stringResource(id = R.string.categoryAdd),
            fontSize = 14.sp,
            color = Main3,
            modifier = Modifier.padding(vertical = 22.dp, horizontal = 20.dp)
        )
    }
}