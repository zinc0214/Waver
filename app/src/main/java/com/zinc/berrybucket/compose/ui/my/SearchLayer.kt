package com.zinc.berrybucket.compose.ui.my

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zinc.berrybucket.R
import com.zinc.berrybucket.compose.theme.*
import com.zinc.berrybucket.compose.ui.component.BucketListView
import com.zinc.berrybucket.compose.ui.component.CategoryListView
import com.zinc.berrybucket.compose.ui.component.RoundChip
import com.zinc.berrybucket.model.BucketInfoSimple
import com.zinc.berrybucket.model.MyClickEvent
import com.zinc.berrybucket.model.TabType
import com.zinc.berrybucket.model.TabType.Companion.getNameResource
import com.zinc.data.models.Category

@Composable
fun SearchLayer(
    currentTabType: TabType,
    clickEvent: (MyClickEvent) -> Unit,
    searchWord: (TabType, String) -> Unit,
    result: State<Pair<TabType, List<*>>?>
) {

    val selectTab = remember { mutableStateOf(currentTabType) }
    val searchedTab = remember { mutableStateOf(currentTabType) }

    BaseTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Gray2)
        ) {
            Column {
                TopAppBar(clickEvent = clickEvent)

                SearchEditView(
                    type = selectTab.value,
                    onImeAction = { word ->
                        if (word.isNotEmpty()) {
                            searchWord.invoke(selectTab.value, word)
                            searchedTab.value = selectTab.value
                        }
                    }
                )

                Box(
                    modifier = Modifier
                        .padding(top = 15.dp, bottom = 21.dp, start = 24.dp, end = 24.dp)
                )
                {
                    Divider(
                        modifier = Modifier
                            .height(1.dp)
                            .background(color = Gray4)
                            .fillMaxWidth()
                    )
                }

                ChipBodyContent(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    list = TabType.values().toList(),
                    currentTab = selectTab
                )

                Box(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(top = 20.dp)
                        .background(Gray2)
                        .fillMaxHeight()
                ) {
                    result.value?.let {
                        if (searchedTab.value == selectTab.value) {
                            Column {
                                Spacer(modifier = Modifier.height(20.dp))
                                SearchResultView(it)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TopAppBar(clickEvent: (MyClickEvent) -> Unit) {
    TopAppBar(
        navigationIcon = {
            Icon(
                painter = painterResource(id = R.drawable.btn40close),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 14.dp, top = 8.dp)
                    .clickable {
                        clickEvent(MyClickEvent.CloseClicked)
                    }
            )
        },
        title = {
            Text(text = "")
        },
        backgroundColor = Gray1,
        elevation = 0.dp
    )
}

@Composable
private fun SearchEditView(
    type: TabType,
    onImeAction: (String) -> Unit
) {

    Row(
        modifier = Modifier.padding(start = 28.dp, end = 28.dp, top = 24.dp)
    ) {
        Text(
            text = stringResource(id = getNameResource(type)),
            color = Main4,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.width(12.dp))

        SearchEditView(onImeAction)
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SearchEditView(
    onImeAction: (String) -> Unit
) {
    val hintText = stringResource(id = R.string.hint)
    val keyboardController = LocalSoftwareKeyboardController.current
    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    BasicTextField(
        value = searchText,
        textStyle = TextStyle(
            color = Gray10,
            fontSize = 20.sp
        ),
        onValueChange = { searchText = it },
        maxLines = 1,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            onImeAction(searchText.text)
            keyboardController?.hide()
        }),
        decorationBox = { innerTextField ->
            Row {
                if (searchText.text.isEmpty()) {
                    Text(text = hintText, color = Gray6, fontSize = 20.sp)
                }
                innerTextField()  //<-- Add this
            }
        },
    )
}


@Composable
fun ChipBodyContent(
    modifier: Modifier = Modifier,
    list: List<TabType>,
    currentTab: MutableState<TabType>,

    ) {
    Row(
        modifier = modifier
            .horizontalScroll(rememberScrollState())
    ) {
        for (tabType in list) {
            RoundChip(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .selectable(
                        selected = (currentTab.value == tabType),
                        onClick = {
                            currentTab.value = tabType
                        }
                    ),
                text = stringResource(id = getNameResource(tabType)),
                isSelected = currentTab.value == tabType
            )
        }

    }
}


@Composable
private fun SearchResultView(result: Pair<TabType, List<*>>) {

    if (result.first == TabType.CATEGORY) {
        if (result.second.all { item -> item is Category }) {
            val items = result.second as List<Category>
            CategoryListView(items)
        }
    } else {
        if (result.second.all { item -> item is BucketInfoSimple }) {
            val items = result.second as List<BucketInfoSimple>
            BucketListView(items, result.first)
        }
    }
}