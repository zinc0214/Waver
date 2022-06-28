package com.zinc.berrybucket.ui.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.zinc.berrybucket.model.DetailType
import com.zinc.berrybucket.model.UIBucketInfoSimple
import com.zinc.berrybucket.ui.presentation.BucketDestinations.BUCKET_COMMENT_REPORT
import com.zinc.berrybucket.ui.presentation.BucketDestinations.REPORT_INFO
import com.zinc.berrybucket.ui.presentation.SearchDestinations.GO_TO_SEARCH
import com.zinc.berrybucket.ui.presentation.detail.screen.CloseDetailLayer
import com.zinc.berrybucket.ui.presentation.detail.screen.OpenDetailLayer
import com.zinc.berrybucket.ui.presentation.home.HomeBottomBar
import com.zinc.berrybucket.ui.presentation.home.HomeSections
import com.zinc.berrybucket.ui.presentation.home.addHomeGraph
import com.zinc.berrybucket.ui.presentation.my.BottomSheetScreenType
import com.zinc.berrybucket.ui.presentation.my.MyBottomSheetScreen
import com.zinc.berrybucket.ui.presentation.report.ReportScreen
import com.zinc.berrybucket.ui.presentation.search.SearchScreen
import com.zinc.berrybucket.util.getRequiredSerializableExtra
import com.zinc.common.models.ReportInfo
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BerryBucketApp() {
    MaterialTheme {
        val coroutineScope = rememberCoroutineScope()
        val appState = rememberBerryBucketkAppState()
        val bottomSheetScaffoldState = rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true
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
                    MyBottomSheetScreen(
                        currentScreen = currentBottomSheet,
                        isNeedToBottomSheetOpen = {
                            isNeedToBottomSheetOpen.invoke(it)
                        }
                    )
                }

            },
            sheetShape = if (currentBottomSheet is BottomSheetScreenType.FilterScreen)
                RoundedCornerShape(
                    topEnd = 16.dp,
                    topStart = 16.dp
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
                            )
                        }
                    },
                    scaffoldState = appState.scaffoldState
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
                                            selected.bucketInfo.id,
                                            nav
                                        )
                                    } else {
                                        appState.navigateToOpenBucketDetail(
                                            selected.bucketInfo.id,
                                            nav
                                        )
                                    }
                                }
                            }
                        },
                            onSearchEvent = { event, nav ->
                                when (event) {
                                    SearchEvent.GoToSearch -> {
                                        appState.navigateToSearch(nav)
                                    }
                                }
                            },
                            bottomSheetClicked = {
                                currentBottomSheet = it
                                isNeedToBottomSheetOpen.invoke(true)
                            })

                        berryBucketNavGraph(
                            goToBucketDetailEvent = { eventInfo, nav ->
                                when (eventInfo) {
                                    is GoToBucketDetailEvent.GoToCommentReport -> {
                                        appState.navigateToCommentReport(eventInfo.reportInfo, nav)
                                    }
                                }
                            },
                            backPress = appState::backPress
                        )
                        bucketNavGraph(backPress = appState::backPress)
                        searchNavGraph(backPress = appState::backPress)
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
}

object BucketDestinations {
    const val BUCKET_COMMENT_REPORT = "bucket_comment_report"
    const val REPORT_INFO = "report_info"
}

object SearchDestinations {
    const val GO_TO_SEARCH = "go_to_search"
}

sealed class BucketSelected {
    data class GoToDetailBucket(val bucketInfo: UIBucketInfoSimple) : BucketSelected()
}

sealed class SearchEvent {
    object GoToSearch : SearchEvent()
}

sealed class GoToBucketDetailEvent {
    data class GoToCommentReport(val reportInfo: ReportInfo) : GoToBucketDetailEvent()
}

private fun NavGraphBuilder.berryBucketNavGraph(
    goToBucketDetailEvent: (GoToBucketDetailEvent, NavBackStackEntry) -> Unit,
    backPress: () -> Unit
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
            detailId = detailId,
            goToEvent = {
                when (it) {
                    is GoToBucketDetailEvent.GoToCommentReport -> {
                        goToBucketDetailEvent.invoke(
                            GoToBucketDetailEvent.GoToCommentReport(it.reportInfo),
                            backStackEntry
                        )
                    }
                }
            },
            backPress = backPress
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

private fun NavGraphBuilder.bucketNavGraph(
    backPress: () -> Unit
) {
    navigation(
        route = MainDestinations.OPEN_BUCKET_DETAIL,
        startDestination = BUCKET_COMMENT_REPORT
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