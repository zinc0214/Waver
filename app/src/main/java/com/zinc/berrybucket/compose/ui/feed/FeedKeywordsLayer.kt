package com.zinc.berrybucket.compose.ui.feed

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import com.zinc.berrybucket.R
import com.zinc.berrybucket.compose.theme.*
import com.zinc.berrybucket.compose.ui.common.RoundChip
import com.zinc.common.models.FeedKeyWord
import kotlin.math.min


@Composable
fun FeedKeywordsLayer(keywords: List<FeedKeyWord>, recommendClicked: () -> Unit) {

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .background(Gray2)
                .padding(horizontal = 28.dp)

        ) {
            val scrollState = rememberLazyListState()
            val scrollOffset: Float = min(
                1f,
                1 - (scrollState.firstVisibleItemScrollOffset / 600f + scrollState.firstVisibleItemIndex)
            )
            Column {
                FeedCollapsingToolbar(
                    scrollOffset = scrollOffset
                )
                if (scrollOffset <= 0.0) {
                    Divider(
                        color = Gray3
                    )
                }
                BodyContent(
                    state = scrollState,
                    keywords = keywords,
                    recommendClicked = recommendClicked
                )
            }
        }
    }
}

@Composable
private fun FeedCollapsingToolbar(scrollOffset: Float) {
    val textSize by animateDpAsState(targetValue = max(16.dp, 24.dp * scrollOffset))
    val topPadding by animateDpAsState(targetValue = max(34.dp, 40.dp * scrollOffset))
    val bottomPadding by animateDpAsState(targetValue = max(14.dp, 40.dp * scrollOffset))

    Text(
        text = if (scrollOffset > 0.0) stringResource(id = R.string.feedRecommendTitle) else stringResource(
            id = R.string.feedRecommendSmallTitle
        ),
        fontSize = textSize.value.sp,
        textAlign = if (scrollOffset > 0.0) TextAlign.Start else TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = topPadding, bottom = bottomPadding)
    )

}

@Composable
private fun BodyContent(
    state: LazyListState,
    keywords: List<FeedKeyWord>,
    recommendClicked: () -> Unit
) {
    Box(
        content = {
            ChipBodyContent(
                modifier = Modifier.padding(bottom = 100.dp),
                state = state,
                keywords = keywords
            )
            BucketRecommendButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 28.dp),
                recommendClicked
            )
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ChipBodyContent(
    modifier: Modifier = Modifier,
    state: LazyListState,
    keywords: List<FeedKeyWord>
) {
    LazyVerticalGrid(
        cells = GridCells.Adaptive(minSize = 90.dp),
        state = state,
        modifier = modifier
    ) {
        items(keywords) { keywordItem ->
            var selected by remember { mutableStateOf(false) }
            RoundChip(
                modifier = Modifier
                    .padding(horizontal = 8.5.dp, vertical = 14.dp)
                    .padding(bottom = if (keywords.last() == keywordItem) 69.dp else 0.dp)
                    .defaultMinSize(minWidth = 90.dp, minHeight = 48.dp)
                    .selectable(
                        selected = selected,
                        onClick = {
                            selected = !selected
                        }
                    ),
                chipRadius = 24.dp,
                textModifier = Modifier.padding(horizontal = 8.dp, vertical = 14.dp),
                selectedColor = Main3,
                unSelectedColor = Gray7,
                text = keywordItem.ketWord,
                isSelected = selected
            )
        }
    }
}

@Composable
private fun BucketRecommendButton(modifier: Modifier = Modifier, recommendClicked: () -> Unit) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Main4)
            .clickable {
                recommendClicked()
            }
    ) {
        Text(
            text = stringResource(id = R.string.recommendBucketList),
            textAlign = TextAlign.Center,
            color = Gray1,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(vertical = 14.dp, horizontal = 24.dp)
                .fillMaxWidth()
        )
    }
}