package com.zinc.berrybucket.ui_feed

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.zinc.berrybucket.ui.design.theme.Gray1
import com.zinc.berrybucket.ui.design.theme.Gray2
import com.zinc.berrybucket.ui.design.theme.Gray3
import com.zinc.berrybucket.ui.design.theme.Gray7
import com.zinc.berrybucket.ui.design.theme.Main3
import com.zinc.berrybucket.ui.design.theme.Main4
import com.zinc.berrybucket.ui.presentation.component.MyText
import com.zinc.berrybucket.ui.presentation.component.RoundChip
import com.zinc.berrybucket.ui.util.dpToSp
import com.zinc.berrybucket.ui_feed.models.UIFeedKeyword


@Composable
fun FeedKeywordsLayer(keywords: List<UIFeedKeyword>, recommendClicked: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(Gray2)

    ) {
        val scrollState = rememberLazyGridState()
        var offset by remember { mutableFloatStateOf(0f) }
        var prevOffset by remember { mutableFloatStateOf(0f) }

        val firstVisibleIndex by remember {
            derivedStateOf { scrollState.firstVisibleItemIndex }
        }

        val isScrolled = remember {
            mutableStateOf(false)
        }

        LaunchedEffect(key1 = offset) {
            // Scroll Down
            if (offset < prevOffset && !isScrolled.value) {
                isScrolled.value = true
            } else if (offset > prevOffset && firstVisibleIndex == 0) {
                isScrolled.value = false
            }
            prevOffset = offset
        }

        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (toolbar, divider, body) = createRefs()

            FeedCollapsingToolbar(
                isScrolled = isScrolled.value,
                modifier = Modifier
                    .constrainAs(toolbar) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .padding(horizontal = 28.dp)
            )

            if (isScrolled.value) {
                Divider(color = Gray3, modifier = Modifier.constrainAs(divider) {
                    top.linkTo(toolbar.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
            }

            BodyContent(state = scrollState,
                keywords = keywords,
                recommendClicked = recommendClicked,
                modifier = Modifier
                    .constrainAs(body) {
                        top.linkTo(toolbar.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                    }
                    .padding(horizontal = 28.dp),
                scrollChanged = {
                    offset += it
                }
            )
        }
    }
}

@Composable
private fun FeedCollapsingToolbar(
    isScrolled: Boolean, modifier: Modifier
) {
    Box(
        modifier = modifier
            .animateContentSize()
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        MyText(
            text = if (isScrolled) stringResource(
                id = R.string.feedRecommendSmallTitle
            ) else stringResource(id = R.string.feedRecommendTitle),
            fontSize = if (isScrolled) dpToSp(16.dp) else dpToSp(24.dp),
            fontWeight = FontWeight.Bold,
            textAlign = if (isScrolled) TextAlign.Center else TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = if (isScrolled) 14.dp else 40.dp,
                    bottom = if (isScrolled) 14.dp else 35.dp,
                )
        )
    }

}

@Composable
private fun BodyContent(
    state: LazyGridState,
    keywords: List<UIFeedKeyword>,
    recommendClicked: () -> Unit,
    modifier: Modifier,
    scrollChanged: (Float) -> Unit
) {
    Box(modifier = modifier, content = {
        ChipBodyContent(
            modifier = Modifier.padding(bottom = 100.dp), state = state, keywords = keywords
        ) {
            scrollChanged(it)
        }
        BucketRecommendButton(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 28.dp), recommendClicked
        )
    })
}

@Composable
private fun ChipBodyContent(
    modifier: Modifier = Modifier,
    state: LazyGridState,
    keywords: List<UIFeedKeyword>,
    scrollChanged: (Float) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 90.dp),
        state = state,
        modifier = modifier.scrollable(
            orientation = Orientation.Vertical,
            state = rememberScrollableState { delta ->
                scrollChanged(delta)
                delta
            }
        )) {
        items(items = keywords) { keywordItem ->
            var selected by remember { mutableStateOf(false) }
            RoundChip(
                modifier = Modifier
                    .padding(horizontal = 8.5.dp, vertical = 14.dp)
                    .padding(bottom = if (keywords.last() == keywordItem) 69.dp else 0.dp)
                    .defaultMinSize(minWidth = 90.dp, minHeight = 48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .selectable(selected = selected, onClick = {
                        selected = !selected
                    }),
                chipRadius = 24.dp,
                textModifier = Modifier.padding(horizontal = 8.dp, vertical = 14.dp),
                selectedTextColor = Main3,
                unSelectedTextColor = Gray7,
                text = keywordItem.keyword,
                isSelected = selected,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun BucketRecommendButton(modifier: Modifier = Modifier, recommendClicked: () -> Unit) {
    MyText(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Main4, shape = RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                recommendClicked()
            }
            .padding(vertical = 14.dp, horizontal = 24.dp),
        text = stringResource(id = R.string.recommendBucketList),
        textAlign = TextAlign.Center,
        color = Gray1,
        fontWeight = FontWeight.Bold,
        fontSize = dpToSp(16.dp)
    )
}