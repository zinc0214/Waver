package com.zinc.berrybucket.ui_my

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zinc.berrybucket.model.MySearchClickEvent
import com.zinc.berrybucket.model.MyTabType
import com.zinc.berrybucket.model.MyTabType.CATEGORY
import com.zinc.berrybucket.model.parseToUI
import com.zinc.berrybucket.ui.design.theme.Gray1
import com.zinc.berrybucket.ui.design.theme.Gray10
import com.zinc.berrybucket.ui.design.theme.Gray2
import com.zinc.berrybucket.ui.design.theme.Gray4
import com.zinc.berrybucket.ui.design.theme.Gray6
import com.zinc.berrybucket.ui.design.theme.Main4
import com.zinc.berrybucket.ui.presentation.component.CategoryListView
import com.zinc.berrybucket.ui.presentation.component.MyText
import com.zinc.berrybucket.ui.presentation.component.MyTextField
import com.zinc.berrybucket.ui.presentation.component.RoundChip
import com.zinc.berrybucket.ui.util.dpToSp
import com.zinc.berrybucket.ui_my.viewModel.MyViewModel
import com.zinc.common.models.BucketInfoSimple
import com.zinc.common.models.CategoryInfo

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
    androidx.compose.material.TopAppBar(navigationIcon = {
        Icon(painter = painterResource(id = com.zinc.berrybucket.ui_common.R.drawable.btn_40_close),
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
    type: MyTabType, onImeAction: (String) -> Unit
) {

    Row(
        modifier = Modifier.padding(start = 28.dp, end = 28.dp, top = 24.dp)
    ) {
        MyText(
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
                    .clip(RoundedCornerShape(16.dp))
                    .selectable(
                        selected = (currentTab.value == tabType),
                        onClick = {
                            currentTab.value = tabType
                        }),
                textModifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                text = stringResource(id = tabType.title),
                isSelected = currentTab.value == tabType,
                fontSize = 13.dp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
private fun SearchResultView(
    result: Pair<MyTabType, List<*>>, clickEvent: (MySearchClickEvent) -> Unit
) {
    if (result.first is CATEGORY) {
        if (result.second.all { item -> item is CategoryInfo }) {
            val items = result.second as List<CategoryInfo>
            CategoryListView(items)
        }
    } else {
        if (result.second.all { item -> item is BucketInfoSimple }) {
            val items = result.second as List<BucketInfoSimple>
            SimpleBucketListView(
                bucketList = items.parseToUI(),
                tabType = result.first,
                showDday = true,
                nestedScrollInterop = null,
                itemClicked = {
                    clickEvent.invoke(MySearchClickEvent.ItemClicked(it))
                },
            )
        }
    }
}