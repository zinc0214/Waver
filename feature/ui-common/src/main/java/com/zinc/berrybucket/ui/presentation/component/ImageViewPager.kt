package com.zinc.berrybucket.ui.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.zinc.berrybucket.ui_common.R


@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImageViewPagerOutSideIndicator(
    modifier: Modifier = Modifier,
    corner: Dp = 4.dp,
    imageList: List<String>
) {
    Column(
        modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        val pagerState = rememberPagerState()

        Card(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .wrapContentHeight()
                .aspectRatio(1f),
            shape = RoundedCornerShape(corner),
            elevation = 0.dp,
            content = {
                // Display 10 items
                HorizontalPager(
                    count = imageList.size,
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                ) { page ->
                    Image(
                        painter = painterResource(id = R.drawable.kakao),
                        contentDescription = "Test",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        )

        if (imageList.size > 1) {
            HorizontalPagerIndicator(
                pagerState = pagerState,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 4.dp),
            )
        }
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImageViewPagerInsideIndicator(
    modifier: Modifier = Modifier,
    corner: Dp = 0.dp,
    indicatorModifier: Modifier = Modifier,
    imageList: List<String>
) {
    Box(
        modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        content = {
            val pagerState = rememberPagerState()

            Card(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .aspectRatio(1f),
                shape = RoundedCornerShape(corner),
                elevation = 0.dp,
                content = {
                    HorizontalPager(
                        count = imageList.size,
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) { page ->

                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(pagerState.currentPage)
                                .crossfade(true)
                                .build(),
                            error = painterResource(R.drawable.kakao),
                            placeholder = painterResource(R.drawable.kakao),
                            contentDescription = stringResource(R.string.bucketImage),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            )

            if (imageList.size > 1) {
                PageCountIndicator(
                    pagerState = pagerState,
                    modifier = indicatorModifier
                        .align(Alignment.BottomEnd),
                )
            }
        }
    )
}
