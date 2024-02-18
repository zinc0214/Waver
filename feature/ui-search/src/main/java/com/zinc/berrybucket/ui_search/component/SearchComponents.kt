package com.zinc.berrybucket.ui_search.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.zinc.berrybucket.ui.design.theme.Gray1
import com.zinc.berrybucket.ui.design.theme.Gray10
import com.zinc.berrybucket.ui.design.theme.Gray2
import com.zinc.berrybucket.ui.design.theme.Gray3
import com.zinc.berrybucket.ui.design.theme.Gray6
import com.zinc.berrybucket.ui.design.theme.Gray7
import com.zinc.berrybucket.ui.design.theme.Gray9
import com.zinc.berrybucket.ui.design.theme.Main4
import com.zinc.berrybucket.ui.presentation.component.IconButton
import com.zinc.berrybucket.ui.presentation.component.MyText
import com.zinc.berrybucket.ui.util.dpToSp
import com.zinc.berrybucket.ui_search.R
import com.zinc.berrybucket.ui_search.model.KeyWordItem
import com.zinc.berrybucket.ui_search.model.SearchClickEvent
import com.zinc.berrybucket.ui_search.model.SearchRecommendItems
import com.zinc.berrybucket.ui_search.model.SearchRecommendType
import com.zinc.berrybucket.ui_search.model.SearchResultItems
import com.zinc.berrybucket.ui_search.model.UserItem

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

        IconButton(image = com.zinc.berrybucket.ui_common.R.drawable.btn_40_close,
            contentDescription = stringResource(id = com.zinc.berrybucket.ui_common.R.string.closeDesc),
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
            MyText(
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

@Composable
fun RecommendKeyWordView(
    searchItems: SearchRecommendItems,
    itemClicked: (String) -> Unit,
    recentItemDelete: (String) -> Unit
) {
    var selectType by remember { mutableStateOf(SearchRecommendType.RECENT) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
    ) {
        Row(modifier = Modifier.padding(horizontal = 24.dp)) {
            MyText(text = stringResource(id = R.string.recentSearch),
                fontSize = dpToSp(15.dp),
                fontWeight = FontWeight.Bold,
                color = if (selectType == SearchRecommendType.RECENT) Main4 else Gray6,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .clickable {
                        selectType = SearchRecommendType.RECENT
                    })
            MyText(text = stringResource(id = R.string.recommendSearch),
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
    recentItems: List<String>,
    itemClicked: (String) -> Unit,
    recentItemDelete: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        recentItems.forEach {
            RecentSearchItem(it, itemClicked, recentItemDelete)
        }
    }
}

@Composable
private fun RecentSearchItem(
    recentItem: String, itemClicked: (String) -> Unit, recentItemDelete: (String) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp)
    ) {
        val (textView, deleteView) = createRefs()

        MyText(
            text = recentItem,
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
                    itemClicked(recentItem)
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
                contentDescription = stringResource(id = com.zinc.berrybucket.ui_common.R.string.delete),
                alignment = Alignment.CenterEnd
            )
        }
    }
}

@Composable
private fun RecommendKeyWordView(keyWordItems: List<KeyWordItem>, itemClicked: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        keyWordItems.forEach {
            RecommendKeyWordItem(it, itemClicked)
        }
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

        MyText(text = "#${item.keyword}",
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
        MyText(text = "${item.count}개",
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
    resultItems: SearchResultItems,
    modifier: Modifier,
    clickEvent: (SearchClickEvent) -> Unit
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
            RecommendBucketItemView(item = it,
                bucketClicked = { id ->
                    clickEvent.invoke(SearchClickEvent.GoToOpenBucket(id))
                })
        }

        if (needBucketMoreButtonShow) {
            ShowMoreButton {
                bucketVisibleItem = resultItems.bucketItems
                needBucketMoreButtonShow = false
            }
        }

        if (resultItems.bucketItems.isNotEmpty() && resultItems.userItems.isNotEmpty()) {
            Divider(
                color = Gray3, modifier = Modifier.padding(
                    top = if (needBucketMoreButtonShow) 0.dp else 28.dp, bottom = 15.dp
                )
            )
        }

        userVisibleItem.forEach {
            SearchUserItemView(
                item = it,
                userClicked = { id ->
                    clickEvent.invoke(SearchClickEvent.GoToOtherUser(id))
                }

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
        MyText(
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
    modifier: Modifier = Modifier, item: UserItem, userClicked: (String) -> Unit
) {
    Card(
        backgroundColor = Gray1,
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, Gray3),
        elevation = 0.dp,
        modifier = modifier
            .padding(top = 12.dp)
            .clickable {
                userClicked(item.userId)
            }
    ) {

        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (profileImage, nickNameView, followButton) = createRefs()

            Image(
                painter = painterResource(id = com.zinc.berrybucket.ui_common.R.drawable.kakao),
                contentDescription = stringResource(
                    id = R.string.searchProfileImage
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

            MyText(
                text = item.nickName,
                fontSize = dpToSp(14.dp),
                color = Gray10,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                textAlign = TextAlign.Start,
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
                contentDescription = stringResource(id = com.zinc.berrybucket.ui_common.R.string.copy),
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
