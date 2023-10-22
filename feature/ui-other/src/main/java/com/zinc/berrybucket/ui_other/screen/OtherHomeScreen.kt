package com.zinc.berrybucket.ui_other.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.zinc.berrybucket.ui.design.theme.Gray1
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
            Column {

            }
        }
    }
}