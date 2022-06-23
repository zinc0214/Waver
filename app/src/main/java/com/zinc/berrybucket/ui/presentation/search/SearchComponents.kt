package com.zinc.berrybucket.ui.presentation.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.zinc.berrybucket.R
import com.zinc.berrybucket.model.SearchRecommendType
import com.zinc.berrybucket.ui.compose.theme.*
import com.zinc.berrybucket.ui.presentation.common.IconButton
import com.zinc.common.models.KeyWordItem
import com.zinc.common.models.RecentItem
import com.zinc.common.models.SearchRecommendItems


@Composable
fun SearchTopAppBar(
    listState: LazyListState,
    title: String,
    closeClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isTitleScrolled = listState.firstVisibleItemIndex != 0

    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        val (closeButton, titleView) = createRefs()

        IconButton(
            image = R.drawable.btn40close,
            contentDescription = stringResource(id = R.string.closeDesc),
            modifier = Modifier
                .padding(start = 14.dp, top = 6.dp, bottom = 6.dp)
                .size(40.dp)
                .constrainAs(closeButton) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
            onClick = { closeClicked() }
        )

        if (isTitleScrolled) {
            Text(
                color = Gray10,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                text = title,
                modifier = Modifier
                    .padding(top = 14.dp, bottom = 14.dp)
                    .constrainAs(titleView) {
                        start.linkTo(closeButton.end)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                    }
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchEditView(
    onImeAction: (String) -> Unit,
    searchTextChange: (String) -> Unit
) {
    val hintText = stringResource(id = R.string.searchHint)
    val keyboardController = LocalSoftwareKeyboardController.current
    var searchText by remember { mutableStateOf("") }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        val (searchEdit, deleteButton, divider) = createRefs()
        BasicTextField(
            value = searchText,
            textStyle = TextStyle(
                color = Gray10,
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium
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
                        Text(text = hintText, color = Gray6, fontSize = 22.sp)
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
                    })
        }

        Divider(
            modifier = Modifier.constrainAs(divider) {
                start.linkTo(parent.start)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
            },
            color = Gray4
        )
    }
}

@Composable
fun RecommendKeyWordView(searchItems: SearchRecommendItems) {
    var selectType by remember { mutableStateOf(SearchRecommendType.RECENT) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(top = 20.dp)
    ) {
        Row {
            Text(
                text = stringResource(id = R.string.recentSearch),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = if (selectType == SearchRecommendType.RECENT) Main4 else Gray6,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .clickable {
                        selectType = SearchRecommendType.RECENT
                    }
            )
            Text(
                text = stringResource(id = R.string.recommendSearch),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = if (selectType == SearchRecommendType.RECOMMEND) Main4 else Gray6,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .clickable {
                        selectType = SearchRecommendType.RECOMMEND
                    }
            )
        }
        Column(
            modifier = Modifier.padding(top = 24.dp)
        ) {
            AnimatedVisibility(
                visible = selectType == SearchRecommendType.RECENT
            ) {
                RecentSearchView(searchItems.recentWords)
            }
            AnimatedVisibility(
                visible = selectType == SearchRecommendType.RECOMMEND
            ) {
                RecommendKeyWordView(searchItems.recommendWords)
            }
        }
    }
}

@Composable
private fun RecentSearchView(recentItems: List<RecentItem>) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        items(items = recentItems) { item ->
            RecentSearchItem(item)
        }
    }
}

@Composable
private fun RecentSearchItem(recentItem: RecentItem) {
    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val (textView, deleteView) = createRefs()

        Text(
            text = recentItem.word,
            fontSize = 15.sp,
            color = Gray9,
            modifier = Modifier
                .padding(end = 24.dp)
                .constrainAs(textView) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(deleteView.start)
                    width = Dimension.fillToConstraints
                }
        )
        Image(
            painter = painterResource(id = R.drawable.btn_40_close),
            modifier = Modifier
                .size(24.dp)
                .constrainAs(deleteView) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                },
            contentDescription = stringResource(id = R.string.delete),
            alignment = Alignment.CenterEnd
        )
    }
}

@Composable
private fun RecommendKeyWordView(ketWordItems: List<KeyWordItem>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            items(items = ketWordItems) { item ->
                RecommendKeyWordItem(item)
            }
        }

        KeyWordChangeButton(
            modifier = Modifier
                .padding(top = 40.dp, bottom = 32.dp)
                .align(Alignment.CenterHorizontally)
        )
    }

}

@Composable
private fun RecommendKeyWordItem(item: KeyWordItem) {
    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val (textView, countView) = createRefs()

        Text(
            text = "#${item.keyword}",
            fontSize = 15.sp,
            color = Gray9,
            modifier = Modifier
                .padding(end = 24.dp)
                .constrainAs(textView) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(countView.start)
                    width = Dimension.fillToConstraints
                }
        )
        Text(
            text = "${item.count}개",
            fontSize = 15.sp,
            color = Gray6,
            modifier = Modifier
                .constrainAs(countView) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }
        )
    }
}