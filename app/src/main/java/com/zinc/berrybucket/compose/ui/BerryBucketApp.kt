package com.zinc.berrybucket.compose.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.zinc.berrybucket.compose.ui.detail.CloseDetailLayer
import com.zinc.berrybucket.compose.ui.detail.OpenDetailLayer
import com.zinc.berrybucket.compose.ui.home.HomeBottomBar
import com.zinc.berrybucket.compose.ui.home.HomeSections
import com.zinc.berrybucket.compose.ui.home.addHomeGraph
import com.zinc.berrybucket.compose.ui.my.BottomSheetScreenType
import com.zinc.berrybucket.compose.ui.my.MyBottomSheetScreen
import com.zinc.berrybucket.model.DetailType
import com.zinc.berrybucket.model.UIBucketInfoSimple
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BerryBucketApp() {
    MaterialTheme {
        val coroutineScope = rememberCoroutineScope()
        val appState = rememberBerryBucketkAppState()
        val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
        var currentBottomSheet: BottomSheetScreenType? by remember { mutableStateOf(null) }
        val isNeedToBottomSheetOpen: (Boolean) -> Unit = {
            coroutineScope.launch {
                if (it) {
                    bottomSheetScaffoldState.bottomSheetState.expand()
                } else {
                    bottomSheetScaffoldState.bottomSheetState.collapse()
                }
            }
        }
        BottomSheetScaffold(
            scaffoldState = bottomSheetScaffoldState,
            sheetContent = {
                currentBottomSheet?.let {
                    MyBottomSheetScreen(
                        currentScreen = it,
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
                ) else RoundedCornerShape(0.dp),
            sheetPeekHeight = 1.dp,
            sheetGesturesEnabled = false
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
                        startDestination = MainDestinations.HOME_ROUTE,
                        modifier = Modifier.padding(innerPaddingModifier)
                    ) {
                        berryBucketNavGraph(
                            onBucketSelected = { selected, nav ->
                                when (selected) {
                                    is BucketSelected.goToDetailBucket -> {
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
                            bottomSheetItemClicked = {
                                currentBottomSheet = it
                                isNeedToBottomSheetOpen.invoke(true)
                            },
                            backPress = appState::backPress
                        )
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

sealed class BucketSelected {
    data class goToDetailBucket(val bucketInfo: UIBucketInfoSimple) : BucketSelected()
}

private fun NavGraphBuilder.berryBucketNavGraph(
    onBucketSelected: (BucketSelected, NavBackStackEntry) -> Unit,
    bottomSheetItemClicked: (BottomSheetScreenType) -> Unit,
    backPress: () -> Unit
) {
    navigation(
        route = MainDestinations.HOME_ROUTE,
        startDestination = HomeSections.MY.route
    ) {
        addHomeGraph(onBucketSelected, bottomSheetItemClicked)
    }
    composable(
        "${MainDestinations.OPEN_BUCKET_DETAIL}/{${MainDestinations.BUCKET_ID_KEY}}",
        arguments = listOf(navArgument(MainDestinations.BUCKET_ID_KEY) { type = NavType.LongType })
    ) { backStackEntry ->
        val arguments = requireNotNull(backStackEntry.arguments)
        val detailId = arguments.getString(MainDestinations.BUCKET_ID_KEY) ?: ""
        OpenDetailLayer(detailId, backPress)
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
