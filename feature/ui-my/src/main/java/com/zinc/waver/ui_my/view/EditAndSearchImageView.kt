package com.zinc.waver.ui_my.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zinc.waver.ui.design.theme.Gray4
import com.zinc.waver.ui.design.theme.Gray9
import com.zinc.waver.ui.presentation.component.IconButton
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.ui_common.R

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
        MyText(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .clickable {
                    editClicked()
                },
            text = stringResource(id = R.string.edit2),
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
            contentDescription = stringResource(com.zinc.waver.ui_my.R.string.categorySearchImageViewDesc)
        )
    }
}

@Preview
@Composable
private fun EditButtonAndSearchImagePreview() {
    EditButtonAndSearchImageView(editClicked = {}, searchClicked = {})
}