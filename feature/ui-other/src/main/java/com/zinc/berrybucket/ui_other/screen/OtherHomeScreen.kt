package com.zinc.berrybucket.ui_other.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.zinc.berrybucket.ui.design.theme.Gray1
import com.zinc.berrybucket.ui.design.theme.Gray2
import com.zinc.berrybucket.ui_other.component.OtherBucketListView
import com.zinc.berrybucket.ui_other.component.OtherHomeProfile
import com.zinc.domain.models.TopProfile
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@Composable
fun OtherHomeScreen(
    userId: String,
    goToBack: () -> Unit
) {
    val nestedScrollInterop = rememberNestedScrollInteropConnection()

//    val bucketListAsState by viewModel.allBucketItem.observeAsState()
//    val bucketInfo = remember {
//        mutableStateOf(bucketListAsState)
//    }

    val profileInfo = TopProfile(
        name = "다른사람",
        imgUrl = null,
        percent = 0.3f,
        badgeType = null,
        badgeTitle = "반갑다구우",
        bio = "나는다른사람일거야 안녕!",
        followerCount = "10",
        followingCount = "11"
    )

    val list = buildList {
        add(
            OtherBucketInfo(
                title = "테스트1", bucketId = 0, isProgress = true
            )
        )
        add(
            OtherBucketInfo(
                title = "테스트12", bucketId = 1, isProgress = false
            )
        )
        add(
            OtherBucketInfo(
                title = "테스트13", bucketId = 2, isProgress = false
            )
        )
        add(
            OtherBucketInfo(
                title = "테스트13", bucketId = 3, isProgress = false
            )
        )
        add(
            OtherBucketInfo(
                title = "테스트14", bucketId = 4, isProgress = false
            )
        )
        repeat(10) {
            add(
                OtherBucketInfo(
                    title = "테스트$it", bucketId = it + 10, isProgress = true
                )
            )
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        rememberSystemUiController().setSystemBarsColor(Gray1)
        CollapsingToolbarScaffold(
            modifier = Modifier.fillMaxSize(),
            state = rememberCollapsingToolbarScaffoldState(),
            scrollStrategy = ScrollStrategy.EnterAlways,
            toolbar = {
                Column {
                    OtherHomeProfile(
                        profileInfo = profileInfo,
                        isAlreadyFollowed = false,
                        changeFollowStatus = {},
                        goToBack = goToBack
                    )
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .background(Gray2)
                    .fillMaxHeight()
                    .padding(top = 24.dp)
            ) {
                OtherBucketListView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    bucketList = list,
                    itemClicked = {}

                )
            }
        }
    }
}