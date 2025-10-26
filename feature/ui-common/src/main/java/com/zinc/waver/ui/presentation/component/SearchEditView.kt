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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
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
    // 상태를 TextFieldValue로 변경하여 커서(Selection) 제어
    var searchTextField by remember {
        mutableStateOf(
            TextFieldValue(
                text = currentSearchWord,
                selection = TextRange(currentSearchWord.length)
            )
        )
    }
    var isFocused by remember { mutableStateOf(false) }
    var wasFocused by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = currentSearchWord) {
        // 외부 currentSearchWord가 바뀌면 내부 상태를 동기화하고 커서를 맨 뒤로 보냄
        if (currentSearchWord != searchTextField.text) {
            searchTextField = TextFieldValue(
                text = currentSearchWord,
                selection = TextRange(currentSearchWord.length)
            )
        }
    }

    Column(modifier = Modifier.padding(24.dp)) {
        Row {
            MyTextField(
                value = searchTextField,
                textStyle = TextStyle(
                    color = Gray10, fontSize = dpToSp(22.dp), fontWeight = FontWeight.Medium
                ),
                onValueChange = { newValue ->
                    searchTextField = newValue
                },
                maxLines = 1,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    goToSearch(searchTextField.text)
                    keyboardController?.hide()
                }),
                decorationBox = { innerTextField ->
                    Row {
                        // 힌트는 텍스트가 비어있고 포커스가 없을 때만 보임
                        if (searchTextField.text.isEmpty() && !isFocused) {
                            MyText(text = hintText, color = Gray6, fontSize = dpToSp(22.dp))
                        }
                        innerTextField()
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged {
                        val focusedNow = it.isFocused
                        // 포커스가 새로 생겼을 때(비포커스 -> 포커스)라면 커서를 맨 뒤로 보낸다
                        if (focusedNow && !wasFocused && searchTextField.text.isNotEmpty()) {
                            searchTextField = searchTextField.copy(
                                selection = TextRange(searchTextField.text.length)
                            )
                        }
                        wasFocused = focusedNow
                        isFocused = focusedNow
                    }
            )

            if (searchTextField.text.isNotEmpty()) {
                IconButton(onClick = {
                    searchTextField = TextFieldValue(text = "")
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
    SearchEditView(goToSearch = {}, currentSearchWord = ("z"))
}