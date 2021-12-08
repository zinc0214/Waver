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
import com.zinc.berrybucket.compose.theme.Gray1
import com.zinc.berrybucket.compose.theme.Gray2
import com.zinc.berrybucket.compose.theme.Gray3
import com.zinc.berrybucket.compose.theme.Main4
import com.zinc.berrybucket.compose.ui.component.BoxedChip
import kotlin.math.min


@Composable
fun FeedKeywordsLayer(keywords: List<String>, recommendClicked: () -> Unit) {

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
                BodyContent(scrollState, keywords, recommendClicked)
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
    keywords: List<String>,
    recommendClicked: () -> Unit
) {
    Box(
        content = {
            ChipBodyContent(
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
    keywords: List<String>
) {
    LazyVerticalGrid(
        cells = GridCells.Adaptive(minSize = 90.dp),
        state = state,
        modifier = modifier
    ) {
        items(keywords) { keyword ->
            var selected by remember { mutableStateOf(false) }

            BoxedChip(
                modifier = Modifier
                    .padding(horizontal = 8.5.dp, vertical = 14.dp)
                    .size(width = 90.dp, height = 40.dp)
                    .selectable(
                        selected = selected,
                        onClick = {
                            selected = !selected
                        }
                    ),
                text = keyword,
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