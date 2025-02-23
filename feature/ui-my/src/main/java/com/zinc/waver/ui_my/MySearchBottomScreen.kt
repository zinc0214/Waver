package com.zinc.waver.ui_my

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zinc.common.models.BucketInfoSimple
import com.zinc.waver.model.MySearchClickEvent
import com.zinc.waver.model.MyTabType
import com.zinc.waver.model.MyTabType.CATEGORY
import com.zinc.waver.model.UICategoryInfo
import com.zinc.waver.model.parseToUI
import com.zinc.waver.ui.design.theme.Gray1
import com.zinc.waver.ui.design.theme.Gray10
import com.zinc.waver.ui.design.theme.Gray2
import com.zinc.waver.ui.design.theme.Gray4
import com.zinc.waver.ui.design.theme.Gray6
import com.zinc.waver.ui.design.theme.Main4
import com.zinc.waver.ui.presentation.component.CategoryListView
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.presentation.component.MyTextField
import com.zinc.waver.ui.presentation.component.RoundChip
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.ui_my.viewModel.MyViewModel

@Composable
fun MySearchBottomScreen(
    currentTabType: MyTabType,
    clickEvent: (MySearchClickEvent) -> Unit,
) {
    val viewModel: MyViewModel = hiltViewModel()
    val searchResultAsState by viewModel.searchResult.observeAsState()
    val prevSearchInfoAsState by viewModel.prevSearchedResult.observeAsState()

    val selectTab = remember { mutableStateOf(currentTabType) }
    val searchedTab = remember { mutableStateOf(currentTabType) }
    val searchResult = remember { mutableStateOf(searchResultAsState) }
    val prevSearchResult = remember { mutableStateOf(prevSearchInfoAsState) }

    LaunchedEffect(key1 = searchResultAsState, block = {
        if (searchResultAsState != null) {
            searchResult.value = searchResultAsState
        }
    })

    LaunchedEffect(key1 = prevSearchInfoAsState) {
        // 이전 데이터가 있는지 확인
        if (prevSearchInfoAsState != null) {
            prevSearchResult.value = prevSearchInfoAsState

            // 이전에 지정된 탭
            val type = prevSearchResult.value?.first
            if (type != null) {
                selectTab.value = type
            }

            // 이전에 지정된 단어
            val word = prevSearchResult.value?.second
            if (word?.isNotEmpty() == true && selectTab.value != searchedTab.value && type != null) {
                viewModel.searchList(type, word)
                searchedTab.value = type
            }
        }
    }

    Column(modifier = Modifier.statusBarsPadding()) {
        TopAppBar(clickEvent = clickEvent)

        SearchEditView(
            type = selectTab.value,
            prevText = prevSearchResult.value?.second.orEmpty(),
            onImeAction = { word ->
                if (word.isNotEmpty()) {
                    viewModel.searchList(selectTab.value, word)
                    searchedTab.value = selectTab.value
                }
            }
        )

        Box(modifier = Modifier.padding(top = 15.dp, bottom = 21.dp, start = 24.dp, end = 24.dp)) {
            Divider(
                modifier = Modifier
                    .height(1.dp)
                    .background(color = Gray4)
                    .fillMaxWidth()
            )
        }

        ChipBodyContent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            list = MyTabType.values().toList(),
            currentTab = selectTab,
            tabChanged = {
                if (searchedTab.value != selectTab.value) {
                    viewModel.searchList(selectTab.value, prevSearchResult.value?.second.orEmpty())
                    searchedTab.value = selectTab.value
                }
            }
        )

        Box(
            modifier = Modifier
                .padding(top = 20.dp)
                .background(Gray2)
                .fillMaxHeight()
        ) {
            searchResult.value.let {
                if (searchedTab.value == selectTab.value) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxHeight()
                    ) {
                        it?.let { result ->
                            SearchResultView(result, clickEvent = { event ->
                                clickEvent.invoke(event)
                            }, bucketAchieve = {
                                viewModel.achieveBucket(it, currentTabType)
                            })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TopAppBar(clickEvent: (MySearchClickEvent) -> Unit) {
    androidx.compose.material.TopAppBar(navigationIcon = {
        Icon(
            painter = painterResource(id = com.zinc.waver.ui_common.R.drawable.btn_40_close),
            contentDescription = null,
            modifier = Modifier
                .padding(start = 14.dp, top = 8.dp)
                .clickable {
                    clickEvent(MySearchClickEvent.CloseClicked)
                })
    }, title = {
        MyText(text = "")
    }, backgroundColor = Gray1, elevation = 0.dp
    )
}

@Composable
private fun SearchEditView(
    type: MyTabType, prevText: String, onImeAction: (String) -> Unit
) {

    Row(
        modifier = Modifier.padding(start = 28.dp, end = 28.dp, top = 24.dp)
    ) {
        MyText(
            text = stringResource(type.getTitle()),
            color = Main4,
            fontSize = dpToSp(20.dp),
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.width(12.dp))
        SearchEditView(prevText, onImeAction)
    }
}

@Composable
private fun SearchEditView(
    prevText: String,
    onImeAction: (String) -> Unit
) {
    val hintText = stringResource(id = R.string.myBucketSearchHint)
    val keyboardController = LocalSoftwareKeyboardController.current
    var searchText by remember { mutableStateOf(TextFieldValue(prevText)) }

    MyTextField(
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
                    MyText(text = hintText, color = Gray6, fontSize = dpToSp(20.dp))
                }
                innerTextField()  //<-- Add this
            }
        },
    )
}


@Composable
private fun ChipBodyContent(
    modifier: Modifier = Modifier,
    list: List<MyTabType>,
    currentTab: MutableState<MyTabType>,
    tabChanged: () -> Unit
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        for (tabType in list) {
            RoundChip(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .selectable(
                        selected = (currentTab.value == tabType),
                        onClick = {
                            currentTab.value = tabType
                            tabChanged()
                        }),
                textModifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                text = stringResource(id = tabType.getTitle()),
                isSelected = currentTab.value == tabType,
                fontSize = 15.dp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
private fun SearchResultView(
    result: Pair<MyTabType, List<*>>,
    clickEvent: (MySearchClickEvent) -> Unit,
    bucketAchieve: (String) -> Unit
) {
    if (result.first is CATEGORY) {
        if (result.second.all { item -> item is UICategoryInfo }) {
            val items = result.second as List<UICategoryInfo>
            CategoryListView(items) {
                clickEvent.invoke(MySearchClickEvent.CategoryItemClicked(it.toString()))
            }
        }
    } else {
        if (result.second.all { item -> item is BucketInfoSimple }) {
            val items = result.second as List<BucketInfoSimple>
            SimpleBucketListView(
                bucketList = items.parseToUI().toMutableStateList(),
                tabType = result.first,
                showDday = true,
                itemClicked = {
                    clickEvent.invoke(
                        MySearchClickEvent.BucketItemClicked(
                            it.id,
                            it.isPrivate()
                        )
                    )
                },
                achieveClicked = {
                    bucketAchieve(it)
                }
            )
        }
    }
}

@Preview
@Composable
private fun MySearchBottomScreenPreview() {
    MySearchBottomScreen(currentTabType = CATEGORY, clickEvent = {})
}