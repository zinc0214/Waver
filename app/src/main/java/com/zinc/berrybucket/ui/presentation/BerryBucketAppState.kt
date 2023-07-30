package com.zinc.berrybucket.ui.presentation

import android.content.res.Resources
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.navigation.*
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.zinc.berrybucket.model.MyTabType
import com.zinc.berrybucket.model.WriteTotalInfo
import com.zinc.berrybucket.ui.presentation.MainDestinations.HOME_ROUTE
import com.zinc.berrybucket.ui.presentation.home.HomeSections
import com.zinc.berrybucket.util.navigateWithArgument
import com.zinc.common.models.ReportInfo
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberBerryBucketAppState(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navController: NavHostController = rememberNavController(),
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) =
    remember(scaffoldState, navController, resources, coroutineScope) {
        BerryBucketAppState(scaffoldState, navController, resources, coroutineScope)
    }


/**
 * Responsible for holding state related to [JetsnackApp] and containing UI-related logic.
 */
@Stable
class BerryBucketAppState(
    val scaffoldState: ScaffoldState,
    val navController: NavHostController,
    private val resources: Resources,
    coroutineScope: CoroutineScope
) {

    // ----------------------------------------------------------
    // BottomBar state source of truth
    // ----------------------------------------------------------

    val bottomBarTabs = HomeSections.values().toList()
    private val bottomBarRoutes = bottomBarTabs.map { it.route }

    // Reading this attribute will cause recompositions when the bottom bar needs shown, or not.
    // Not all routes need to show the bottom bar.
    val shouldShowBottomBar: Boolean
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination?.route in bottomBarRoutes ||
                navController
                    .currentBackStackEntryAsState().value?.destination?.route == HOME_ROUTE

    // ----------------------------------------------------------
    // Navigation state source of truth
    // ----------------------------------------------------------

    var currentHomeRoute = mutableStateOf(HomeSections.MY.route)

    val currentRoute: String?
        get() = if (navController.currentDestination?.route == HOME_ROUTE) HomeSections.MY.route else navController.currentDestination?.route

    fun backPress() {
        navController.navigateUp()
    }

    fun navigateToBottomBarRoute(route: String) {
        if (route != currentRoute) {
            navController.navigate(route) {
                launchSingleTop = true
                restoreState = true
                // Pop up backstack to the first destination and save state. This makes going back
                // to the start destination when pressing back in any other bottom tab.
                popUpTo(findStartDestination(navController.graph).id) {
                    saveState = true
                }
            }
        }
    }

    fun navigateToMySearch(tab: MyTabType, from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigateWithArgument(
                route = MainDestinations.MY_SEARCH,
                args = listOf(MainDestinations.MY_SEARCH to tab)
            )
        }
    }

    fun navigateToOpenBucketDetail(bucketId: String, from: NavBackStackEntry) {
        // In order to discard duplicated navigation events, we check the Lifecycle
        if (from.lifecycleIsResumed()) {
            navController.navigate("${MainDestinations.OPEN_BUCKET_DETAIL}/$bucketId")
        }
    }

    fun navigateToCloseBucketDetail(bucketId: String, from: NavBackStackEntry) {
        // In order to discard duplicated navigation events, we check the Lifecycle
        if (from.lifecycleIsResumed()) {
            navController.navigate("${MainDestinations.CLOSE_BUCKET_DETAIL}/$bucketId")
        }
    }

    fun navigateToCommentReport(reportInfo: ReportInfo, from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigateWithArgument(
                route = BucketListDetailDestinations.BUCKET_COMMENT_REPORT,
                args = listOf(
                    BucketListDetailDestinations.REPORT_INFO to reportInfo
                )
            )
        }
    }

    fun navigateToSearch(from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate(SearchDestinations.GO_TO_SEARCH)
        }
    }

    fun navigateToWrite1(writeInfo: WriteTotalInfo, from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigateWithArgument(
                route = WriteDestinations.GO_TO_WRITE1,
                args = listOf(
                    WriteDestinations.WRITE_INFO to writeInfo
                ),
                navOptions {
                    // TODO : 백했을 때 홈이 항상 첫번째 탭으로 오는 문제 해결 필요
                    popUpTo(findStartDestination(navController.graph).id) {
                        inclusive = false
                        saveState = true
                    }
                }
            )
        }
    }

    fun navigateToWrite2(from: NavBackStackEntry, totalInfo: WriteTotalInfo) {
        if (from.lifecycleIsResumed()) {
            navController.navigateWithArgument(
                route = WriteDestinations.GO_TO_WRITE2,
                args = listOf(
                    WriteDestinations.WRITE_INFO to totalInfo
                )
            )
        }
    }

    fun navigateToMoreAlarmSetting(from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate(MoreDestinations.ALARM_SETTING)
        }
    }

    fun navigateToMoreBlockSetting(from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate(MoreDestinations.BLOCK_SETTING)
        }
    }

    fun navigateToMoreProfileSetting(from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate(MoreDestinations.PROFILE_SETTING)
        }
    }

    fun navigateToMoreAppInfo(from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate(MoreDestinations.MORE_APP_INFO)
        }
    }

    fun navigateToAlarm(from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate(AlarmDestinations.GO_TO_ALARM)
        }
    }

    fun navigateToCategoryEdit(from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate(MainDestinations.MY_CATEGORY_EDIT)
        }
    }
}

/**
 * If the lifecycle is not resumed it means this NavBackStackEntry already processed a nav event.
 *
 * This is used to de-duplicate navigation events.
 */
private fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED

private val NavGraph.startDestination: NavDestination?
    get() = findNode(startDestinationId)

/**
 * Copied from similar function in NavigationUI.kt
 *
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:navigation/navigation-ui/src/main/java/androidx/navigation/ui/NavigationUI.kt
 */
private tailrec fun findStartDestination(graph: NavDestination): NavDestination {
    return if (graph is NavGraph) findStartDestination(graph.startDestination!!) else graph
}

/**
 * A composable function that returns the [Resources]. It will be recomposed when `Configuration`
 * gets updated.
 */
@Composable
@ReadOnlyComposable
private fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}
