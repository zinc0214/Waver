package com.zinc.berrybucket.ui.presentation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.zinc.berrybucket.model.AddImageType
import com.zinc.berrybucket.model.BucketSelected
import com.zinc.berrybucket.model.MyTabType
import com.zinc.berrybucket.model.UserSelectedImageInfo
import com.zinc.berrybucket.model.WriteTotalInfo
import com.zinc.berrybucket.ui.presentation.detail.screen.CloseDetailLayer
import com.zinc.berrybucket.ui.presentation.detail.screen.OpenDetailScreen
import com.zinc.berrybucket.ui.presentation.home.HomeSections
import com.zinc.berrybucket.ui.presentation.model.ActionWithActivity
import com.zinc.berrybucket.ui.presentation.report.ReportScreen
import com.zinc.berrybucket.ui.presentation.search.RecommendScreen
import com.zinc.berrybucket.ui.presentation.search.SearchScreen
import com.zinc.berrybucket.ui_feed.FeedScreen
import com.zinc.berrybucket.ui_more.AlarmSettingScreen
import com.zinc.berrybucket.ui_more.AppInfoScreen
import com.zinc.berrybucket.ui_more.BlockSettingScreen
import com.zinc.berrybucket.ui_more.MoreScreen
import com.zinc.berrybucket.ui_more.ProfileSettingScreen
import com.zinc.berrybucket.ui_more.models.AppInfoItemType
import com.zinc.berrybucket.ui_more.models.MoreItemType
import com.zinc.berrybucket.ui_my.BottomSheetScreenType
import com.zinc.berrybucket.ui_my.MyScreen
import com.zinc.berrybucket.ui_my.alarm.AlarmScreen
import com.zinc.berrybucket.ui_my.category.CategoryEditScreen
import com.zinc.berrybucket.ui_write.model.Write1Event
import com.zinc.berrybucket.ui_write.presentation.WriteScreen1
import com.zinc.berrybucket.ui_write.presentation.WriteScreen2
import com.zinc.berrybucket.util.getRequiredSerializableExtra
import com.zinc.common.models.ReportInfo


internal fun NavGraphBuilder.homeMy(
    onBucketSelected: (BucketSelected, NavBackStackEntry) -> Unit,
    bottomSheetClicked: (BottomSheetScreenType, NavBackStackEntry) -> Unit,
    alarmClicked: (NavBackStackEntry) -> Unit,
    goToCategoryEdit: (NavBackStackEntry) -> Unit,
) {
    composable(HomeSections.MY.route) { from ->
        MyScreen(
            onBucketSelected = {
                onBucketSelected.invoke(it, from)
            },
            bottomSheetClicked = {
                bottomSheetClicked.invoke(it, from)
            },
            alarmClicked = {
                alarmClicked.invoke(from)
            },
            goToCategoryEdit = {
                goToCategoryEdit.invoke(from)
            }
        )
    }
}

internal fun NavGraphBuilder.homeFeed(
    onFeedClicked: (String, NavBackStackEntry) -> Unit
) {
    composable(HomeSections.FEED.route) { from ->
        FeedScreen(feedClicked = {
            onFeedClicked(it, from)
        })
    }
}

internal fun NavGraphBuilder.homeSearch(
    onSearchEvent: (SearchEvent, NavBackStackEntry) -> Unit,
) {
    composable(HomeSections.SEARCH.route) { nav ->
        RecommendScreen(
            onSearchEvent = {
                onSearchEvent.invoke(it, nav)
            }
        )
    }
}

internal fun NavGraphBuilder.homeMore(moreItemClicked: (MoreItemType, NavBackStackEntry) -> Unit) {
    composable(HomeSections.MORE.route) { from ->
        MoreScreen(moreItemClicked = {
            moreItemClicked.invoke(it, from)
        })
    }
}

internal fun NavGraphBuilder.homeSearchNavGraph(backPress: () -> Unit) {
    composable(MainDestinations.MY_SEARCH) { entry ->
        val arguments = requireNotNull(entry.arguments)
        val selectedTab =
            arguments.getRequiredSerializableExtra<MyTabType>(MainDestinations.MY_SEARCH)
        com.zinc.berrybucket.ui_my.SearchBottomView(
            tab = selectedTab,
            isNeedToBottomSheetOpen = {
                backPress()
            }
        )

    }
}

internal fun NavGraphBuilder.homeCategoryEditNavGraph(backPress: () -> Unit) {
    composable(MainDestinations.MY_CATEGORY_EDIT) { entry ->
        CategoryEditScreen(backClicked = backPress)
    }
}

internal fun NavGraphBuilder.bucketNavGraph(
    backPress: () -> Unit
) {
    navigation(
        route = MainDestinations.OPEN_BUCKET_DETAIL,
        startDestination = BucketListDetailDestinations.BUCKET_COMMENT_REPORT
    ) {
        composable(BucketListDetailDestinations.BUCKET_COMMENT_REPORT) { entry ->
            val arguments = requireNotNull(entry.arguments)
            val reportInfo =
                arguments.getRequiredSerializableExtra<ReportInfo>(BucketListDetailDestinations.REPORT_INFO)
            ReportScreen(reportInfo = reportInfo, backPress = backPress)
        }
    }
}

internal fun NavGraphBuilder.searchNavGraph(
    backPress: () -> Unit
) {
    composable(SearchDestinations.GO_TO_SEARCH) {
        SearchScreen {
            backPress()
        }
    }
}

internal fun NavGraphBuilder.writeNavGraph1(
    action: (ActionWithActivity) -> Unit,
    backPress: () -> Unit,
    goToNextWrite: (NavBackStackEntry, WriteTotalInfo) -> Unit,
    goToAddCategory: (NavBackStackEntry) -> Unit
) {
    composable(WriteDestinations.GO_TO_WRITE1) { it ->
        val arguments = requireNotNull(it.arguments)
        val totalInfo =
            arguments.getRequiredSerializableExtra<WriteTotalInfo>(WriteDestinations.WRITE_INFO)

        WriteScreen1(
            event = { event ->
                when (event) {
                    is Write1Event.ActivityAction -> {
                        action(event.acton)
                    }

                    is Write1Event.GoToWrite2 -> {
                        goToNextWrite(it, event.info)

                    }

                    Write1Event.GoToBack -> {
                        backPress()
                    }

                    Write1Event.GoToAddCategory -> {
                        goToAddCategory(it)
                    }
                }
            },
            originWriteTotalInfo = totalInfo
        )
    }
}

internal fun NavGraphBuilder.writeNavGraph2(
    backPress: (NavBackStackEntry, WriteTotalInfo) -> Unit,
    goToHome: () -> Unit
) {

    composable(WriteDestinations.GO_TO_WRITE2) { nav ->
        val arguments = requireNotNull(nav.arguments)
        val writeTotalInfo =
            arguments.getRequiredSerializableExtra<WriteTotalInfo>(WriteDestinations.WRITE_INFO)
        WriteScreen2(
            writeTotalInfo = writeTotalInfo,
            goToBack = { newInfo -> backPress(nav, newInfo) },
            addBucketSucceed = { goToHome() })
    }
}

internal fun NavGraphBuilder.alarmNavGraph(
    backPress: () -> Unit
) {
    composable(AlarmDestinations.GO_TO_ALARM) {
        AlarmScreen {
            backPress()
        }
    }
}

internal fun NavGraphBuilder.moreAlarmNavGraph(
    backPress: () -> Unit
) {
    composable(MoreDestinations.ALARM_SETTING) {
        AlarmSettingScreen {
            backPress()
        }
    }
}

internal fun NavGraphBuilder.moreBlockNavGraph(
    backPress: () -> Unit
) {
    composable(MoreDestinations.BLOCK_SETTING) {
        BlockSettingScreen {
            backPress()
        }
    }
}

internal fun NavGraphBuilder.moreProfileNavGraph(
    backPress: () -> Unit,
    imageUpdateButtonClicked: (AddImageType, () -> Unit, (UserSelectedImageInfo) -> Unit) -> Unit
) {
    composable(MoreDestinations.PROFILE_SETTING) {
        ProfileSettingScreen(
            onBackPressed = { backPress() },
            imageUpdateButtonClicked = { type, fail, success ->
                imageUpdateButtonClicked(
                    type,
                    fail
                ) { info ->
                    success(info)
                }
            })
    }
}

internal fun NavGraphBuilder.moreAppInfoNavGraph(
    backPress: () -> Unit,
    moreItemClicked: (AppInfoItemType) -> Unit

) {
    composable(MoreDestinations.MORE_APP_INFO) {
        AppInfoScreen(onBackClicked = { backPress() }, moreItemClicked = moreItemClicked)
    }
}

internal fun NavGraphBuilder.bucketDetailNavGraph(
    goToBucketDetailEvent: (GoToBucketDetailEvent, NavBackStackEntry) -> Unit,
    goToUpdate: (WriteTotalInfo, NavBackStackEntry) -> Unit,
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
        OpenDetailScreen(
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
        CloseDetailLayer(detailId, goToUpdate = {
            goToUpdate(it, backStackEntry)
        }, backPress = backPress)
    }
}

object MainDestinations {
    const val HOME_ROUTE = "home"
    const val CLOSE_BUCKET_DETAIL = "close_bucket_detail"
    const val OPEN_BUCKET_DETAIL = "open_bucket_detail"
    const val BUCKET_ID_KEY = "bucketId"
    const val MY_SEARCH = "my_search"
    const val MY_CATEGORY_EDIT = "my_category_edit"
}

object BucketListDetailDestinations {
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

object MoreDestinations {
    const val ALARM_SETTING = "alarm_setting"
    const val BLOCK_SETTING = "block_setting"
    const val PROFILE_SETTING = "profile_setting"
    const val MORE_APP_INFO = "app_info"
}

object AlarmDestinations {
    const val GO_TO_ALARM = "go_to_alarm"
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

