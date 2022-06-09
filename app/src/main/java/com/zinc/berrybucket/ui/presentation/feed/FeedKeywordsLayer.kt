package com.zinc.berrybucket.ui.presentation.feed

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.zinc.berrybucket.R
import com.zinc.berrybucket.ui.compose.theme.*
import com.zinc.berrybucket.ui.presentation.common.RoundChip
import com.zinc.common.models.FeedKeyWord
import kotlin.math.min


@Composable
fun FeedKeywordsLayer(keywords: List<FeedKeyWord>, recommendClicked: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(Gray2)

    ) {
        val scrollState = rememberLazyGridState()
        val isScrolled = scrollState.firstVisibleItemIndex != 0
        Log.e(
            "ayhan",
            "firstVisibleItemScrollOffset ; ${scrollState.firstVisibleItemScrollOffset}, ${scrollState.firstVisibleItemIndex}"
        )

        val scrollOffset: Float = min(
            1f, 1 - (scrollState.firstVisibleItemScrollOffset / 150f)
        )
        Log.e("ayhan", "scrollOffset : $scrollOffset")


        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (toolbar, divider, body) = createRefs()

            FeedCollapsingToolbar(
                scrollOffset = scrollOffset,
                isScrolled = isScrolled,
                modifier = Modifier
                    .constrainAs(toolbar) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .padding(horizontal = 28.dp)
            )

            if (scrollOffset <= 0.0 || isScrolled) {
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
                    .padding(horizontal = 28.dp)
            )
        }
    }
}

@Composable
private fun FeedCollapsingToolbar(
    scrollOffset: Float, isScrolled: Boolean, modifier: Modifier
) {
    val textSize by animateDpAsState(targetValue = max(16.dp, 24.dp * scrollOffset))
    val topPadding by animateDpAsState(targetValue = max(34.dp, 40.dp * scrollOffset))
    val bottomPadding by animateDpAsState(targetValue = max(14.dp, 40.dp * scrollOffset))

    Text(
        text = if (scrollOffset > 0.0 && !isScrolled) stringResource(id = R.string.feedRecommendTitle) else stringResource(
            id = R.string.feedRecommendSmallTitle
        ),
        fontSize = if (isScrolled) 16.sp else textSize.value.sp,
        fontWeight = FontWeight.Bold,
        textAlign = if (scrollOffset > 0.0 && !isScrolled) TextAlign.Start else TextAlign.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = if (isScrolled) 34.dp else topPadding,
                bottom = if (isScrolled) 14.dp else bottomPadding
            )
    )

}

@Composable
private fun BodyContent(
    state: LazyGridState,
    keywords: List<FeedKeyWord>,
    recommendClicked: () -> Unit,
    modifier: Modifier
) {
    Box(modifier = modifier, content = {
        ChipBodyContent(
            modifier = Modifier.padding(bottom = 100.dp), state = state, keywords = keywords
        )
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
    modifier: Modifier = Modifier, state: LazyGridState, keywords: List<FeedKeyWord>
) {
    androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 90.dp), state = state, modifier = modifier
    ) {
        items(items = keywords) { keywordItem ->
            var selected by remember { mutableStateOf(false) }
            RoundChip(
                modifier = Modifier
                    .padding(horizontal = 8.5.dp, vertical = 14.dp)
                    .padding(bottom = if (keywords.last() == keywordItem) 69.dp else 0.dp)
                    .defaultMinSize(minWidth = 90.dp, minHeight = 48.dp)
                    .selectable(selected = selected, onClick = {
                        selected = !selected
                    }),
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
    Box(modifier = modifier
        .clip(RoundedCornerShape(12.dp))
        .background(Main4)
        .clickable {
            recommendClicked()
        }) {
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