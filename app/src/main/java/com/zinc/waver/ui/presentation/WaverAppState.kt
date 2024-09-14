package com.zinc.waver.ui.presentation

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
import com.zinc.waver.model.MyTabType
import com.zinc.waver.model.UICategoryInfo
import com.zinc.waver.ui.presentation.home.HomeSections
import com.zinc.waver.util.nav.AlarmDestinations
import com.zinc.waver.util.nav.MainDestinations
import com.zinc.waver.util.nav.MainDestinations.HOME_ROUTE
import com.zinc.waver.util.nav.MoreDestinations
import com.zinc.waver.util.nav.OtherDestinations
import com.zinc.waver.util.nav.SearchDestinations
import com.zinc.waver.util.nav.WriteDestinations
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberWaverAppState(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navController: NavHostController = rememberNavController(),
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) =
    remember(scaffoldState, navController, resources, coroutineScope) {
        WaverAppState(scaffoldState, navController, resources, coroutineScope)
    }


/**
 * Responsible for holding state related to [JetsnackApp] and containing UI-related logic.
 */
@Stable
class WaverAppState(
    val scaffoldState: ScaffoldState,
    val navController: NavHostController,
    private val resources: Resources,
    coroutineScope: CoroutineScope
) {

    // ----------------------------------------------------------
    // BottomBar state source of truth
    // ----------------------------------------------------------

    val bottomBarTabs = HomeSections.entries
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
            val selectTab = MyTabType.toNavigationValue(tab)
            navController.navigate("${MainDestinations.MY_SEARCH}/${selectTab}")
        }
    }

    fun navigateToOpenBucketDetail(
        bucketId: String,
        writerId: String,
        isMine: Boolean,
        from: NavBackStackEntry
    ) {
        // In order to discard duplicated navigation events, we check the Lifecycle
        if (from.lifecycleIsResumed()) {
            navController.navigate("${MainDestinations.OPEN_BUCKET_DETAIL}/${bucketId}/${writerId}/${isMine}")
        }
    }

    fun navigateToCloseBucketDetail(bucketId: String, from: NavBackStackEntry) {
        // In order to discard duplicated navigation events, we check the Lifecycle
        if (from.lifecycleIsResumed()) {
            navController.navigate("${MainDestinations.CLOSE_BUCKET_DETAIL}/${bucketId}")
        }
    }

    fun navigateToCategoryBucketList(categoryInfo: UICategoryInfo, from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            val infoParse = UICategoryInfo.toNavigationValue(categoryInfo)
            navController.navigate("${MainDestinations.MY_CATEGORY_BUCKET_LIST}/${infoParse}")
        }
    }

    fun navigateToSearch(from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate(SearchDestinations.GO_TO_SEARCH)
        }
    }

    fun navigateToWriteBucket(updateId: String, from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate("${WriteDestinations.GO_TO_WRITE}/${updateId}",
                navOptions {
                    popUpTo(findStartDestination(navController.graph).id) {
                        inclusive = false
                        saveState = true
                    }
                })
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

    fun navigateToFollowingList(from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate(MainDestinations.FOLLOWING.MY_FOLLOWING)
        }
    }

    fun navigateToFollowingSettingList(
        from: NavBackStackEntry,
    ) {
        if (from.lifecycleIsResumed()) {
            navController.navigate(MainDestinations.FOLLOWING.MY_FOLLOWING_SETTING)
        }
    }

    fun navigateToFollowerList(from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate(MainDestinations.FOLLOWER.MY_FOLLOWER)
        }
    }

    fun navigateToFollowerSettingList(from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate(MainDestinations.FOLLOWER.MY_FOLLOWER_SETTING)
        }
    }

    fun navigateToOtherHome(from: NavBackStackEntry, userId: String) {
        if (from.lifecycleIsResumed()) {
            navController.navigate("${OtherDestinations.GO_TO_OTHER_HOME}/${userId}")
        }
    }

    fun navigateToMyWaveManage(from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate(MoreDestinations.MY_WAVE_MANAGE)
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
