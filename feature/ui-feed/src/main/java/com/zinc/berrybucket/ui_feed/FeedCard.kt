package com.zinc.berrybucket.ui_feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.zinc.berrybucket.ui.design.theme.Gray1
import com.zinc.berrybucket.ui.design.theme.Gray10
import com.zinc.berrybucket.ui.design.theme.Gray5
import com.zinc.berrybucket.ui.design.theme.Main4
import com.zinc.berrybucket.ui.design.theme.Main5
import com.zinc.berrybucket.ui.presentation.component.IconButton
import com.zinc.berrybucket.ui.presentation.component.ImageViewPagerOutSideIndicator
import com.zinc.berrybucket.ui.presentation.component.MyText
import com.zinc.berrybucket.ui.presentation.component.ProfileView
import com.zinc.berrybucket.ui.util.dpToSp
import com.zinc.berrybucket.ui_common.R
import com.zinc.berrybucket.ui_feed.models.UIFeedInfo
import com.zinc.berrybucket.ui_feed.models.profileInfo
import com.zinc.berrybucket.util.shadow


@Composable
fun FeedListView(
    modifier: Modifier = Modifier,
    feedItems: List<UIFeedInfo>,
    feedClicked: (String) -> Unit
) {
    Column(modifier = modifier) {
        feedItems.forEach { feed ->
            FeedCardView(
                feedInfo = feed,
                feedClicked = {
                    feedClicked(it)
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}


@Composable
fun FeedCardView(feedInfo: UIFeedInfo, feedClicked: (String) -> Unit) {

    Card(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = Gray1,
        modifier = Modifier
            .background(color = Gray1, shape = RoundedCornerShape(8.dp))
            .shadow(
                color = Gray5.copy(alpha = 0.2f),
                offsetX = (0).dp,
                offsetY = (0).dp,
                blurRadius = 8.dp,
            )
            .clip(
                RoundedCornerShape(8.dp)
            )
            .clickable {
                feedClicked(feedInfo.bucketId)
            }
    ) {
        Column {
            ProfileView(
                modifier = Modifier.padding(top = 20.dp),
                profileInfo = feedInfo.profileInfo().toUi()
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

            if (feedInfo.imageList.isNullOrEmpty().not()) {
                ImageView(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(top = 20.dp),
                    imageList = feedInfo.imageList.orEmpty()
                )
            }

            BottomStateView(
                modifier = Modifier
                    .padding(horizontal = 14.dp)
                    .padding(top = 12.dp, bottom = 20.dp)
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
                painter = painterResource(id = if (isProcessing) R.drawable.status_process_img else R.drawable.stauts_success_img),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(61.dp, 21.dp)
                    .align(Alignment.Center)
            )
            MyText(
                text = stringResource(
                    id = if (isProcessing) R.string.proceedingText else R.string.succeedText
                ),
                color = if (isProcessing) Main4 else Main5,
                fontSize = dpToSp(13.dp),
                modifier = Modifier.align(Alignment.Center)
            )
        }
    )
}

@Composable
private fun TitleView(modifier: Modifier = Modifier, title: String) {
    MyText(
        modifier = modifier,
        text = title,
        fontSize = dpToSp(18.dp),
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
private fun BottomStateView(modifier: Modifier = Modifier, feedInfo: UIFeedInfo) {
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

            IconButton(
                image = if (liked.value) R.drawable.btn_32_like_on else R.drawable.btn_32_like_off,
                contentDescription = null,
                onClick = {
                    liked.value = !liked.value
                },
                modifier = Modifier
                    .size(32.dp)
            )

            Spacer(modifier = Modifier.width(4.dp))

            MyText(
                text = feedInfo.likeCount.toString(),
                color = Gray10,
                fontSize = dpToSp(12.dp),
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )

            Spacer(modifier = Modifier.width(12.dp))

            IconButton(
                image = R.drawable.btn_32_comment,
                contentDescription = null,
                onClick = {
                    // Go TO Comment!
                },
                modifier = Modifier
                    .size(32.dp)
            )

            Spacer(modifier = Modifier.width(4.dp))

            MyText(
                text = feedInfo.commentCount.toString(),
                color = Gray10,
                fontSize = dpToSp(12.dp),
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )
        }

        IconButton(
            image = if (feedInfo.isScraped) R.drawable.btn_32_copy_on else R.drawable.btn_32_copy_off,
            contentDescription = null,
            onClick = {
                // Go TO Comment!
            },
            modifier = Modifier
                .size(32.dp)
                .constrainAs(rightContent) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        )
    }
}