package com.zinc.berrybucket.compose.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.zinc.berrybucket.compose.ui.detail.CloseDetailLayer
import com.zinc.berrybucket.compose.ui.home.HomeBottomBar
import com.zinc.berrybucket.compose.ui.home.HomeSections
import com.zinc.berrybucket.compose.ui.home.addHomeGraph
import com.zinc.berrybucket.model.DetailType
import com.zinc.berrybucket.model.UIBucketInfoSimple

@Composable
fun BerryBucketApp() {
    MaterialTheme() {
        val appState = rememberBerryBucketkAppState()

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
                                    }
                                }
                            }
                        },
                        backPress = appState::backPress
                    )
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
    backPress: () -> Unit
) {
    navigation(
        route = MainDestinations.HOME_ROUTE,
        startDestination = HomeSections.MY.route
    ) {
        addHomeGraph(onBucketSelected)
    }
    composable(
        "${MainDestinations.OPEN_BUCKET_DETAIL}/{${MainDestinations.BUCKET_ID_KEY}}",
        arguments = listOf(navArgument(MainDestinations.BUCKET_ID_KEY) { type = NavType.LongType })
    ) { backStackEntry ->
        val arguments = requireNotNull(backStackEntry.arguments)
        val snackId = arguments.getLong(MainDestinations.OPEN_BUCKET_DETAIL)
        //   OpenDetailLayer(snackId, upPress)
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
