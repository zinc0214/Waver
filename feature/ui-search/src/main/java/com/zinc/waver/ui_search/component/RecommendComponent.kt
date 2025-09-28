package com.zinc.waver.ui_search.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.zinc.waver.ui.design.theme.Gray1
import com.zinc.waver.ui.design.theme.Gray10
import com.zinc.waver.ui.design.theme.Gray2
import com.zinc.waver.ui.design.theme.Gray3
import com.zinc.waver.ui.design.theme.Gray4
import com.zinc.waver.ui.design.theme.Gray6
import com.zinc.waver.ui.design.theme.Gray9
import com.zinc.waver.ui.presentation.component.IconButton
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.presentation.component.TagListView
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.ui_search.R
import com.zinc.waver.ui_search.model.RecommendItem
import com.zinc.waver.ui_search.model.RecommendList
import com.zinc.waver.ui_search.model.RecommendType
import com.zinc.waver.ui_search.model.SearchBucketItem
import com.zinc.waver.ui_common.R as CommonR

@Composable
fun RecommendTopBar(
    modifier: Modifier,
    editViewClicked: () -> Unit,
    isFirstItemShown: Boolean
) {

    Column(modifier = modifier) {
        AnimatedVisibility(visible = isFirstItemShown) {
            SearchTitle()
        }
        SearchTextView {
            editViewClicked.invoke()
        }

        AnimatedVisibility(visible = !isFirstItemShown) {
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
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Gray1)
            .padding(top = 24.dp)
            .padding(horizontal = 28.dp)
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
                painter = painterResource(id = com.zinc.waver.ui_common.R.drawable.btn_32_search),
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
    recommendList: RecommendList,
    bucketClicked: (String, String) -> Unit,
    isFirstItemShown: (Boolean) -> Unit
) {

    val listScrollState = rememberLazyListState()
    val isFirstItemShownState = listScrollState.canScrollBackward.not()

    LaunchedEffect(key1 = isFirstItemShownState) {
        isFirstItemShown(isFirstItemShownState)
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
        state = listScrollState,
        contentPadding = PaddingValues(
            top = 16.dp, bottom = 32.dp
        )
    ) {
        items(
            items = recommendList.items,
            key = { it.type },
            itemContent = {
                RecommendTitleView(it)
                RecommendBucketListView(
                    it.items,
                    bucketClicked = bucketClicked
                )
                if (recommendList.items.last() != it) {
                    RecommendDivider(modifier = Modifier.padding(vertical = 32.dp))
                }
            })
    }
}

@Composable
private fun RecommendTitleView(recommendItem: RecommendItem) {
    val type = recommendItem.type

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row {
                Image(
                    painter = if (type == RecommendType.POPULAR) painterResource(CommonR.drawable.btn_32_like_on) else painterResource(
                        R.drawable.btn_32_star
                    ), contentDescription = null, modifier = Modifier
                        .size(24.dp)
                        .padding(end = 4.dp)
                )
                MyText(text = recommendItem.title, fontSize = dpToSp(15.dp), color = Gray10)
            }

            TagListView(
                modifier = Modifier,
                tagList = recommendItem.tagList
            )
        }
        if (type == RecommendType.RECOMMEND) {
            KeyWordChangeButton(
                modifier = Modifier
                    .padding(start = 6.dp)
                    .align(Alignment.Bottom)
            )
        }
    }
}


@Composable
fun RecommendBucketListView(
    list: List<SearchBucketItem>,
    bucketClicked: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
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
    bucketClicked: (String, String) -> Unit
) {
    Card(
        backgroundColor = Gray1,
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, Gray3),
        elevation = 0.dp,
        modifier = modifier
            .padding(top = 12.dp)
            .clickable {
                bucketClicked(item.bucketId, item.writerId)
            }
    ) {
        Column {
            if (item.thumbnail != null) {
                Image(
                    painter = painterResource(id = com.zinc.waver.ui_common.R.drawable.profile_placeholder),
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
                    image = if (item.isCopied) com.zinc.waver.ui_common.R.drawable.btn_32_copy_on else com.zinc.waver.ui_common.R.drawable.btn_32_copy_off,
                    contentDescription = stringResource(id = com.zinc.waver.ui_common.R.string.copy),
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


@Preview(showBackground = true)
@Composable
private fun RecommendTitlePreview() {

    RecommendTitleView(
        recommendItem = RecommendItem(
            title = "delenit",
            type = RecommendType.RECOMMEND,
            tagList = listOf("제주도", "1박2일", "좀 길어버린 텍스투"),
            items = listOf()
        )
    )
}