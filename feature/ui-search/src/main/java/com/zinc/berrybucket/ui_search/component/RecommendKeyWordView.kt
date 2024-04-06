package com.zinc.berrybucket.ui_search.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.zinc.berrybucket.ui.design.theme.Gray6
import com.zinc.berrybucket.ui.design.theme.Gray9
import com.zinc.berrybucket.ui.design.theme.Main4
import com.zinc.berrybucket.ui.presentation.component.MyText
import com.zinc.berrybucket.ui.util.dpToSp
import com.zinc.berrybucket.ui_search.R
import com.zinc.berrybucket.ui_search.model.KeyWordItem
import com.zinc.berrybucket.ui_search.model.SearchRecommendItems
import com.zinc.berrybucket.ui_search.model.SearchRecommendType

@Composable
fun RecommendKeyWordView(
    searchItems: SearchRecommendItems,
    showOnlyRecommend: Boolean,
    itemClicked: (String) -> Unit,
    recentItemDelete: (String) -> Unit,
) {
    var selectType by remember {
        mutableStateOf(
            if (showOnlyRecommend) SearchRecommendType.RECOMMEND else SearchRecommendType.RECENT
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
    ) {
        Row(modifier = Modifier.padding(horizontal = 24.dp)) {
            if (!showOnlyRecommend) {
                MyText(text = stringResource(id = R.string.recentSearch),
                    fontSize = dpToSp(15.dp),
                    fontWeight = FontWeight.Bold,
                    color = if (selectType == SearchRecommendType.RECENT) Main4 else Gray6,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .clickable {
                            selectType = SearchRecommendType.RECENT
                        })
            }
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
            if (!showOnlyRecommend) {
                AnimatedVisibility(
                    visible = selectType == SearchRecommendType.RECENT
                ) {
                    RecentSearchView(
                        searchItems.recentWords,
                        itemClicked = itemClicked,
                        recentItemDelete = recentItemDelete
                    )
                }
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
private fun RecommendKeyWordView(keyWordItems: List<KeyWordItem>, itemClicked: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        keyWordItems.forEach {
            RecommendKeyWordItem(it, itemClicked)
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
