package com.zinc.waver.ui_search.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.zinc.waver.ui.design.theme.Gray1
import com.zinc.waver.ui.design.theme.Gray10
import com.zinc.waver.ui.design.theme.Gray2
import com.zinc.waver.ui.design.theme.Gray3
import com.zinc.waver.ui.design.theme.Gray7
import com.zinc.waver.ui.presentation.component.IconButton
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.ui_search.R
import com.zinc.waver.ui_search.model.SearchActionEvent
import com.zinc.waver.ui_search.model.SearchGoToEvent
import com.zinc.waver.ui_search.model.SearchResultItems
import com.zinc.waver.ui_search.model.UserItem

@Composable
fun SearchResultView(
    resultItems: SearchResultItems,
    modifier: Modifier,
    goToEvent: (SearchGoToEvent) -> Unit,
    actionEvent: (SearchActionEvent) -> Unit
) {
    var needBucketMoreButtonShow by remember {
        mutableStateOf(resultItems.bucketItems.size > 3)
    }
    var needUserMoreButtonShow by remember {
        mutableStateOf(resultItems.userItems.size > 3)
    }
    var bucketVisibleItem by remember {
        mutableStateOf(if (needBucketMoreButtonShow) resultItems.bucketItems.take(3) else resultItems.bucketItems)
    }
    var userVisibleItem by remember {
        mutableStateOf(if (needUserMoreButtonShow) resultItems.userItems.take(3) else resultItems.userItems)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp)
    ) {

        bucketVisibleItem.forEach {
            RecommendBucketItemView(item = it,
                bucketClicked = { bucketId, userId ->
                    goToEvent.invoke(SearchGoToEvent.GoToOpenBucket(bucketId, userId))
                })
        }

        if (needBucketMoreButtonShow) {
            ShowMoreButton {
                bucketVisibleItem = resultItems.bucketItems
                needBucketMoreButtonShow = false
            }
        }

        if (resultItems.bucketItems.isNotEmpty() && resultItems.userItems.isNotEmpty()) {
            Divider(
                color = Gray3, modifier = Modifier.padding(
                    top = if (needBucketMoreButtonShow) 0.dp else 28.dp, bottom = 15.dp
                )
            )
        }

        userVisibleItem.forEach {
            SearchUserItemView(
                item = it,
                userClicked = { id ->
                    goToEvent.invoke(SearchGoToEvent.GoToOtherUser(id))
                },
                actionEvent = { event ->
                    actionEvent(event)
                }

            )
        }

        if (needUserMoreButtonShow) {
            ShowMoreButton {
                userVisibleItem = resultItems.userItems
                needUserMoreButtonShow = false
            }
        }
    }
}

@Composable
private fun ShowMoreButton(buttonClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth()
            .background(color = Gray2, shape = RoundedCornerShape(2.dp))
            .clip(shape = RoundedCornerShape(2.dp))
            .clickable {
                buttonClicked()
            }, contentAlignment = Alignment.Center
    ) {
        MyText(
            text = stringResource(id = R.string.showMore),
            color = Gray7,
            fontSize = dpToSp(14.dp),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
fun SearchUserItemView(
    modifier: Modifier = Modifier,
    item: UserItem,
    userClicked: (String) -> Unit,
    actionEvent: (SearchActionEvent) -> Unit
) {
    Card(
        backgroundColor = Gray1,
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, Gray3),
        elevation = 0.dp,
        modifier = modifier
            .padding(top = 12.dp)
            .clickable {
                userClicked(item.userId)
            }
    ) {

        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (profileImage, nickNameView, followButton) = createRefs()

            Image(
                painter = painterResource(id = com.zinc.waver.ui_common.R.drawable.kakao),
                contentDescription = stringResource(
                    id = R.string.searchProfileImage
                ),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .size(32.dp)
                    .aspectRatio(1f)
                    .clip(shape = RoundedCornerShape(12.dp))
                    .constrainAs(profileImage) {
                        linkTo(
                            top = parent.top,
                            bottom = parent.bottom,
                            topMargin = 16.dp,
                            bottomMargin = 16.dp
                        )
                        start.linkTo(parent.start)
                    })

            MyText(
                text = item.nickName,
                fontSize = dpToSp(14.dp),
                color = Gray10,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                textAlign = TextAlign.Start,
                modifier = Modifier.constrainAs(nickNameView) {
                    linkTo(
                        top = parent.top,
                        bottom = parent.bottom,
                        topMargin = 22.dp,
                        bottomMargin = 22.dp
                    )
                    linkTo(
                        start = profileImage.end,
                        end = followButton.start,
                        startMargin = 12.dp,
                        endMargin = 12.dp
                    )
                    width = Dimension.fillToConstraints
                })

            IconButton(onClick = {
                actionEvent(SearchActionEvent.RequestFollow(item.userId, item.isFollowed))
            },
                image = if (item.isFollowed) R.drawable.btn_32_following else R.drawable.btn_32_not_follow,
                contentDescription = stringResource(id = com.zinc.waver.ui_common.R.string.copy),
                modifier = Modifier
                    .constrainAs(followButton) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
                    .padding(end = 12.dp)
                    .size(32.dp))
        }

    }
}


