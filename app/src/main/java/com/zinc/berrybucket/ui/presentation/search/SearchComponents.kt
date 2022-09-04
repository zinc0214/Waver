package com.zinc.berrybucket.ui.presentation.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.zinc.berrybucket.R
import com.zinc.berrybucket.model.SearchRecommendType
import com.zinc.berrybucket.ui.design.theme.*
import com.zinc.berrybucket.ui.presentation.common.IconButton
import com.zinc.berrybucket.ui.util.dpToSp
import com.zinc.common.models.*


@Composable
fun SearchTopAppBar(
    listState: LazyListState, title: String, closeClicked: () -> Unit, modifier: Modifier = Modifier
) {
    val isTitleScrolled = listState.firstVisibleItemIndex != 0

    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        val (closeButton, titleView) = createRefs()

        IconButton(image = R.drawable.btn_40_close,
            contentDescription = stringResource(id = R.string.closeDesc),
            modifier = Modifier
                .padding(start = 14.dp, top = 6.dp, bottom = 6.dp)
                .size(40.dp)
                .constrainAs(closeButton) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
            onClick = { closeClicked() })

        if (isTitleScrolled) {
            Text(
                color = Gray10,
                fontSize = dpToSp(16.dp),
                fontWeight = FontWeight.Bold,
                text = title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier
                    .padding(top = 14.dp, bottom = 14.dp)
                    .padding(start = 60.dp, end = 60.dp)
                    .constrainAs(titleView) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                    },
                textAlign = TextAlign.Center
            )
        }
    }
}

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

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        val (searchEdit, deleteButton, divider) = createRefs()
        BasicTextField(
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
                        Text(text = hintText, color = Gray6, fontSize = dpToSp(22.dp))
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
            }, color = Gray4
        )
    }
}

@Composable
fun RecommendKeyWordView(
    searchItems: SearchRecommendItems,
    itemClicked: (String) -> Unit,
    recentItemDelete: (RecentItem) -> Unit
) {
    var selectType by remember { mutableStateOf(SearchRecommendType.RECENT) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
    ) {
        Row(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(text = stringResource(id = R.string.recentSearch),
                fontSize = dpToSp(15.dp),
                fontWeight = FontWeight.Bold,
                color = if (selectType == SearchRecommendType.RECENT) Main4 else Gray6,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .clickable {
                        selectType = SearchRecommendType.RECENT
                    })
            Text(text = stringResource(id = R.string.recommendSearch),
                fontSize = dpToSp(15.dp),
                fontWeight = FontWeight.Bold,
                color = if (selectType == SearchRecommendType.RECOMMEND) Main4 else Gray6,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .clickable {
                        selectType = SearchRecommendType.RECOMMEND
                    })
        }
        Column(
            modifier = Modifier.padding(top = 24.dp)
        ) {
            AnimatedVisibility(
                visible = selectType == SearchRecommendType.RECENT
            ) {
                RecentSearchView(
                    searchItems.recentWords,
                    itemClicked = itemClicked,
                    recentItemDelete = recentItemDelete
                )
            }
            AnimatedVisibility(
                visible = selectType == SearchRecommendType.RECOMMEND
            ) {
                RecommendKeyWordView(
                    searchItems.recommendWords,
                    itemClicked = itemClicked,
                )
            }
        }
    }
}

@Composable
private fun RecentSearchView(
    recentItems: List<RecentItem>,
    itemClicked: (String) -> Unit,
    recentItemDelete: (RecentItem) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        recentItems.forEach {
            RecentSearchItem(it, itemClicked, recentItemDelete)
        }
    }
}

@Composable
private fun RecentSearchItem(
    recentItem: RecentItem, itemClicked: (String) -> Unit, recentItemDelete: (RecentItem) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp)
    ) {
        val (textView, deleteView) = createRefs()

        Text(
            text = recentItem.word,
            fontSize = dpToSp(15.dp),
            color = Gray9,
            modifier = Modifier
                .constrainAs(textView) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(deleteView.start)
                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                }
                .clickable {
                    itemClicked(recentItem.word)
                }
                .padding(start = 24.dp)
                .wrapContentHeight(align = Alignment.CenterVertically),
            textAlign = TextAlign.Start)

        Box(
            modifier = Modifier
                .constrainAs(deleteView) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                    height = Dimension.fillToConstraints
                }
                .clickable {
                    recentItemDelete(recentItem)
                },
            contentAlignment = Alignment.CenterStart
        ) {
            Image(
                painter = painterResource(id = R.drawable.btn_24_close),
                modifier = Modifier
                    .padding(start = 18.dp, end = 24.dp)
                    .sizeIn(24.dp),
                contentDescription = stringResource(id = R.string.delete),
                alignment = Alignment.CenterEnd
            )
        }
    }
}

@Composable
private fun RecommendKeyWordView(keyWordItems: List<KeyWordItem>, itemClicked: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            keyWordItems.forEach {
                RecommendKeyWordItem(it, itemClicked)
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
private fun RecommendKeyWordItem(item: KeyWordItem, itemClicked: (String) -> Unit) {
    ConstraintLayout(modifier = Modifier
        .heightIn(min = 40.dp)
        .fillMaxWidth()
        .clickable { itemClicked(item.keyword) }
        .padding(horizontal = 24.dp)) {
        val (textView, countView) = createRefs()

        Text(text = "#${item.keyword}",
            fontSize = dpToSp(15.dp),
            color = Gray9,
            modifier = Modifier
                .padding(end = 24.dp)
                .constrainAs(textView) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(countView.start)
                    width = Dimension.fillToConstraints
                })
        Text(text = "${item.count}개",
            fontSize = dpToSp(15.dp),
            color = Gray6,
            modifier = Modifier.constrainAs(countView) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
            })
    }
}

@Composable
fun SearchResultView(
    resultItems: SearchResultItems, modifier: Modifier
) {
    var needBucketMoreButtonShow by remember {
        mutableStateOf(resultItems.bucketItems.size > 3)
    }
    var needUserMoreButtonShow by remember {
        mutableStateOf(resultItems.userItems.size > 3)
    }
    var bucketVisibleItem by remember {
        mutableStateOf(if (needBucketMoreButtonShow) resultItems.bucketItems.take(3) else resultItems.bucketItems)
    }
    var userVisibleItem by remember {
        mutableStateOf(if (needUserMoreButtonShow) resultItems.userItems.take(3) else resultItems.userItems)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp)
    ) {

        bucketVisibleItem.forEach {
            RecommendBucketItemView(item = it)
        }

        if (needBucketMoreButtonShow) {
            ShowMoreButton {
                bucketVisibleItem = resultItems.bucketItems
                needBucketMoreButtonShow = false
            }
        }

        Divider(
            color = Gray3, modifier = Modifier.padding(
                top = if (needBucketMoreButtonShow) 0.dp else 28.dp, bottom = 15.dp
            )
        )

        userVisibleItem.forEach {
            SearchUserItemView(
                item = it
            )
        }

        if (needUserMoreButtonShow) {
            ShowMoreButton {
                userVisibleItem = resultItems.userItems
                needUserMoreButtonShow = false
            }
        }
    }
}

@Composable
private fun ShowMoreButton(buttonClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth()
            .background(color = Gray2, shape = RoundedCornerShape(2.dp))
            .clip(shape = RoundedCornerShape(2.dp))
            .clickable {
                buttonClicked()
            }, contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.showMore),
            color = Gray7,
            fontSize = dpToSp(14.dp),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
fun SearchUserItemView(
    modifier: Modifier = Modifier, item: UserItem
) {
    Card(
        backgroundColor = Gray1,
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, Gray3),
        elevation = 0.dp,
        modifier = modifier.padding(top = 12.dp)
    ) {

        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (profileImage, nickNameView, followButton) = createRefs()

            if (item.profileImageUrl != null) {
                Image(painter = painterResource(id = R.drawable.kakao),
                    contentDescription = stringResource(
                        id = R.string.feedProfileImage
                    ),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .size(32.dp)
                        .aspectRatio(1f)
                        .clip(shape = RoundedCornerShape(12.dp))
                        .constrainAs(profileImage) {
                            linkTo(
                                top = parent.top,
                                bottom = parent.bottom,
                                topMargin = 16.dp,
                                bottomMargin = 16.dp
                            )
                            start.linkTo(parent.start)
                        })
            }


            Text(
                text = item.nickName,
                fontSize = dpToSp(14.dp),
                color = Gray10,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                modifier = Modifier.constrainAs(nickNameView) {
                    linkTo(
                        top = parent.top,
                        bottom = parent.bottom,
                        topMargin = 22.dp,
                        bottomMargin = 22.dp
                    )
                    linkTo(
                        start = profileImage.end,
                        end = followButton.start,
                        startMargin = 12.dp,
                        endMargin = 12.dp
                    )
                    width = Dimension.fillToConstraints
                })

            IconButton(onClick = {
                // can copied if is unCopied
            },
                image = if (item.isFollowed) R.drawable.btn_32_following else R.drawable.btn_32_not_follow,
                contentDescription = stringResource(id = R.string.copy),
                modifier = Modifier
                    .constrainAs(followButton) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
                    .padding(end = 12.dp)
                    .size(32.dp))
        }

    }
}
