package com.zinc.berrybucket.ui.presentation.component.calendar.year

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.zinc.berrybucket.ui.presentation.component.MyText

@Composable
internal fun YearRow(year: Int, verticalPadding: Dp) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = verticalPadding)
            .background(MaterialTheme.colors.background),
        horizontalArrangement = Arrangement.Center,
    ) {
        MyText(
            text = year.toString(),
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.onBackground
        )
    }
}