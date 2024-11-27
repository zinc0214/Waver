package com.zinc.waver.ui.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zinc.waver.ui.design.theme.Gray10
import com.zinc.waver.ui.design.theme.Gray4
import com.zinc.waver.ui.design.theme.Gray6
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.ui_common.R


@Composable
fun SearchEditView(
    goToSearch: (String) -> Unit,
    currentSearchWord: String
) {
    val hintText = stringResource(id = R.string.searchHint)
    val keyboardController = LocalSoftwareKeyboardController.current
    var searchText by remember { mutableStateOf(currentSearchWord) }

    LaunchedEffect(key1 = currentSearchWord) {
        if (currentSearchWord != searchText) {
            searchText = currentSearchWord
        }
    }

    Column(modifier = Modifier.padding(24.dp)) {
        Row {
            MyTextField(
                value = searchText,
                textStyle = TextStyle(
                    color = Gray10, fontSize = dpToSp(22.dp), fontWeight = FontWeight.Medium
                ),
                onValueChange = {
                    searchText = it
                },
                maxLines = 1,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    goToSearch(searchText)
                    keyboardController?.hide()
                }),
                decorationBox = { innerTextField ->
                    Row {
                        if (searchText.isEmpty()) {
                            MyText(text = hintText, color = Gray6, fontSize = dpToSp(22.dp))
                        }
                        innerTextField()  //<-- Add this
                    }
                },
                modifier = Modifier.weight(1f)
            )

            if (searchText.isNotEmpty()) {
                IconButton(onClick = {
                    searchText = ""
                    goToSearch("")
                }, modifier = Modifier.size(32.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.btn_32_delete),
                        contentDescription = null
                    )
                }
            }
        }
        Divider(
            color = Gray4, modifier = Modifier
                .fillMaxWidth()
                .padding(top = 13.dp)
        )
    }

}

@Preview(showBackground = true)
@Composable
private fun SearchEditPreview() {
    SearchEditView(
        goToSearch = {}, currentSearchWord = ("z")

    )
}