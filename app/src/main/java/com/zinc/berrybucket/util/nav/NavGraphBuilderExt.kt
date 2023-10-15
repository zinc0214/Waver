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
import com.zinc.berrybucket.ui_my.model.MyTopEvent
import com.zinc.berrybucket.ui_my.screen.alarm.AlarmScreen
import com.zinc.berrybucket.ui_my.screen.category.screen.CategoryBucketListScreen
import com.zinc.berrybucket.ui_my.screen.category.screen.CategoryEditScreen
import com.zinc.berrybucket.ui_my.screen.profile.FollowerListScreen
import com.zinc.berrybucket.ui_my.screen.profile.FollowerListSettingScreen
import com.zinc.berrybucket.ui_my.screen.profile.FollowingListScreen
import com.zinc.berrybucket.ui_my.screen.profile.FollowingListSettingScreen
import com.zinc.berrybucket.ui_write.model.Write1Event
import com.zinc.berrybucket.ui_write.presentation.WriteScreen1
import com.zinc.berrybucket.ui_write.presentation.WriteScreen2
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

            com.zinc.berrybucket.ui_my.SearchBottomView(
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
    goToSetting: (NavBackStackEntry) -> Unit
) {
    composable(MY_FOLLOWING) { nav ->
        FollowingListScreen(
            goToBack = {
                backPress()
            },
            goToSetting = {
                goToSetting(nav)
            }
        )
    }
}

internal fun NavGraphBuilder.myFollowingSettingNavGraph(
    backPress: () -> Unit
) {
    composable(MY_FOLLOWING_SETTING) {
        FollowingListSettingScreen(
            goToBack = {
                backPress()
            }
        )
    }
}

internal fun NavGraphBuilder.myFollowerListNavGraph(
    backPress: () -> Unit,
    goToSetting: (NavBackStackEntry) -> Unit
) {
    composable(MY_FOLLOWER) { nav ->
        FollowerListScreen(goToBack = { backPress() }, goToSetting = { goToSetting(nav) }
        )
    }
}

internal fun NavGraphBuilder.myFollowerSettingNavGraph(
    backPress: () -> Unit
) {
    composable(MY_FOLLOWER_SETTING) { nav ->
        FollowerListSettingScreen(goToBack = { backPress() })
    }
}

internal fun NavGraphBuilder.bucketNavGraph(
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
    backPress: () -> Unit
) {
    composable(SearchDestinations.GO_TO_SEARCH) {
        SearchScreen {
            backPress()
        }
    }
}

// 3
internal fun NavGraphBuilder.writeNavGraph1(
    action: (ActionWithActivity) -> Unit,
    backPress: () -> Unit,
    goToNextWrite: (NavBackStackEntry, WriteTotalInfo) -> Unit,
    goToAddCategory: (NavBackStackEntry) -> Unit
) {

    composable(
        route = WriteDestinations.GO_TO_WRITE1 + "/{${WriteDestinations.WRITE_INFO}}",
        arguments = listOf(
            navArgument(WriteDestinations.WRITE_INFO) {
                type = SerializableType(
                    type = WriteTotalInfo::class.java,
                    parser = WriteTotalInfo::parseNavigationValue
                )
            }
        ),
        content = { entry ->
            val arguments = requireNotNull(entry.arguments)
            val totalInfo: WriteTotalInfo =
                arguments.extraNotNullSerializable(WriteDestinations.WRITE_INFO)

            WriteScreen1(
                event = { event ->
                    when (event) {
                        is Write1Event.ActivityAction -> {
                            action(event.acton)
                        }

                        is Write1Event.GoToWrite2 -> {
                            goToNextWrite(entry, event.info)

                        }

                        Write1Event.GoToBack -> {
                            backPress()
                        }

                        Write1Event.GoToAddCategory -> {
                            goToAddCategory(entry)
                        }
                    }
                },
                originWriteTotalInfo = totalInfo
            )
        }
    )
}

internal fun NavGraphBuilder.writeNavGraph2(
    backPress: (NavBackStackEntry, WriteTotalInfo) -> Unit,
    goToHome: () -> Unit
) {
    composable(
        route = WriteDestinations.GO_TO_WRITE2 + "/{${WriteDestinations.WRITE_INFO}}",
        arguments = listOf(
            navArgument(WriteDestinations.WRITE_INFO) {
                type = SerializableType(
                    type = WriteTotalInfo::class.java,
                    parser = WriteTotalInfo::parseNavigationValue
                )
            }
        ),
        content = { nav ->
            val arguments = requireNotNull(nav.arguments)
            val writeTotalInfo =
                arguments.extraNotNullSerializable<WriteTotalInfo>(WriteDestinations.WRITE_INFO)
            WriteScreen2(
                writeTotalInfo = writeTotalInfo,
                goToBack = { newInfo -> backPress(nav, newInfo) },
                addBucketSucceed = { goToHome() })
        }
    )
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

internal fun NavGraphBuilder.bucketDetailNavGraph(
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
                            GoToBucketDetailEvent.GoToCommentReport(it.reportInfo), backStackEntry
                        )
                    }

                    is GoToBucketDetailEvent.GoToUpdate -> {
                        goToBucketDetailEvent.invoke(
                            GoToBucketDetailEvent.GoToUpdate(it.info), backStackEntry
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
        CloseDetailScreen(detailId, goToUpdate = {
            goToBucketDetailEvent.invoke(
                GoToBucketDetailEvent.GoToUpdate(it), backStackEntry
            )
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
    data class GoToUpdate(val info: WriteTotalInfo) : GoToBucketDetailEvent()
}

