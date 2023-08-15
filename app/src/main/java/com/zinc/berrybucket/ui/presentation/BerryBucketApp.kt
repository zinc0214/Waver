package com.zinc.berrybucket.ui.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import com.zinc.berrybucket.model.BucketSelected
import com.zinc.berrybucket.model.WriteTotalInfo
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
import com.zinc.berrybucket.ui_my.MyBottomSheetScreen
import com.zinc.berrybucket.ui_my.model.MyTopEvent
import com.zinc.berrybucket.util.nav.GoToBucketDetailEvent
import com.zinc.berrybucket.util.nav.SearchEvent
import com.zinc.berrybucket.util.nav.alarmNavGraph
import com.zinc.berrybucket.util.nav.bucketDetailNavGraph
import com.zinc.berrybucket.util.nav.bucketNavGraph
import com.zinc.berrybucket.util.nav.homeCategoryEditNavGraph
import com.zinc.berrybucket.util.nav.homeFeed
import com.zinc.berrybucket.util.nav.homeMore
import com.zinc.berrybucket.util.nav.homeMy
import com.zinc.berrybucket.util.nav.homeSearch
import com.zinc.berrybucket.util.nav.homeSearchNavGraph
import com.zinc.berrybucket.util.nav.moreAlarmNavGraph
import com.zinc.berrybucket.util.nav.moreAppInfoNavGraph
import com.zinc.berrybucket.util.nav.moreBlockNavGraph
import com.zinc.berrybucket.util.nav.moreProfileNavGraph
import com.zinc.berrybucket.util.nav.myFollowerListNavGraph
import com.zinc.berrybucket.util.nav.myFollowerSettingNavGraph
import com.zinc.berrybucket.util.nav.myFollowingListNavGraph
import com.zinc.berrybucket.util.nav.myFollowingSettingNavGraph
import com.zinc.berrybucket.util.nav.searchNavGraph
import com.zinc.berrybucket.util.nav.writeNavGraph1
import com.zinc.berrybucket.util.nav.writeNavGraph2
import com.zinc.common.models.ExposureStatus
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BerryBucketApp(
    action: (ActionWithActivity) -> Unit
) {
    BaseTheme {
        val coroutineScope = rememberCoroutineScope()
        val appState = rememberBerryBucketAppState()
        val bottomSheetScaffoldState = rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden, skipHalfExpanded = true
        )
        var currentBottomSheet: BottomSheetScreenType? by remember { mutableStateOf(null) }
        val isNeedToBottomSheetOpen: (Boolean) -> Unit = {
            coroutineScope.launch {
                if (it) {
                    bottomSheetScaffoldState.show()
                } else {
                    bottomSheetScaffoldState.hide()
                    currentBottomSheet = null
                }
            }
        }

        LaunchedEffect(bottomSheetScaffoldState.isVisible) {
            if (!bottomSheetScaffoldState.isVisible) {
                currentBottomSheet = null
            }
        }


        BackHandler {
            coroutineScope.launch {
                if (bottomSheetScaffoldState.isVisible) {
                    bottomSheetScaffoldState.hide() // will trigger the LaunchedEffect
                } else {
                    appState.backPress()
                }
            }
        }


        ModalBottomSheetLayout(
            sheetState = bottomSheetScaffoldState,
            sheetContent = {
                Box(Modifier.defaultMinSize(minHeight = 1.dp)) {
                    MyBottomSheetScreen(
                        currentScreen = currentBottomSheet,
                        isNeedToBottomSheetOpen = {
                            isNeedToBottomSheetOpen.invoke(it)
                        })
                }

            },
            sheetShape = if (currentBottomSheet is BottomSheetScreenType.FilterScreen) RoundedCornerShape(
                topEnd = 16.dp, topStart = 16.dp
            ) else RoundedCornerShape(0.dp)
        ) {
            Column {
                Scaffold(
                    bottomBar = {
                        if (appState.shouldShowBottomBar) {
                            HomeBottomBar(
                                tabs = appState.bottomBarTabs,
                                currentRoute = appState.currentRoute!!,
                                navigateToRoute = {
                                    appState.navigateToBottomBarRoute(it)
                                    appState.currentHomeRoute.value = it
                                }
                            ) {
                                appState.navigateToWrite1(
                                    WriteTotalInfo(),
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
                            appState.navigateToOpenBucketDetail(id, nav)
                        })
                        homeSearch(
                            onSearchEvent = { event, nav ->
                                when (event) {
                                    SearchEvent.GoToSearch -> {
                                        appState.navigateToSearch(nav)
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
                                    is BucketSelected.GoToDetailBucket -> {
                                        if (selected.bucketInfo.exposureStatues == ExposureStatus.PRIVATE) {
                                            appState.navigateToCloseBucketDetail(
                                                selected.bucketInfo.id, nav
                                            )
                                        } else {
                                            appState.navigateToOpenBucketDetail(
                                                selected.bucketInfo.id, nav
                                            )
                                        }
                                    }
                                }
                            },

                            bottomSheetClicked = { event, nav ->
                                if (event is BottomSheetScreenType.SearchScreen) {
                                    appState.navigateToMySearch(event.selectTab, nav)
                                } else {
                                    currentBottomSheet = event
                                    isNeedToBottomSheetOpen.invoke(true)
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


                        homeSearchNavGraph(backPress = appState::backPress)

                        homeCategoryEditNavGraph(backPress = appState::backPress)

                        myFollowingListNavGraph(
                            backPress = appState::backPress,
                            goToSetting = { nav ->
                                appState.navigateToFollowingSettingList(from = nav)
                            })

                        myFollowingSettingNavGraph(backPress = appState::backPress)

                        myFollowerListNavGraph(backPress = appState::backPress, goToSetting = {
                            appState.navigateToFollowerSettingList(it)
                        })

                        myFollowerSettingNavGraph(backPress = appState::backPress)

                        bucketDetailNavGraph(
                            goToBucketDetailEvent = { eventInfo, nav ->
                                when (eventInfo) {
                                    is GoToBucketDetailEvent.GoToCommentReport -> {
                                        appState.navigateToCommentReport(eventInfo.reportInfo, nav)
                                    }

                                    is GoToBucketDetailEvent.GoToUpdate -> {
                                        appState.navigateToWrite1(eventInfo.info, nav)
                                    }
                                }
                            },
                            backPress = appState::backPress
                        )
                        bucketNavGraph(backPress = appState::backPress)
                        searchNavGraph(backPress = appState::backPress)
                        writeNavGraph1(action = { actionType -> action(actionType) },
                            backPress = {
                                appState.navigateToBottomBarRoute(appState.currentHomeRoute.value)
                            }, goToNextWrite = { nav, info ->
                                appState.navigateToWrite2(nav, info)
                            }, goToAddCategory = {
                                appState.navigateToCategoryEdit(it)
                            })
                        writeNavGraph2(
                            backPress = { nav, info ->
                                appState.navigateToWrite1(info, nav)
                            },
                            goToHome = {
                                appState.navigateToBottomBarRoute(appState.currentHomeRoute.value)
                            })
                        alarmNavGraph(backPress = appState::backPress)
                        moreAlarmNavGraph(backPress = appState::backPress)
                        moreBlockNavGraph(backPress = appState::backPress)
                        moreProfileNavGraph(
                            backPress = appState::backPress,
                            imageUpdateButtonClicked = { type, fail, success ->
                                action.invoke(
                                    ActionWithActivity.AddImage(
                                        type,
                                        fail
                                    ) { info -> success(info.path) })
                            }
                        )
                        moreAppInfoNavGraph(
                            backPress = appState::backPress,
                            moreItemClicked = {

                            }
                        )
                    }
                }
            }
        }

    }
}

