package com.zinc.berrybucket.ui.presentation.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zinc.berrybucket.R
import com.zinc.berrybucket.ui.compose.theme.Gray4
import com.zinc.berrybucket.ui.compose.theme.Gray9
import com.zinc.berrybucket.util.dpToSp

@Composable
fun EditButtonAndSearchImageView(
    modifier: Modifier = Modifier,
    editClicked: () -> Unit,
    searchClicked: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .clickable {
                    editClicked()
                },
            text = stringResource(id = R.string.categoryEdit),
            color = Gray9,
            fontSize = dpToSp(16.dp),
        )
        Divider(
            color = Gray4,
            modifier = Modifier
                .padding(start = 13.5.dp, end = 7.5.dp)
                .height(16.dp)
                .width(1.dp)
                .align(Alignment.CenterVertically)
        )
        IconButton(
            onClick = {
                searchClicked()
            },
            modifier = Modifier.size(32.dp),
            image = R.drawable.btn_32_search,
            contentDescription = stringResource(R.string.categorySearchImageViewDesc)
        )
    }
}