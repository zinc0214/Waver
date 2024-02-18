package com.zinc.berrybucket.ui_search.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.zinc.berrybucket.ui.design.theme.Gray1
import com.zinc.berrybucket.ui.design.theme.Gray10
import com.zinc.berrybucket.ui.design.theme.Gray2
import com.zinc.berrybucket.ui.design.theme.Gray3
import com.zinc.berrybucket.ui.design.theme.Gray4
import com.zinc.berrybucket.ui.design.theme.Gray6
import com.zinc.berrybucket.ui.design.theme.Gray9
import com.zinc.berrybucket.ui.presentation.component.IconButton
import com.zinc.berrybucket.ui.presentation.component.MyText
import com.zinc.berrybucket.ui.presentation.component.TagListView
import com.zinc.berrybucket.ui.presentation.component.rememberNestedScrollConnection
import com.zinc.berrybucket.ui.util.dpToSp
import com.zinc.berrybucket.ui_search.R
import com.zinc.berrybucket.ui_search.model.RecommendItem
import com.zinc.berrybucket.ui_search.model.RecommendList
import com.zinc.berrybucket.ui_search.model.RecommendType
import com.zinc.berrybucket.ui_search.model.SearchBucketItem


@Composable
fun RecommendTopBar(
    modifier: Modifier,
    editViewClicked: () -> Unit,
    height: Dp
) {

    Column(modifier = modifier.height(height)) {
        if (height >= 130.dp) {
            SearchTitle()
        }
        SearchTextView {
            editViewClicked.invoke()
        }

        if (height < 130.dp) {
            RecommendDivider()
        }
    }
}

@Composable
fun SearchTitle() {
    MyText(
        text = stringResource(id = R.string.searchTitle),
        fontSize = dpToSp(24.dp),
        color = Gray10,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Gray1)
            .padding(top = 24.dp, bottom = 4.dp, start = 28.dp, end = 17.dp)
    )
}

@Composable
private fun SearchTextView(
    editViewClicked: () -> Unit
) {
    val hintText = stringResource(id = R.string.searchHint)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Gray1)
            .padding(top = 20.dp, bottom = 29.dp, start = 28.dp, end = 28.dp)
            .height(48.dp)
            .background(shape = RoundedCornerShape(8.dp), color = Gray2)
            .clickable {
                editViewClicked()
            }) {
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (searchEdit, searchButton) = createRefs()

            MyText(
                text = hintText,
                color = Gray6,
                fontSize = dpToSp(14.dp),
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(searchEdit) {
                        linkTo(
                            top = parent.top,
                            bottom = parent.bottom,
                            topMargin = 14.dp,
                            bottomMargin = 14.dp
                        )
                        linkTo(
                            start = parent.start,
                            end = searchButton.start,
                            startMargin = 16.dp,
                            endMargin = 12.dp,
                        )
                        width = Dimension.fillToConstraints
                    })

            Image(
                painter = painterResource(id = com.zinc.berrybucket.ui_common.R.drawable.btn_32_search),
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .constrainAs(searchButton) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                        linkTo(
                            endMargin = 12.dp, end = parent.end, start = parent.start, bias = 1f
                        )
                    })
        }
    }
}

@Composable
fun RecommendDivider(modifier: Modifier = Modifier) {
    Divider(
        color = Gray2,
        modifier = modifier
            .fillMaxWidth()
            .height(6.dp)
    )
}


@Composable
fun RecommendListView(
    onOffsetChanged: (Float) -> Unit,
    maxAppBarHeight: Dp,
    minAppBarHeight: Dp,
    recommendList: RecommendList,
    bucketClicked: (String) -> Unit
) {

    val maxAppBarPixelValue = with(LocalDensity.current) { maxAppBarHeight.toPx() }
    val minAppBarPixelValue = with(LocalDensity.current) { minAppBarHeight.toPx() }
    val nestedScrollState =
        rememberNestedScrollConnection(
            onOffsetChanged = onOffsetChanged,
            maxAppBarHeight = maxAppBarPixelValue,
            minAppBarHeight = minAppBarPixelValue
        )
    LaunchedEffect(key1 = Unit, block = {
        onOffsetChanged(maxAppBarPixelValue)
    })
    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollState)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(
                top = 16.dp, bottom = 32.dp
            )
        ) {
            items(
                items = recommendList.items,
                key = { it.type },
                itemContent = {
                    RecommendTitleView(it)
                    RecommendBucketListView(it.items, bucketClicked = bucketClicked)
                    if (recommendList.items.last() != it) {
                        RecommendDivider(modifier = Modifier.padding(vertical = 32.dp))
                    }
                })
        }
    }
}

@Composable
private fun RecommendTitleView(recommendItem: RecommendItem) {
    val type = recommendItem.type
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp)
    ) {

        val (leftContent, rightContent) = createRefs()

        Column(
            modifier = Modifier
                .constrainAs(leftContent) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(if (type == RecommendType.POPULAR) rightContent.start else parent.end)
                    width = Dimension.fillToConstraints
                }
        ) {
            Row {
                Image(
                    painter = if (type == RecommendType.POPULAR) painterResource(com.zinc.berrybucket.ui_common.R.drawable.btn_32_like_on) else painterResource(
                        R.drawable.btn_32_star
                    ), contentDescription = null, modifier = Modifier
                        .size(24.dp)
                        .padding(end = 4.dp)
                )
                MyText(text = "인기", fontSize = dpToSp(15.dp), color = Gray10)
            }

            TagListView(
                tagList = recommendItem.tagList
            )
        }
        if (type == RecommendType.RECOMMEND) {
            KeyWordChangeButton(modifier = Modifier.constrainAs(rightContent) {
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
            })
        }
    }
}


@Composable
fun RecommendBucketListView(list: List<SearchBucketItem>, bucketClicked: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp)
    ) {
        list.forEach {
            RecommendBucketItemView(
                item = it, bucketClicked = bucketClicked
            )
        }
    }
}

@Composable
fun RecommendBucketItemView(
    modifier: Modifier = Modifier,
    item: SearchBucketItem,
    bucketClicked: (String) -> Unit
) {
    Card(
        backgroundColor = Gray1,
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, Gray3),
        elevation = 0.dp,
        modifier = modifier
            .padding(top = 12.dp)
            .clickable {
                bucketClicked(item.id)
            }
    ) {
        Column {
            if (item.thumbnail != null) {
                Image(
                    painter = painterResource(id = com.zinc.berrybucket.ui_common.R.drawable.kakao),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp, top = 12.dp, end = 12.dp)
                )
            }

            ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                val (bucketTitle, copiedIcon) = createRefs()

                MyText(
                    text = item.title,
                    fontSize = dpToSp(14.dp),
                    color = Gray10,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    modifier = Modifier.constrainAs(bucketTitle) {
                        linkTo(
                            top = parent.top,
                            bottom = parent.bottom,
                            topMargin = 22.dp,
                            bottomMargin = 22.dp
                        )
                        linkTo(
                            start = parent.start,
                            end = copiedIcon.start,
                            startMargin = 16.dp,
                            endMargin = 12.dp
                        )
                        width = Dimension.fillToConstraints
                    })

                IconButton(onClick = {
                    // can copied if is unCopied
                },
                    image = if (item.isCopied) com.zinc.berrybucket.ui_common.R.drawable.btn_32_copy_on else com.zinc.berrybucket.ui_common.R.drawable.btn_32_copy_off,
                    contentDescription = stringResource(id = com.zinc.berrybucket.ui_common.R.string.copy),
                    modifier = Modifier
                        .constrainAs(copiedIcon) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            end.linkTo(parent.end)
                        }
                        .padding(end = 12.dp)
                        .size(32.dp))
            }
        }
    }
}

@Composable
fun KeyWordChangeButton(modifier: Modifier) {
    Row(
        modifier = modifier
            .background(shape = RoundedCornerShape(4.dp), color = Gray1)
            .border(width = 1.dp, color = Gray4, shape = RoundedCornerShape(4.dp))
            .padding(start = 8.dp, top = 6.dp, bottom = 6.dp, end = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painterResource(R.drawable.btn_16_refresh),
            contentDescription = null,
            modifier = Modifier
                .height(24.dp)
                .width(24.dp),
            contentScale = ContentScale.Crop
        )
        MyText(
            text = stringResource(id = R.string.keywordRefresh),
            fontSize = dpToSp(13.dp),
            color = Gray9
        )
    }
}
