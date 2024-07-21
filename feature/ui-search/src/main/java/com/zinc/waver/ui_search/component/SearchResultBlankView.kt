package com.zinc.waver.ui_search.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zinc.waver.ui.design.theme.Gray3
import com.zinc.waver.ui.design.theme.Gray6
import com.zinc.waver.ui.design.theme.Main1
import com.zinc.waver.ui.design.theme.Main3
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui_search.R

@Composable
fun SearchResultBlankView(modifier: Modifier, searchWord: String) {
    Column(modifier = modifier.fillMaxWidth()) {
        MyText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 100.dp, bottom = 110.dp),
            textAlign = TextAlign.Center,
            color = Gray6,
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Main3)) {
                    append(searchWord)
                }
                append(stringResource(id = R.string.searchWordBlankText, searchWord))
            }
        )

        Divider(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(color = Gray3)
        )
    }
}

@Composable
@Preview
private fun BlankViewPreview() {
    SearchResultBlankView(
        Modifier
            .fillMaxWidth()
            .background(color = Main1), "안녕"
    )
}