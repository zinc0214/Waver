package com.zinc.waver.ui_search.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zinc.waver.ui.design.theme.Gray6
import com.zinc.waver.ui.design.theme.Main1
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui_search.R

@Composable
fun SearchResultBlankView(modifier: Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.btn_60_search_blank),
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )

        MyText(
            modifier = Modifier.padding(top = 8.dp),
            text = stringResource(R.string.searchWordBlankText),
            color = Gray6
        )

    }
}

@Composable
@Preview
private fun BlankViewPreview() {
    SearchResultBlankView(
        Modifier
            .fillMaxWidth()
            .background(color = Main1)
    )
}