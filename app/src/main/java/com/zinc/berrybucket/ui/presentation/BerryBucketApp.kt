package com.zinc.berrybucket.ui.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.zinc.berrybucket.model.BucketSelected
import com.zinc.berrybucket.model.DetailType
import com.zinc.berrybucket.model.MyTabType
import com.zinc.berrybucket.model.WriteInfo1
import com.zinc.berrybucket.ui.presentation.BucketDestinations.BUCKET_COMMENT_REPORT
import com.zinc.berrybucket.ui.presentation.BucketDestinations.REPORT_INFO
import com.zinc.berrybucket.ui.presentation.MainDestinations.MY_SEARCH
import com.zinc.berrybucket.ui.presentation.SearchDestinations.GO_TO_SEARCH
import com.zinc.berrybucket.ui.presentation.WriteDestinations.GO_TO_WRITE1
import com.zinc.berrybucket.ui.presentation.WriteDestinations.GO_TO_WRITE2
import com.zinc.berrybucket.ui.presentation.WriteDestinations.WRITE_INFO
import com.zinc.berrybucket.ui.presentation.detail.screen.CloseDetailLayer
import com.zinc.berrybucket.ui.presentation.detail.screen.OpenDetailLayer
import com.zinc.berrybucket.ui.presentation.home.HomeBottomBar
import com.zinc.berrybucket.ui.presentation.home.HomeSections
import com.zinc.berrybucket.ui.presentation.home.addHomeGraph
import com.zinc.berrybucket.ui.presentation.report.ReportScreen
import com.zinc.berrybucket.ui.presentation.search.SearchScreen
import com.zinc.berrybucket.ui.presentation.write.WriteScreen1
import com.zinc.berrybucket.ui.presentation.write.WriteScreen2
import com.zinc.berrybucket.ui_my.BottomSheetScreenType
import com.zinc.berrybucket.util.getRequiredSerializableExtra
import com.zinc.common.models.ReportInfo
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BerryBucketApp(
    action: (ActionWithActivity) -> Unit
) {
    MaterialTheme {
        val coroutineScope = rememberCoroutineScope()
        val appState = rememberBerryBucketkAppState()
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
                }
            }
        }
        ModalBottomSheetLayout(
            sheetState = bottomSheetScaffoldState,
            sheetContent = {
                Box(Modifier.defaultMinSize(minHeight = 1.dp)) {
                    com.zinc.berrybucket.ui_my.MyBottomSheetScreen(
                        currentScreen = currentBottomSheet,
                        isNeedToBottomSheetOpen = {
                            isNeedToBottomSheetOpen.invoke(it)
                        })
                }

            },
            sheetShape = if (currentBottomSheet is com.zinc.berrybucket.ui_my.BottomSheetScreenType.FilterScreen) RoundedCornerShape(
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
                                navigateToRoute = appState::navigateToBottomBarRoute
                            ) {
                                appState.navigateToWrite1(
                                    appState.navController.currentBackStackEntry!!,
                                    WriteInfo1()
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

                        addHomeGraph(onBucketSelected = { selected, nav ->
                            when (selected) {
                                is BucketSelected.GoToDetailBucket -> {
                                    if (selected.bucketInfo.detailType == DetailType.MY_CLOSE) {
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
                        }, onSearchEvent = { event, nav ->
                            when (event) {
                                SearchEvent.GoToSearch -> {
                                    appState.navigateToSearch(nav)
                                }
                            }
                        }, bottomSheetClicked = { event, nav ->
                            if (event is com.zinc.berrybucket.ui_my.BottomSheetScreenType.SearchScreen) {
                                appState.navigateToMySearch(event.selectTab, nav)
                            } else {
                                currentBottomSheet = event
                                isNeedToBottomSheetOpen.invoke(true)
                            }
                        })

                        homeSearchNavGraph(backPress = appState::backPress)

                        berryBucketNavGraph(
                            goToBucketDetailEvent = { eventInfo, nav ->
                                when (eventInfo) {
                                    is GoToBucketDetailEvent.GoToCommentReport -> {
                                        appState.navigateToCommentReport(eventInfo.reportInfo, nav)
                                    }
                                }
                            }, backPress = appState::backPress
                        )
                        bucketNavGraph(backPress = appState::backPress)
                        searchNavGraph(backPress = appState::backPress)
                        writeNavGraph1(action = { actionType -> action(actionType) },
                            backPress = {
                                appState.backPress()
                            }, goToNextWrite = { nav, info ->
                                appState.navigateToWrite2(nav, info)
                            })
                        writeNavGraph2(backPress = { nav, info ->
                            appState.navigateToWrite1(nav, info)
                        })
                    }
                }
            }
        }

    }
}


object MainDestinations {
    const val HOME_ROUTE = "home"
    const val CLOSE_BUCKET_DETAIL = "close_bucket_detail"
    const val OPEN_BUCKET_DETAIL = "open_bucket_detail"
    const val BUCKET_ID_KEY = "bucketId"
    const val MY_SEARCH = "my_search"
}

object BucketDestinations {
    const val BUCKET_COMMENT_REPORT = "bucket_comment_report"
    const val REPORT_INFO = "report_info"
}

object SearchDestinations {
    const val GO_TO_SEARCH = "go_to_search"
}

object WriteDestinations {
    const val GO_TO_WRITE1 = "go_to_write1"
    const val GO_TO_WRITE2 = "go_to_write2"
    const val WRITE_INFO = "write_info"
}

sealed class SearchEvent {
    object GoToSearch : SearchEvent()
}

sealed class WriteEvent {
    object GoToWrite : WriteEvent()
}

sealed class GoToBucketDetailEvent {
    data class GoToCommentReport(val reportInfo: ReportInfo) : GoToBucketDetailEvent()
}

private fun NavGraphBuilder.berryBucketNavGraph(
    goToBucketDetailEvent: (GoToBucketDetailEvent, NavBackStackEntry) -> Unit, backPress: () -> Unit
) {
    composable(
        "${MainDestinations.OPEN_BUCKET_DETAIL}/{${MainDestinations.BUCKET_ID_KEY}}",
        arguments = listOf(navArgument(MainDestinations.BUCKET_ID_KEY) {
            type = NavType.StringType
        })
    ) { backStackEntry ->
        val arguments = requireNotNull(backStackEntry.arguments)
        val detailId = arguments.getString(MainDestinations.BUCKET_ID_KEY) ?: ""
        OpenDetailLayer(
            detailId = detailId, goToEvent = {
                when (it) {
                    is GoToBucketDetailEvent.GoToCommentReport -> {
                        goToBucketDetailEvent.invoke(
                            GoToBucketDetailEvent.GoToCommentReport(it.reportInfo), backStackEntry
                        )
                    }
                }
            }, backPress = backPress
        )
    }
    composable(
        "${MainDestinations.CLOSE_BUCKET_DETAIL}/{${MainDestinations.BUCKET_ID_KEY}}",
        arguments = listOf(navArgument(MainDestinations.BUCKET_ID_KEY) {
            type = NavType.StringType
        })
    ) { backStackEntry ->
        val arguments = requireNotNull(backStackEntry.arguments)
        val detailId = arguments.getString(MainDestinations.BUCKET_ID_KEY) ?: ""
        CloseDetailLayer(detailId, backPress)
    }
}

private fun NavGraphBuilder.homeSearchNavGraph(backPress: () -> Unit) {
    navigation(
        route = MainDestinations.HOME_ROUTE, startDestination = MY_SEARCH
    ) {
        composable(MY_SEARCH) { entry ->
            val arguments = requireNotNull(entry.arguments)
            val selectedTab = arguments.getRequiredSerializableExtra<MyTabType>(MY_SEARCH)
            com.zinc.berrybucket.ui_my.SearchBottomView(
                tab = selectedTab,
                isNeedToBottomSheetOpen = {
                    backPress()
                }
            )
        }
    }
}

private fun NavGraphBuilder.bucketNavGraph(
    backPress: () -> Unit
) {
    navigation(
        route = MainDestinations.OPEN_BUCKET_DETAIL, startDestination = BUCKET_COMMENT_REPORT
    ) {
        composable(BUCKET_COMMENT_REPORT) { entry ->
            val arguments = requireNotNull(entry.arguments)
            val reportInfo = arguments.getRequiredSerializableExtra<ReportInfo>(REPORT_INFO)
            ReportScreen(reportInfo = reportInfo, backPress = backPress)
        }
    }
}

private fun NavGraphBuilder.searchNavGraph(
    backPress: () -> Unit
) {
    composable(GO_TO_SEARCH) {
        SearchScreen {
            backPress()
        }
    }
}

private fun NavGraphBuilder.writeNavGraph1(
    action: (ActionWithActivity) -> Unit,
    backPress: () -> Unit,
    goToNextWrite: (NavBackStackEntry, WriteInfo1) -> Unit
) {
    composable(GO_TO_WRITE1) {
        val arguments = requireNotNull(it.arguments)
        val writeInfo1 = arguments.getRequiredSerializableExtra<WriteInfo1>(WRITE_INFO)

        WriteScreen1(
            action = { actionType -> action(actionType) },
            goToBack = { backPress() },
            goToNext = { info ->
                goToNextWrite(it, info)
            },
            writeInfo1 = writeInfo1
        )
    }
}

private fun NavGraphBuilder.writeNavGraph2(
    backPress: (NavBackStackEntry, WriteInfo1) -> Unit
) {

    composable(GO_TO_WRITE2) { nav ->
        val arguments = requireNotNull(nav.arguments)
        val writeInfo1 = arguments.getRequiredSerializableExtra<WriteInfo1>(WRITE_INFO)
        WriteScreen2(
            writeInfo1 = writeInfo1,
            goToBack = { newInfo -> backPress(nav, newInfo) },
            goToAddBucket = {})
    }
}