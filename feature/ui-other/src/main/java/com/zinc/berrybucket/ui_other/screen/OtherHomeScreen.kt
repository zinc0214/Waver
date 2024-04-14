package com.zinc.berrybucket.ui_other.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.zinc.berrybucket.ui.design.theme.Gray1
import com.zinc.berrybucket.ui.design.theme.Gray2
import com.zinc.berrybucket.ui_other.component.OtherBucketListView
import com.zinc.berrybucket.ui_other.component.OtherHomeProfile
import com.zinc.berrybucket.ui_other.model.OtherHomeEvent
import com.zinc.berrybucket.ui_other.viewmodel.OtherViewModel
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@Composable
fun OtherHomeScreen(
    userId: String,
    otherHomeEvent: (OtherHomeEvent) -> Unit
) {

    val viewModel: OtherViewModel = hiltViewModel()
    val profileHomeDataAsState by viewModel.otherHomeData.observeAsState()

    val profileHomeData = remember { mutableStateOf(profileHomeDataAsState) }

    if (profileHomeDataAsState == null) {
        viewModel.loadOtherInfo(userId)
    }

    LaunchedEffect(key1 = profileHomeDataAsState) {
        profileHomeDataAsState?.let {
            profileHomeData.value = it
        }
    }


    profileHomeData.value?.let { homeData ->
        Column(modifier = Modifier.fillMaxSize()) {
            rememberSystemUiController().setSystemBarsColor(Gray1)
            CollapsingToolbarScaffold(
                modifier = Modifier.fillMaxSize(),
                state = rememberCollapsingToolbarScaffoldState(),
                scrollStrategy = ScrollStrategy.EnterAlways,
                toolbar = {
                    Column {
                        OtherHomeProfile(
                            profileInfo = homeData.profile,
                            changeFollowStatus = {},
                            goToBack = {
                                otherHomeEvent.invoke(OtherHomeEvent.GoToBack)
                            }
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
                        bucketList = homeData.bucketList,
                        itemClicked = { bucketId ->
                            otherHomeEvent.invoke(OtherHomeEvent.GoToOtherBucket(bucketId, userId))
                        }
                    )
                }
            }
        }
    }
}