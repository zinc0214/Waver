package com.zinc.berrybucket.compose.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.zinc.berrybucket.R
import com.zinc.berrybucket.compose.theme.Gray1
import com.zinc.berrybucket.compose.theme.Gray10
import com.zinc.berrybucket.compose.theme.Main4
import com.zinc.berrybucket.compose.theme.Main5
import com.zinc.berrybucket.model.FeedInfo
import com.zinc.berrybucket.model.profileInfo


@Composable
fun FeedListView(modifier: Modifier = Modifier, feedList: List<FeedInfo>) {
    Column(modifier = modifier) {
        feedList.forEach { feed ->
            FeedCardView(
                feedInfo = feed,
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}


@Composable
fun FeedCardView(feedInfo: FeedInfo) {

    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = 2.dp,
        backgroundColor = Gray1
    ) {
        Column {
            ProfileView(
                modifier = Modifier.padding(top = 20.dp),
                profileInfo = feedInfo.profileInfo()
            )
            ProcessView(
                modifier = Modifier.padding(top = 25.dp, start = 10.dp),
                isProcessing = feedInfo.isProcessing
            )
            TitleView(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 10.dp),
                title = feedInfo.title
            )

            if (!feedInfo.imageList.isNullOrEmpty()) {
                ImageView(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(top = 20.dp),
                    imageList = feedInfo.imageList
                )
            }

            BottomStateView(
                modifier = Modifier
                    .padding(horizontal = 14.dp)
                    .padding(top = 8.dp, bottom = 20.dp)
                    .fillMaxWidth(),
                feedInfo = feedInfo
            )
        }
    }
}

@Composable
private fun ProcessView(modifier: Modifier = Modifier, isProcessing: Boolean) {
    Box(modifier = modifier,
        content = {
            Image(
                painter = painterResource(id = if (isProcessing) R.drawable.feed_process_img else R.drawable.feed_success_img),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(61.dp, 21.dp)
                    .align(Alignment.Center)
            )
            Text(
                text = stringResource(
                    id = if (isProcessing) R.string.proceedingText else R.string.succeedText
                ),
                color = if (isProcessing) Main4 else Main5,
                fontSize = 13.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    )
}

@Composable
private fun TitleView(modifier: Modifier = Modifier, title: String) {
    Text(
        modifier = modifier,
        text = title,
        fontSize = 15.sp,
        color = Gray10
    )
}

@Composable
private fun ImageView(modifier: Modifier = Modifier, imageList: List<String>) {
    ImageViewPagerOutSideIndicator(
        modifier = modifier,
        imageList = imageList
    )
}

@Composable
private fun BottomStateView(modifier: Modifier = Modifier, feedInfo: FeedInfo) {
    ConstraintLayout(modifier = modifier) {


        val (leftContent, rightContent) = createRefs()

        Row(modifier = Modifier.constrainAs(leftContent) {
            start.linkTo(parent.start)
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
        }) {
            val liked = remember {
                mutableStateOf(feedInfo.liked)
            }

            Image(
                painter = painterResource(id = if (liked.value) R.drawable.btn_32_like_on else R.drawable.btn_32_like_off),
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        liked.value = !liked.value
                    }
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = feedInfo.likeCount,
                color = Gray10,
                fontSize = 12.sp,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Image(
                painter = painterResource(id = R.drawable.btn_32_comment),
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        // Go TO Comment!
                    }
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = feedInfo.commentCount,
                color = Gray10,
                fontSize = 12.sp,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )
        }

        Image(
            painter = painterResource(id = if (feedInfo.copied) R.drawable.btn_32_copy_on else R.drawable.btn_32_copy_off),
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
                .clickable {
                    // Go TO Comment!
                }
                .constrainAs(rightContent) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        )
    }
}