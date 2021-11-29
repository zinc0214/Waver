package com.zinc.berrybucket.compose.ui.feed

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zinc.berrybucket.R
import com.zinc.berrybucket.compose.theme.Gray1
import com.zinc.berrybucket.compose.theme.Gray10
import com.zinc.berrybucket.compose.theme.Gray2
import com.zinc.berrybucket.compose.theme.Main4
import com.zinc.berrybucket.compose.ui.component.BoxedChip


@Composable
fun FeedKeywordsLayer(keywords: List<String>) {

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .background(Gray2)
                .padding(horizontal = 28.dp)
                .padding(top = 40.dp)

        ) {
            TitleView()
            ChipBodyContent(
                modifier = Modifier.padding(top = 40.dp),
                keywords = keywords
            )
            Column(
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 28.dp)
                    .fillMaxWidth()
            ) {

                BucketRecommendButton(
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}


@Composable
private fun TitleView() {
    Text(
        text = stringResource(id = R.string.feedRecommendTitle),
        color = Gray10,
        fontSize = 24.sp
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ChipBodyContent(
    modifier: Modifier = Modifier,
    keywords: List<String>
) {
    LazyVerticalGrid(
        cells = GridCells.Adaptive(minSize = 90.dp),
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
private fun BucketRecommendButton(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Main4)
            .clickable { }
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