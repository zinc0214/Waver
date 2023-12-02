package com.zinc.berrybucket.ui.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.zinc.berrybucket.model.HomeItemSelected
import com.zinc.berrybucket.model.MySearchClickEvent
import com.zinc.berrybucket.ui.design.theme.BaseTheme
import com.zinc.berrybucket.ui.presentation.home.HomeBottomBar
import com.zinc.berrybucket.ui.presentation.home.HomeSections
import com.zinc.berrybucket.ui.presentation.model.ActionWithActivity
import com.zinc.berrybucket.ui_more.models.MoreItemType.ALARM
import com.zinc.berrybucket.ui_more.models.MoreItemType.APP_INFO
import com.zinc.berrybucket.ui_more.models.MoreItemType.BLOCK
import com.zinc.berrybucket.ui_more.models.MoreItemType.LOGOUT
import com.zinc.berrybucket.ui_more.models.MoreItemType.PROFILE
import com.zinc.berrybucket.ui_more.models.MoreItemType.QNA
import com.zinc.berrybucket.ui_my.BottomSheetScreenType
import com.zinc.berrybucket.ui_my.model.MyTopEvent
import com.zinc.berrybucket.util.nav.CloseBucketDetailEvent
import com.zinc.berrybucket.util.nav.OpenBucketDetailEvent
import com.zinc.berrybucket.util.nav.SearchEvent
import com.zinc.berrybucket.util.nav.alarmNavGraph
import com.zinc.berrybucket.util.nav.closeBucketDetailNavGraph
import com.zinc.berrybucket.util.nav.goToOtherHomeNavGraph
import com.zinc.berrybucket.util.nav.homeCategoryBucketListNavGraph
import com.zinc.berrybucket.util.nav.homeCategoryEditNavGraph
import com.zinc.berrybucket.util.nav.homeFeed
import com.zinc.berrybucket.util.nav.homeMore
import com.zinc.berrybucket.util.nav.homeMy
import com.zinc.berrybucket.util.nav.homeMyBucketSearch
import com.zinc.berrybucket.util.nav.homeSearch
import com.zinc.berrybucket.util.nav.moreAlarmNavGraph
import com.zinc.berrybucket.util.nav.moreAppInfoNavGraph
import com.zinc.berrybucket.util.nav.moreBlockNavGraph
import com.zinc.berrybucket.util.nav.moreProfileNavGraph
import com.zinc.berrybucket.util.nav.myFollowerListNavGraph
import com.zinc.berrybucket.util.nav.myFollowerSettingNavGraph
import com.zinc.berrybucket.util.nav.myFollowingListNavGraph
import com.zinc.berrybucket.util.nav.myFollowingSettingNavGraph
import com.zinc.berrybucket.util.nav.openBucketDetailNavGraph
import com.zinc.berrybucket.util.nav.openBucketReportNavGraph
import com.zinc.berrybucket.util.nav.searchDirectNavGraph
import com.zinc.berrybucket.util.nav.writeNavGraph
import com.zinc.common.models.ExposureStatus

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BerryBucketApp(
    action: (ActionWithActivity) -> Unit
) {
    BaseTheme {
        val appState = rememberBerryBucketAppState()
        val shownBottomSheet = remember {
            mutableStateOf(false)
        }

//        BackHandler {
//            coroutineScope.launch {
//                if (shownBottomSheet.t) {
//                    bottomSheetScaffoldState.hide() // will trigger the LaunchedEffect
//                } else {
//                    action.invoke(ActionWithActivity.AppFinish)
//                }
//            }
//        }


        Column {
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
                            appState.navigateToWrite(
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
                    homeFeed(onFeedClicked = { id, nav ->
                        appState.navigateToOpenBucketDetail(id, false, nav)
                    })
                    homeSearch(
                        onSearchEvent = { event, nav ->
                            when (event) {
                                SearchEvent.GoToSearch -> {
                                    appState.navigateToSearch(nav)
                                }

                                is SearchEvent.GoToOpenBucket -> {
                                    appState.navigateToOpenBucketDetail(event.id, false, nav)
                                }

                                is SearchEvent.GoToOtherUser -> {
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
                                QNA -> {}
                                APP_INFO -> {
                                    appState.navigateToMoreAppInfo(nav)
                                }

                                LOGOUT -> {}
                                PROFILE -> appState.navigateToMoreProfileSetting(nav)
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
                                            selected.bucketInfo.id, true, nav
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
                            if (event is BottomSheetScreenType.SearchScreen) {
                                appState.navigateToMySearch(event.selectTab, nav)
                            } else if (event is BottomSheetScreenType.FilterScreen) {
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
                                    appState.navigateToOpenBucketDetail(event.id, true, nav)
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
                                appState.navigateToOpenBucketDetail(id, true, nav)
                            }

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
                                is OpenBucketDetailEvent.CommentReport -> {
                                    appState.navigateToCommentReport(eventInfo.reportInfo, nav)
                                }

                                is OpenBucketDetailEvent.Update -> {
                                    appState.navigateToWrite(
                                        eventInfo.info.bucketId ?: "NoId",
                                        appState.navController.currentBackStackEntry!!
                                    )
                                }

                                is OpenBucketDetailEvent.BucketReport -> {

                                }
                            }
                        },
                        backPress = appState::backPress
                    )

                    closeBucketDetailNavGraph(
                        goToBucketDetailEvent = { eventInfo, nav ->
                            when (eventInfo) {
                                is CloseBucketDetailEvent.Update -> {
                                    appState.navigateToWrite(
                                        eventInfo.info.bucketId ?: "NoId",
                                        appState.navController.currentBackStackEntry!!
                                    )
                                }
                            }
                        },
                        backPress = appState::backPress
                    )
                    openBucketReportNavGraph(backPress = appState::backPress)
                    searchDirectNavGraph(
                        backPress = appState::backPress, searchEvent =
                        { event, nav ->
                            when (event) {
                                SearchEvent.GoToSearch -> {
                                    appState.navigateToSearch(nav)
                                }

                                is SearchEvent.GoToOpenBucket -> {
                                    appState.navigateToOpenBucketDetail(event.id, false, nav)
                                }

                                is SearchEvent.GoToOtherUser -> {
                                    appState.navigateToOtherHome(nav, event.id)
                                }
                            }
                        })
                    writeNavGraph(action = { actionType -> action(actionType) },
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
                    goToOtherHomeNavGraph(
                        backPress = appState::backPress,
                    )
                }
            }
        }
    }
}

