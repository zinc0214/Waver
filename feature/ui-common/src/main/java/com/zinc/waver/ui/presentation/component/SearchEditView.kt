package com.zinc.waver.ui.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.zinc.waver.ui.design.theme.Gray10
import com.zinc.waver.ui.design.theme.Gray4
import com.zinc.waver.ui.design.theme.Gray6
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.ui_common.R


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchEditView(
    onImeAction: (String) -> Unit,
    searchTextChange: (String) -> Unit,
    currentSearchWord: MutableState<String>
) {
    val hintText = stringResource(id = R.string.searchHint)
    val keyboardController = LocalSoftwareKeyboardController.current
    var searchText by remember { mutableStateOf(currentSearchWord.value) }

    LaunchedEffect(key1 = currentSearchWord.value) {
        if (currentSearchWord.value != searchText) {
            searchText = currentSearchWord.value
        }
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        val (searchEdit, deleteButton, divider) = createRefs()
        MyTextField(
            value = searchText,
            textStyle = TextStyle(
                color = Gray10, fontSize = dpToSp(22.dp), fontWeight = FontWeight.Medium
            ),
            onValueChange = {
                searchTextChange(it)
                searchText = it
            },
            maxLines = 1,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                onImeAction(searchText)
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
            modifier = Modifier.constrainAs(searchEdit) {
                linkTo(
                    top = parent.top,
                    bottom = parent.bottom,
                    topMargin = 14.dp,
                    bottomMargin = 14.dp
                )
                linkTo(
                    start = parent.start,
                    end = deleteButton.start,
                    endMargin = 12.dp,
                )
                width = Dimension.fillToConstraints
            })

        if (searchText.isNotEmpty()) {
            Image(painter = painterResource(id = R.drawable.btn_32_delete),
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .constrainAs(deleteButton) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                        linkTo(
                            endMargin = 4.dp, end = parent.end, start = parent.start, bias = 1f
                        )
                    }
                    .clickable {
                        searchText = ""
                        searchTextChange.invoke("")
                    })
        }

        Divider(
            modifier = Modifier.constrainAs(divider) {
                start.linkTo(parent.start)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
            }, color = Gray4
        )
    }
}