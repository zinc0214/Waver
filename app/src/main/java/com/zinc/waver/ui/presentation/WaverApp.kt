package com.zinc.waver.ui.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.zinc.common.models.ExposureStatus
import com.zinc.waver.model.HomeItemSelected
import com.zinc.waver.model.MySearchClickEvent
import com.zinc.waver.ui.design.theme.BaseTheme
import com.zinc.waver.ui.design.theme.Gray1
import com.zinc.waver.ui.presentation.home.HomeBottomBar
import com.zinc.waver.ui.presentation.home.HomeSections
import com.zinc.waver.ui.presentation.model.ActionWithActivity
import com.zinc.waver.ui_detail.model.CloseBucketDetailEvent
import com.zinc.waver.ui_detail.model.OpenBucketDetailEvent2
import com.zinc.waver.ui_more.models.MoreItemType
import com.zinc.waver.ui_more.models.MoreItemType.ALARM
import com.zinc.waver.ui_more.models.MoreItemType.APP_INFO
import com.zinc.waver.ui_more.models.MoreItemType.BLOCK
import com.zinc.waver.ui_more.models.MoreItemType.LOGOUT
import com.zinc.waver.ui_more.models.MoreItemType.PROFILE
import com.zinc.waver.ui_more.models.MoreItemType.WAVE_PLUS
import com.zinc.waver.ui_my.BottomSheetScreenType
import com.zinc.waver.ui_my.model.MyTopEvent
import com.zinc.waver.ui_other.model.OtherHomeEvent
import com.zinc.waver.ui_search.model.SearchGoToEvent
import com.zinc.waver.util.nav.alarmNavGraph
import com.zinc.waver.util.nav.closeBucketDetailNavGraph
import com.zinc.waver.util.nav.goToOtherHomeNavGraph
import com.zinc.waver.util.nav.homeCategoryBucketListNavGraph
import com.zinc.waver.util.nav.homeCategoryEditNavGraph
import com.zinc.waver.util.nav.homeFeed
import com.zinc.waver.util.nav.homeMore
import com.zinc.waver.util.nav.homeMy
import com.zinc.waver.util.nav.homeMyBucketSearch
import com.zinc.waver.util.nav.homeSearch
import com.zinc.waver.util.nav.moreAlarmNavGraph
import com.zinc.waver.util.nav.moreAppInfoNavGraph
import com.zinc.waver.util.nav.moreBlockNavGraph
import com.zinc.waver.util.nav.moreProfileNavGraph
import com.zinc.waver.util.nav.myFollowerListNavGraph
import com.zinc.waver.util.nav.myFollowerSettingNavGraph
import com.zinc.waver.util.nav.myFollowingListNavGraph
import com.zinc.waver.util.nav.myFollowingSettingNavGraph
import com.zinc.waver.util.nav.myWaveManageNavGraph
import com.zinc.waver.util.nav.openBucketDetailNavGraph
import com.zinc.waver.util.nav.searchDirectNavGraph
import com.zinc.waver.util.nav.wavePlusGuideNavGraph
import com.zinc.waver.util.nav.writeNavGraph

@Composable
fun WaverApp(
    action: (ActionWithActivity) -> Unit
) {
    BaseTheme {
        val appState = rememberWaverAppState()
        val shownBottomSheet = remember {
            mutableStateOf(false)
        }

        Column(
            modifier = Modifier
                .background(color = Gray1)
                .navigationBarsPadding()
        ) {
            Scaffold(
                bottomBar = {
                    if (appState.shouldShowBottomBar && shownBottomSheet.value.not()) {
                        HomeBottomBar(
                            tabs = appState.bottomBarTabs,
                            currentRoute = appState.currentRoute!!,
                            navigateToRoute = {
                                appState.navigateToBottomBarRoute(it)
                                appState.currentHomeRoute.value = it
                            }
                        ) {
                            appState.navigateToWriteBucket(
                                "NoId",
                                appState.navController.currentBackStackEntry!!
                            )
                        }
                    }
                }, scaffoldState = appState.scaffoldState
            ) { innerPaddingModifier ->
                NavHost(
                    navController = appState.navController,
                    startDestination = HomeSections.MY.route,
                    modifier = Modifier.padding(innerPaddingModifier)
                ) {
                    homeFeed(onFeedClicked = { bucketId, userId, nav ->
                        appState.navigateToOpenBucketDetail(bucketId, userId, false, nav)
                    })
                    homeSearch(
                        onSearchEvent = { event, nav ->
                            when (event) {
                                SearchGoToEvent.GoToSearch -> {
                                    appState.navigateToSearch(nav)
                                }

                                is SearchGoToEvent.GoToOpenBucket -> {
                                    appState.navigateToOpenBucketDetail(
                                        event.bucketId,
                                        event.userId,
                                        false,
                                        nav
                                    )
                                }

                                is SearchGoToEvent.GoToOtherUser -> {
                                    appState.navigateToOtherHome(nav, event.id)
                                }
                            }
                        },
                    )
                    homeMore(
                        moreItemClicked = { type, nav ->
                            when (type) {
                                ALARM -> appState.navigateToMoreAlarmSetting(nav)
                                BLOCK -> appState.navigateToMoreBlockSetting(nav)
                                APP_INFO -> {
                                    appState.navigateToMoreAppInfo(nav)
                                }

                                PROFILE -> appState.navigateToMoreProfileSetting(nav)
                                MoreItemType.MY_WAVE -> appState.navigateToMyWaveManage(nav)
                                WAVE_PLUS -> appState.navigateToWavePlusGuide(nav)
                                MoreItemType.CS_QNA -> action(ActionWithActivity.GoToQNAEmail)
                                LOGOUT -> action(ActionWithActivity.Logout)
                                else -> {
                                    // Do Nothing
                                }
                            }
                        },
                        backPress = appState::backPress
                    )
                    homeMy(
                        onBucketSelected = { selected, nav ->
                            when (selected) {
                                is HomeItemSelected.GoToDetailHomeItem -> {
                                    if (selected.bucketInfo.exposureStatues == ExposureStatus.PRIVATE) {
                                        appState.navigateToCloseBucketDetail(
                                            selected.bucketInfo.id, nav
                                        )
                                    } else {
                                        appState.navigateToOpenBucketDetail(
                                            selected.bucketInfo.id, "NoId", true, nav
                                        )
                                    }
                                }

                                is HomeItemSelected.GoToCategoryBucketList -> {
                                    appState.navigateToCategoryBucketList(
                                        selected.categoryInfo, nav
                                    )
                                }
                            }
                        },

                        bottomSheetClicked = { event, nav ->
                            if (event is BottomSheetScreenType.MyBucketSearchScreen) {
                                appState.navigateToMySearch(event.selectTab, nav)
                            } else if (event is BottomSheetScreenType.MyBucketFilterScreen) {
                                shownBottomSheet.value = event.needToShown
                            }
                        },

                        myTopEvent = { event, nav ->
                            run {
                                when (event) {
                                    MyTopEvent.Alarm -> appState.navigateToAlarm(nav)
                                    MyTopEvent.Follower -> appState.navigateToFollowerList(nav)
                                    MyTopEvent.Following -> appState.navigateToFollowingList(nav)
                                }
                            }
                        },

                        goToCategoryEdit = { nav ->
                            appState.navigateToCategoryEdit(nav)
                        }
                    )


                    homeMyBucketSearch { event, nav ->
                        when (event) {
                            is MySearchClickEvent.BucketItemClicked -> {
                                if (event.isPrivate) {
                                    appState.navigateToCloseBucketDetail(event.id, nav)
                                } else {
                                    appState.navigateToOpenBucketDetail(event.id, "NoId", true, nav)
                                }
                            }

                            is MySearchClickEvent.CategoryItemClicked -> TODO() // 다음은 여기하자!!!!

                            is MySearchClickEvent.CloseClicked -> {
                                shownBottomSheet.value = false
                                appState.backPress()
                            }
                        }

                    }

                    homeCategoryBucketListNavGraph(
                        backPress = appState::backPress,
                        bucketClicked = { id, isPrivate, nav ->
                            if (isPrivate) {
                                appState.navigateToCloseBucketDetail(id, nav)
                            } else {
                                appState.navigateToOpenBucketDetail(id, "NoId", true, nav)
                            }
                        },
                        goToAddBucket = {
                            appState.navigateToWriteBucket("NoId", it)
                        })

                    homeCategoryEditNavGraph(backPress = appState::backPress)

                    myFollowingListNavGraph(
                        backPress = appState::backPress,
                        goToSetting = { nav ->
                            appState.navigateToFollowingSettingList(from = nav)
                        },
                        goToOtherHome = { nav, id ->
                            appState.navigateToOtherHome(nav, id)
                        })

                    myFollowingSettingNavGraph(backPress = appState::backPress,
                        goToOtherHome = { nav, id ->
                            appState.navigateToOtherHome(nav, id)
                        })

                    myFollowerListNavGraph(
                        backPress = appState::backPress,
                        goToSetting = {
                            appState.navigateToFollowerSettingList(it)
                        },
                        goToOtherHome = { nav, id ->
                            appState.navigateToOtherHome(nav, id)
                        })

                    myFollowerSettingNavGraph(backPress = appState::backPress,
                        goToOtherHome = { nav, id ->
                            appState.navigateToOtherHome(nav, id)
                        })

                    openBucketDetailNavGraph(
                        goToBucketDetailEvent = { eventInfo, nav ->
                            when (eventInfo) {
                                is OpenBucketDetailEvent2.Update -> {
                                    appState.navigateToWriteBucket(
                                        eventInfo.info.bucketId ?: "NoId",
                                        appState.navController.currentBackStackEntry!!
                                    )
                                }

                                is OpenBucketDetailEvent2.GoToOtherProfile -> {
                                    appState.navigateToOtherHome(nav, eventInfo.id)
                                }
                            }
                        },
                        backPress = appState::backPress
                    )

                    closeBucketDetailNavGraph(
                        goToBucketDetailEvent = { eventInfo, nav ->
                            when (eventInfo) {
                                is CloseBucketDetailEvent.Update -> {
                                    appState.navigateToWriteBucket(
                                        eventInfo.info.bucketId ?: "NoId",
                                        appState.navController.currentBackStackEntry!!
                                    )
                                }
                            }
                        },
                        backPress = appState::backPress
                    )
                    searchDirectNavGraph(
                        backPress = appState::backPress, searchEvent =
                        { event, nav ->
                            when (event) {
                                SearchGoToEvent.GoToSearch -> {
                                    appState.navigateToSearch(nav)
                                }

                                is SearchGoToEvent.GoToOpenBucket -> {
                                    appState.navigateToOpenBucketDetail(
                                        event.bucketId,
                                        event.userId,
                                        false,
                                        nav
                                    )
                                }

                                is SearchGoToEvent.GoToOtherUser -> {
                                    appState.navigateToOtherHome(nav, event.id)
                                }
                            }
                        })
                    writeNavGraph(
                        action = { actionType ->
                            action(actionType)
                        },
                        backPress = {
                            appState.navigateToBottomBarRoute(appState.currentHomeRoute.value)
                        },
                        goToAddCategory = {
                            appState.navigateToCategoryEdit(it)
                        },
                        goToHome = {
                            appState.navigateToBottomBarRoute(appState.currentHomeRoute.value)
                        })
                    alarmNavGraph(backPress = appState::backPress)
                    moreAlarmNavGraph(backPress = appState::backPress)
                    moreBlockNavGraph(backPress = appState::backPress)
                    moreProfileNavGraph(
                        backPress = appState::backPress,
                        action = {
                            action(it)
                        }
                    )
                    moreAppInfoNavGraph(
                        backPress = appState::backPress,
                        moreItemClicked = {

                        }
                    )
                    goToOtherHomeNavGraph { event, from ->
                        when (event) {
                            is OtherHomeEvent.GoToBack -> appState.backPress()
                            is OtherHomeEvent.GoToOtherBucket -> appState.navigateToOpenBucketDetail(
                                bucketId = event.bucketId.toString(),
                                writerId = event.writerId,
                                isMine = false,
                                from = from
                            )
                        }
                    }
                    myWaveManageNavGraph(backPress = appState::backPress)
                    wavePlusGuideNavGraph(
                        backPress = appState::backPress,
                        inAppBillingShow = { action.invoke(ActionWithActivity.InAppBilling(it)) })
                }
            }
        }
    }
}

