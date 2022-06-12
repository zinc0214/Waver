package com.zinc.berrybucket.ui.presentation.search

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.zinc.berrybucket.R
import com.zinc.berrybucket.model.RecommendType
import com.zinc.berrybucket.model.toKorean
import com.zinc.berrybucket.ui.compose.theme.*
import com.zinc.berrybucket.ui.presentation.common.IconButton
import com.zinc.berrybucket.ui.presentation.common.TagListView
import com.zinc.berrybucket.ui.presentation.common.rememberNestedScrollConnection
import com.zinc.common.models.RecommendBucketItem
import com.zinc.common.models.RecommendItem
import com.zinc.common.models.RecommendList
import com.zinc.common.models.SearchRecommendCategory


@Composable
fun SearchTopBar(
    modifier: Modifier,
    editViewClicked: () -> Unit,
    height: Dp,
    recommendCategoryItemList: List<SearchRecommendCategory>?
) {

    Log.e("ayhan", "height : $height")
    Column(modifier = modifier.height(height)) {
        if (height >= 130.dp) {
            SearchTitle()
        }
        SearchEditView {
            editViewClicked.invoke()
        }
        if (height >= 150.dp) {
            recommendCategoryItemList?.let {
                SearchRecommendCategoryItemsView(it)
            }
        }
    }
}

@Composable
fun SearchTitle() {
    Text(
        text = stringResource(id = R.string.searchTitle),
        fontSize = 24.sp,
        color = Gray10,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Gray1)
            .padding(top = 24.dp, bottom = 4.dp, start = 28.dp, end = 17.dp)
    )
}

@Composable
private fun SearchEditView(
    editViewClicked: () -> Unit
) {
    val hintText = stringResource(id = R.string.myBucketSearchHint)
    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    Box(modifier = Modifier
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

            BasicTextField(value = searchText,
                textStyle = TextStyle(
                    color = Gray10, fontSize = 14.sp, fontWeight = FontWeight.Medium
                ),
                onValueChange = { searchText = it },
                maxLines = 1,
                decorationBox = { innerTextField ->
                    Row {
                        if (searchText.text.isEmpty()) {
                            Text(text = hintText, color = Gray6, fontSize = 20.sp)
                        }
                        innerTextField()  //<-- Add this
                    }
                },
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

            Image(painter = painterResource(id = R.drawable.btn_32_search),
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
private fun SearchRecommendCategoryItemsView(items: List<SearchRecommendCategory>) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Gray1)
            .padding(start = 32.dp, end = 32.dp, bottom = 32.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        items(items = items, itemContent = { item ->
            RecommendCategoryItem(item = item, isLastItem = item == items.last())
        })
    }
}

@Composable
private fun RecommendCategoryItem(item: SearchRecommendCategory, isLastItem: Boolean) {
    Column(
        modifier = Modifier
            .padding(end = if (isLastItem) 0.dp else 35.dp)
            .width(48.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.test),
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .padding(bottom = 10.dp)
        )
        Text(
            text = item.category,
            fontSize = 14.sp,
            color = Gray10,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SearchDivider(modifier: Modifier = Modifier) {
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
    recommendList: RecommendList
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
                .fillMaxWidth()
                .padding(top = 32.dp),
        ) {
            items(recommendList.items, itemContent = {
                RecommendTitleView(it)
                RecommendBucketListView(it.items)
                if (recommendList.items.last() != it) {
                    SearchDivider(modifier = Modifier.padding(vertical = 32.dp))
                } else {
                    Spacer(modifier = Modifier.height(32.dp))
                }
            })
        }
    }

}

@Composable
private fun RecommendTitleView(recommendItem: RecommendItem) {
    val type =
        RecommendType.values().find { it.title == recommendItem.type } ?: RecommendType.RECOMMEND
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp)
    ) {
        Row {
            Image(
                painter = if (type == RecommendType.POPULAR) painterResource(R.drawable.btn_32_like_on) else painterResource(
                    R.drawable.btn_32_star
                ), contentDescription = null, modifier = Modifier
                    .size(24.dp)
                    .padding(end = 4.dp)
            )
            Text(text = type.toKorean(), fontSize = 15.sp, color = Gray10)
        }

        TagListView(
            tagList = recommendItem.tagList
        )
    }
}

@Composable
private fun RecommendBucketListView(list: List<RecommendBucketItem>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp)
    ) {
        list.forEach {
            RecommendBucketItemView(it)
        }
    }
}

@Composable
private fun RecommendBucketItemView(item: RecommendBucketItem) {
    Card(
        backgroundColor = Gray1,
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, Gray3),
        elevation = 0.dp,
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Column {
            if (item.thumbnail != null) {
                Image(
                    painter = painterResource(id = R.drawable.kakao),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp, top = 12.dp, end = 12.dp)
                )
            }

            ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                val (bucketTitle, copiedIcon) = createRefs()

                Text(text = item.title,
                    fontSize = 14.sp,
                    color = Gray10,
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
                    image = if (item.isCopied) R.drawable.btn_32_copy_on else R.drawable.btn_32_copy_off,
                    contentDescription = stringResource(id = R.string.copy),
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
