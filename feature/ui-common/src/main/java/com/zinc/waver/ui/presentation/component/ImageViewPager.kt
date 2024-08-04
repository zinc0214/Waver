package com.zinc.waver.ui.presentation.component

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.zinc.waver.ui_common.R


@OptIn(ExperimentalFoundationApi::class)
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
        val pagerState = rememberPagerState(pageCount = { imageList.size })

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
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                ) { page ->
                    Log.e("ayhan", "imgUrl : ${imageList[page]}")
                    val painter = rememberAsyncImagePainter(
                        model = imageList[page],
                        placeholder = painterResource(R.drawable.kakao),
                        error = painterResource(R.drawable.kakao)
                    )
                    Image(
                        painter = painter,
                        contentDescription = stringResource(id = R.string.bucketImage),
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
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


@OptIn(ExperimentalFoundationApi::class)
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
            val pagerState = rememberPagerState(pageCount = { imageList.size })

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
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) { page ->

                        val painter = rememberAsyncImagePainter(
                            model = imageList[page],
                            placeholder = painterResource(R.drawable.kakao),
                            error = painterResource(R.drawable.kakao)
                        )
                        Image(
                            painter = painter,
                            contentDescription = stringResource(id = R.string.bucketImage),
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
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
