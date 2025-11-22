package com.zinc.waver.ui_feed

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.zinc.waver.ui.design.theme.Gray1
import com.zinc.waver.ui.design.theme.Gray10
import com.zinc.waver.ui.design.theme.Gray5
import com.zinc.waver.ui.design.theme.Main4
import com.zinc.waver.ui.design.theme.Main5
import com.zinc.waver.ui.presentation.component.IconButton
import com.zinc.waver.ui.presentation.component.ImageViewPagerOutSideIndicator
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.presentation.component.ProfileView
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.ui_common.R
import com.zinc.waver.ui_feed.models.FeedClickEvent
import com.zinc.waver.ui_feed.models.UIFeedInfo
import com.zinc.waver.ui_feed.models.profileInfo
import com.zinc.waver.util.shadow


@Composable
fun FeedListView(
    modifier: Modifier = Modifier,
    feedItems: List<UIFeedInfo>,
    feedClicked: (FeedClickEvent) -> Unit
) {
    Column(modifier = modifier) {
        feedItems.forEach { feed ->
            FeedCardView(
                feedInfo = feed,
                clickEvent = feedClicked
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}


@Composable
fun FeedCardView(
    feedInfo: UIFeedInfo,
    clickEvent: (FeedClickEvent) -> Unit
) {

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
                clickEvent.invoke(FeedClickEvent.GoToBucket(feedInfo.bucketId, feedInfo.writerId))
            }
    ) {
        Column {
            ProfileView(
                modifier = Modifier.padding(top = 20.dp),
                profileInfo = feedInfo.profileInfo().toUi(),
                profileSize = 32.dp,
                badgeSize = 20.dp,
                imageSize = 33.dp,
                profileRadius = 12.dp
            )
            ProcessView(
                modifier = Modifier.padding(top = 23.dp, start = 10.dp),
                isProcessing = feedInfo.isProcessing
            )
            TitleView(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 10.dp),
                title = feedInfo.title
            )

            if (feedInfo.images.isNullOrEmpty().not()) {
                ImageView(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(top = 20.dp),
                    imageList = feedInfo.images
                )
            }

            BottomStateView(
                modifier = Modifier
                    .padding(horizontal = 14.dp)
                    .padding(top = 12.dp, bottom = 20.dp)
                    .fillMaxWidth(),
                feedInfo = feedInfo,
                clickEvent = clickEvent
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
                    .size(60.dp, 20.dp)
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
private fun BottomStateView(
    modifier: Modifier = Modifier,
    feedInfo: UIFeedInfo,
    clickEvent: (FeedClickEvent) -> Unit
) {
    val likeImage = if (feedInfo.liked) R.drawable.btn_32_like_on else R.drawable.btn_32_like_off
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
                image = likeImage,
                contentDescription = null,
                onClick = {
                    clickEvent.invoke(FeedClickEvent.Like(!liked.value, feedInfo.bucketId))
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
                    clickEvent.invoke(
                        FeedClickEvent.GoToBucket(
                            feedInfo.bucketId,
                            feedInfo.writerId
                        )
                    )
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
                clickEvent.invoke(FeedClickEvent.Scrap(feedInfo.bucketId))
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

@Composable
@Preview
private fun FeedCardViewPreview() {
    FeedCardView(
        feedInfo = UIFeedInfo(
            bucketId = "1",
            writerId = "11",
            profileImage = null,
            badgeImage = "",
            titlePosition = "나는야 주스될거야",
            nickName = "Sean Barker",
            images = listOf("https://picsum.photos/200/300"),
            isProcessing = true,
            title = "dolorum",
            liked = false,
            likeCount = 7367,
            commentCount = 3546,
            isScraped = false
        ),
        clickEvent = {}
    )
}