package com.zinc.berrybucket.ui.presentation.my

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zinc.berrybucket.R
import com.zinc.berrybucket.model.MySearchClickEvent
import com.zinc.berrybucket.model.MyTabType
import com.zinc.berrybucket.model.parseToUI
import com.zinc.berrybucket.ui.design.theme.*
import com.zinc.berrybucket.ui.presentation.common.CategoryListView
import com.zinc.berrybucket.ui.presentation.common.RoundChip
import com.zinc.berrybucket.ui.presentation.common.SimpleBucketListView
import com.zinc.berrybucket.ui.presentation.my.viewModel.MyViewModel
import com.zinc.berrybucket.util.dpToSp
import com.zinc.common.models.BucketInfoSimple
import com.zinc.common.models.Category

@Composable
fun MySearchBottomScreen(
    currentTabType: MyTabType,
    clickEvent: (MySearchClickEvent) -> Unit,
) {

    val viewModel: MyViewModel = hiltViewModel()
    val selectTab = remember { mutableStateOf(currentTabType) }
    val searchedTab = remember { mutableStateOf(currentTabType) }
    val searchResult = viewModel.searchResult.observeAsState()


    Column {
        TopAppBar(clickEvent = clickEvent)

        SearchEditView(
            type = selectTab.value,
            onImeAction = { word ->
                if (word.isNotEmpty()) {
                    viewModel.searchList(selectTab.value, word)
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
            list = MyTabType.values().toList(),
            currentTab = selectTab
        )

        Box(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(top = 20.dp)
                .background(Gray2)
                .fillMaxHeight()
        ) {
            searchResult.value?.let {
                if (searchedTab.value == selectTab.value) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxHeight()
                    ) {
                        SearchResultView(it, clickEvent = { event ->
                            clickEvent.invoke(event)
                        })
                    }
                }
            }
        }
    }
}

@Composable
private fun TopAppBar(clickEvent: (MySearchClickEvent) -> Unit) {
    TopAppBar(navigationIcon = {
        Icon(painter = painterResource(id = R.drawable.btn_40_close),
            contentDescription = null,
            modifier = Modifier
                .padding(start = 14.dp, top = 8.dp)
                .clickable {
                    clickEvent(MySearchClickEvent.CloseClicked)
                })
    }, title = {
        Text(text = "")
    }, backgroundColor = Gray1, elevation = 0.dp
    )
}

@Composable
private fun SearchEditView(
    type: MyTabType, onImeAction: (String) -> Unit
) {

    Row(
        modifier = Modifier.padding(start = 28.dp, end = 28.dp, top = 24.dp)
    ) {
        Text(
            text = stringResource(type.title),
            color = Main4,
            fontSize = dpToSp(20.dp),
            fontWeight = FontWeight.Medium
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
    val hintText = stringResource(id = R.string.myBucketSearchHint)
    val keyboardController = LocalSoftwareKeyboardController.current
    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    BasicTextField(
        value = searchText,
        textStyle = TextStyle(
            color = Gray10, fontSize = dpToSp(20.dp), fontWeight = FontWeight.Medium
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
                    Text(text = hintText, color = Gray6, fontSize = dpToSp(20.dp))
                }
                innerTextField()  //<-- Add this
            }
        },
    )
}


@Composable
fun ChipBodyContent(
    modifier: Modifier = Modifier, list: List<MyTabType>, currentTab: MutableState<MyTabType>
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState())
    ) {
        for (tabType in list) {
            RoundChip(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .selectable(selected = (currentTab.value == tabType), onClick = {
                        currentTab.value = tabType
                    }),
                textModifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                text = stringResource(id = tabType.title),
                isSelected = currentTab.value == tabType
            )
        }
    }
}


@Composable
private fun SearchResultView(
    result: Pair<MyTabType, List<*>>, clickEvent: (MySearchClickEvent) -> Unit
) {
    if (result.first == MyTabType.CATEGORY) {
        if (result.second.all { item -> item is Category }) {
            val items = result.second as List<Category>
            CategoryListView(items)
        }
    } else {
        if (result.second.all { item -> item is BucketInfoSimple }) {
            val items = result.second as List<BucketInfoSimple>
            SimpleBucketListView(items.parseToUI(), result.first, itemClicked = {
                clickEvent.invoke(MySearchClickEvent.ItemClicked(it))
            })
        }
    }
}