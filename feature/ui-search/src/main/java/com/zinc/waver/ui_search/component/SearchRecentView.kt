package com.zinc.waver.ui_search.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.zinc.waver.ui.design.theme.Gray6
import com.zinc.waver.ui.design.theme.Gray9
import com.zinc.waver.ui.design.theme.Main4
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.ui_search.R

@Composable
fun SearchRecentView(
    modifier: Modifier,
    recentWords: List<String>,
    itemClicked: (String) -> Unit,
    recentItemDelete: (String) -> Unit,
) {
    if (recentWords.isEmpty()) {
        SearchRecentEmptyView()
    } else {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            MyText(
                text = stringResource(id = R.string.recentSearch),
                fontSize = dpToSp(15.dp),
                fontWeight = FontWeight.Bold,
                color = Main4,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
            )
            RecentSearchView(
                recentWords,
                itemClicked = itemClicked,
                recentItemDelete = recentItemDelete
            )
        }
    }
}

@Composable
private fun RecentSearchView(
    recentItems: List<String>,
    itemClicked: (String) -> Unit,
    recentItemDelete: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(top = 24.dp)
    ) {
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
                contentDescription = stringResource(id = com.zinc.waver.ui_common.R.string.delete),
                alignment = Alignment.CenterEnd
            )
        }
    }
}

@Composable
private fun SearchRecentEmptyView() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 140.dp),
        contentAlignment = Alignment.Center
    ) {
        MyText(
            text = stringResource(id = R.string.noRecentSearch),
            fontSize = dpToSp(16.dp),
            color = Gray6
        )
    }
}

@Preview
@Composable
private fun SearchRecentViewPreview1() {
    SearchRecentView(
        modifier = Modifier,
        recentWords = listOf("Recent Search 1", "Recent Search 2", "Recent Search 3"),
        itemClicked = {},
        recentItemDelete = {}
    )
}

@Preview
@Composable
private fun SearchRecentViewPreview2() {
    SearchRecentView(
        modifier = Modifier,
        recentWords = emptyList(),
        itemClicked = {},
        recentItemDelete = {}
    )
}