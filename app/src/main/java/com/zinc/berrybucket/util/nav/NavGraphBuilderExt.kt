package com.zinc.berrybucket.util.nav

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.zinc.berrybucket.model.HomeItemSelected
import com.zinc.berrybucket.model.MySearchClickEvent
import com.zinc.berrybucket.model.MyTabType
import com.zinc.berrybucket.model.UICategoryInfo
import com.zinc.berrybucket.model.WriteTotalInfo
import com.zinc.berrybucket.ui.presentation.detail.screen.CloseDetailScreen
import com.zinc.berrybucket.ui.presentation.detail.screen.OpenDetailScreen
import com.zinc.berrybucket.ui.presentation.home.HomeSections
import com.zinc.berrybucket.ui.presentation.model.ActionWithActivity
import com.zinc.berrybucket.ui.presentation.report.ReportScreen
import com.zinc.berrybucket.ui.presentation.screen.category.CategoryEditScreen
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
import com.zinc.berrybucket.ui_my.SearchBottomView
import com.zinc.berrybucket.ui_my.model.MyTopEvent
import com.zinc.berrybucket.ui_my.screen.alarm.AlarmScreen
import com.zinc.berrybucket.ui_my.screen.category.screen.CategoryBucketListScreen
import com.zinc.berrybucket.ui_my.screen.profile.FollowerListScreen
import com.zinc.berrybucket.ui_my.screen.profile.FollowerListSettingScreen
import com.zinc.berrybucket.ui_my.screen.profile.FollowingListScreen
import com.zinc.berrybucket.ui_my.screen.profile.FollowingListSettingScreen
import com.zinc.berrybucket.ui_other.screen.OtherHomeScreen
import com.zinc.berrybucket.ui_write.model.WriteEvent
import com.zinc.berrybucket.ui_write.presentation.WriteScreen
import com.zinc.berrybucket.util.nav.MainDestinations.FOLLOWER.MY_FOLLOWER
import com.zinc.berrybucket.util.nav.MainDestinations.FOLLOWER.MY_FOLLOWER_SETTING
import com.zinc.berrybucket.util.nav.MainDestinations.FOLLOWING.MY_FOLLOWING
import com.zinc.berrybucket.util.nav.MainDestinations.FOLLOWING.MY_FOLLOWING_SETTING
import com.zinc.common.models.ReportInfo


internal fun NavGraphBuilder.homeMy(
    onBucketSelected: (HomeItemSelected, NavBackStackEntry) -> Unit,
    bottomSheetClicked: (BottomSheetScreenType, NavBackStackEntry) -> Unit,
    myTopEvent: (MyTopEvent, NavBackStackEntry) -> Unit,
    goToCategoryEdit: (NavBackStackEntry) -> Unit,
) {
    composable(HomeSections.MY.route, exitTransition = null, enterTransition = null) { from ->
        MyScreen(
            itemSelected = {
                onBucketSelected.invoke(it, from)
            },
            bottomSheetClicked = {
                bottomSheetClicked.invoke(it, from)
            },
            myTopEvent = {
                myTopEvent.invoke(it, from)
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
        FeedScreen(goToBucket = {
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

internal fun NavGraphBuilder.homeMore(
    moreItemClicked: (MoreItemType, NavBackStackEntry) -> Unit,
    backPress: () -> Unit
) {
    composable(HomeSections.MORE.route) { from ->
        MoreScreen(moreItemClicked = {
            moreItemClicked.invoke(it, from)
        }, goToBack = {
            backPress()
        })
    }
}

internal fun NavGraphBuilder.homeSearchNavGraph(
    mySearchClickEvent: (MySearchClickEvent, NavBackStackEntry) -> Unit
) {

    composable(
        route = MainDestinations.MY_SEARCH + "/{${MainDestinations.SELECT_TAB}}",
        arguments = listOf(
            navArgument(MainDestinations.SELECT_TAB) {
                type = SerializableType(
                    type = MyTabType::class.java,
                    parser = MyTabType::parseNavigationValue
                )
            }
        ),
        content = { entry ->
            val arguments = requireNotNull(entry.arguments)
            val selectedTab: MyTabType =
                arguments.extraNotNullSerializable(MainDestinations.SELECT_TAB)

            SearchBottomView(
                tab = selectedTab,
                mySearchClickEvent = {
                    mySearchClickEvent(it, entry)
                }
            )
        }
    )
}

internal fun NavGraphBuilder.homeCategoryBucketListNavGraph(
    backPress: () -> Unit,
    bucketClicked: (String, Boolean, NavBackStackEntry) -> Unit,
) {
    composable(
        route = "${MainDestinations.MY_CATEGORY_BUCKET_LIST}/{${MainDestinations.CATEGORY_INFO}}",
        arguments = listOf(
            navArgument(MainDestinations.CATEGORY_INFO) {
                type = SerializableType(
                    type = UICategoryInfo::class.java,
                    parser = UICategoryInfo::parseNavigationValue
                )
            }
        ),
        content = { entry ->
            val arguments = requireNotNull(entry.arguments)
            val categoryInfo: UICategoryInfo =
                arguments.extraNotNullSerializable(MainDestinations.CATEGORY_INFO)

            CategoryBucketListScreen(
                _categoryInfo = categoryInfo,
                onBackPressed = backPress,
                bucketItemClicked = { id, isPrivate ->
                    bucketClicked(id, isPrivate, entry)
                }
            )
        }
    )
}

internal fun NavGraphBuilder.homeCategoryEditNavGraph(backPress: () -> Unit) {
    composable(MainDestinations.MY_CATEGORY_EDIT) { entry ->
        CategoryEditScreen(backClicked = backPress)
    }
}

internal fun NavGraphBuilder.myFollowingListNavGraph(
    backPress: () -> Unit,
    goToSetting: (NavBackStackEntry) -> Unit,
    goToOtherHome: (NavBackStackEntry, String) -> Unit
) {
    composable(MY_FOLLOWING) { nav ->
        FollowingListScreen(
            goToBack = {
                backPress()
            },
            goToSetting = {
                goToSetting(nav)
            },
            goToOtherHome = { id ->
                goToOtherHome(nav, id)
            }
        )
    }
}

internal fun NavGraphBuilder.myFollowingSettingNavGraph(
    backPress: () -> Unit,
    goToOtherHome: (NavBackStackEntry, String) -> Unit
) {
    composable(MY_FOLLOWING_SETTING) { nav ->
        FollowingListSettingScreen(
            goToBack = {
                backPress()
            },
            goToOtherHome = { id -> goToOtherHome(nav, id) }
        )
    }
}

internal fun NavGraphBuilder.myFollowerListNavGraph(
    backPress: () -> Unit,
    goToSetting: (NavBackStackEntry) -> Unit,
    goToOtherHome: (NavBackStackEntry, String) -> Unit
) {
    composable(MY_FOLLOWER) { nav ->
        FollowerListScreen(
            goToBack = { backPress() },
            goToSetting = { goToSetting(nav) },
            goToOtherHome = { id -> goToOtherHome(nav, id) }
        )
    }
}

internal fun NavGraphBuilder.myFollowerSettingNavGraph(
    backPress: () -> Unit,
    goToOtherHome: (NavBackStackEntry, String) -> Unit
) {
    composable(MY_FOLLOWER_SETTING) { nav ->
        FollowerListSettingScreen(
            goToBack = { backPress() },
            goToOtherHome = { id -> goToOtherHome(nav, id) })
    }
}

internal fun NavGraphBuilder.openBucketReportNavGraph(
    backPress: () -> Unit
) {
    navigation(
        route = MainDestinations.OPEN_BUCKET_DETAIL + "/{${BucketListDetailDestinations.REPORT_INFO}}",
        startDestination = BucketListDetailDestinations.BUCKET_COMMENT_REPORT
    ) {
        composable(BucketListDetailDestinations.BUCKET_COMMENT_REPORT + "/{${BucketListDetailDestinations.REPORT_INFO}}") { entry ->
            val arguments = requireNotNull(entry.arguments)
            val reportInfo =
                arguments.extraNotNullSerializable<ReportInfo>(BucketListDetailDestinations.REPORT_INFO)
            ReportScreen(reportInfo = reportInfo, backPress = backPress)
        }
    }
}

internal fun NavGraphBuilder.searchNavGraph(
    backPress: () -> Unit,
    searchEvent: (SearchEvent, NavBackStackEntry) -> Unit,
) {
    composable(SearchDestinations.GO_TO_SEARCH) { nav ->
        SearchScreen(
            closeEvent = backPress,
            searchEvent = {
                searchEvent.invoke(it, nav)
            }
        )
    }
}

internal fun NavGraphBuilder.writeNavGraph(
    action: (ActionWithActivity) -> Unit,
    backPress: () -> Unit,
    goToAddCategory: (NavBackStackEntry) -> Unit,
    goToHome: () -> Unit
) {
    composable(
        route = "${WriteDestinations.GO_TO_WRITE}/{${WriteDestinations.UPDATE_ID}}",
        arguments = listOf(
            navArgument(WriteDestinations.UPDATE_ID) {
                type = NavType.StringType
            }
        )) { entry ->
        val arguments = entry.arguments
        val bucketId = arguments?.getString(WriteDestinations.UPDATE_ID) ?: ""
        WriteScreen(
            id = bucketId,
            event = { event ->
                when (event) {
                    is WriteEvent.ActivityAction -> {
                        action(event.acton)
                    }

                    WriteEvent.GoToBack -> {
                        backPress()
                    }

                    WriteEvent.GoToAddCategory -> {
                        goToAddCategory(entry)
                    }
                }
            },
            addBucketSucceed = {
                goToHome()
            }
        )
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
    action: (ActionWithActivity) -> Unit,
) {
    composable(MoreDestinations.PROFILE_SETTING) {
        ProfileSettingScreen(
            onBackPressed = { backPress() },
            addImageAction = {
                action(it)
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

internal fun NavGraphBuilder.openBucketDetailNavGraph(
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
        OpenDetailScreen(
            detailId = detailId, goToEvent = {
                when (it) {
                    is GoToBucketDetailEvent.GoToCommentReport -> {
                        goToBucketDetailEvent.invoke(
                            GoToBucketDetailEvent.GoToCommentReport(it.reportInfo),
                            backStackEntry
                        )
                    }

                    is GoToBucketDetailEvent.GoToUpdate -> {
                        val info = it.info.copy(isForUpdate = true)
                        goToBucketDetailEvent.invoke(
                            GoToBucketDetailEvent.GoToUpdate(info), backStackEntry
                        )
                    }
                }
            }, backPress = backPress
        )
    }
}

internal fun NavGraphBuilder.closeBucketDetailNavGraph(
    goToBucketDetailEvent: (GoToBucketDetailEvent, NavBackStackEntry) -> Unit,
    backPress: () -> Unit
) {
    composable(
        "${MainDestinations.CLOSE_BUCKET_DETAIL}/{${MainDestinations.BUCKET_ID_KEY}}",
        arguments = listOf(navArgument(MainDestinations.BUCKET_ID_KEY) {
            type = NavType.StringType
        })
    ) { backStackEntry ->
        val arguments = requireNotNull(backStackEntry.arguments)
        val detailId = arguments.getString(MainDestinations.BUCKET_ID_KEY) ?: ""
        CloseDetailScreen(detailId, goToUpdate = {
            val info = it.copy(isForUpdate = true)
            goToBucketDetailEvent.invoke(
                GoToBucketDetailEvent.GoToUpdate(info), backStackEntry
            )
        }, backPress = backPress)
    }
}

internal fun NavGraphBuilder.goToOtherHomeNavGraph(
    backPress: () -> Unit
) {
    composable(
        "${OtherDestinations.GO_TO_OTHER_HOME}/{${OtherDestinations.OTHER_USER_ID}}",
        arguments = listOf(navArgument(OtherDestinations.OTHER_USER_ID) {
            type = NavType.StringType
        })
    ) { backStackEntry ->
        val arguments = requireNotNull(backStackEntry.arguments)
        val otherId = arguments.getString(OtherDestinations.OTHER_USER_ID) ?: ""
        OtherHomeScreen(userId = otherId) {
            backPress()
        }
    }
}

object MainDestinations {
    const val HOME_ROUTE = "home"
    const val CLOSE_BUCKET_DETAIL = "close_bucket_detail"
    const val OPEN_BUCKET_DETAIL = "open_bucket_detail"
    const val BUCKET_ID_KEY = "bucketId"
    const val MY_SEARCH = "my_search"
    const val MY_CATEGORY_EDIT = "my_category_edit"
    const val SELECT_TAB = "select_tab"
    const val MY_CATEGORY_BUCKET_LIST = "my_category_bucket_list"
    const val CATEGORY_INFO = "category_info"

    object FOLLOWING {
        const val MY_FOLLOWING = "my_following"
        const val MY_FOLLOWING_SETTING = "my_following_setting"
    }

    object FOLLOWER {
        const val MY_FOLLOWER = "my_follower"
        const val MY_FOLLOWER_SETTING = "my_follower_setting"
    }
}

object BucketListDetailDestinations {
    const val BUCKET_COMMENT_REPORT = "bucket_comment_report"
    const val REPORT_INFO = "report_info"
}

object SearchDestinations {
    const val GO_TO_SEARCH = "go_to_search"
}

object WriteDestinations {
    const val GO_TO_WRITE = "go_to_write"
    const val UPDATE_ID = "update_id"
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

object OtherDestinations {
    const val GO_TO_OTHER_HOME = "go_to_other_home"
    const val OTHER_USER_ID = "other_user_id"
}

sealed class SearchEvent {
    data object GoToSearch : SearchEvent()
    data class GoToOpenBucket(val id: String) : SearchEvent()
    data class GoToOtherUser(val id: String) : SearchEvent()
}

sealed class GoToBucketDetailEvent {
    data class GoToCommentReport(val reportInfo: ReportInfo) : GoToBucketDetailEvent()
    data class GoToUpdate(val info: WriteTotalInfo) : GoToBucketDetailEvent()
}

