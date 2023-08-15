package com.zinc.berrybucket.ui_my

import androidx.compose.runtime.Composable
import com.zinc.berrybucket.model.MySearchClickEvent
import com.zinc.berrybucket.model.MyTabType
import com.zinc.berrybucket.ui_my.screen.all.MyAllBucketFilterBottomScreen
import com.zinc.berrybucket.ui_my.screen.dday.MyDdayBucketFilterBottomScreen
import com.zinc.berrybucket.ui_my.viewModel.MyViewModel

@Composable
fun MyBottomSheetScreen(
    currentScreen: BottomSheetScreenType?,
    isNeedToBottomSheetOpen: (Boolean) -> Unit
) {
    when (currentScreen) {
        is BottomSheetScreenType.FilterScreen -> {
            FilterBottomView(
                tab = currentScreen.selectTab,
                viewModel = currentScreen.viewModel,
                isNeedToBottomSheetOpen = isNeedToBottomSheetOpen
            )
        }

        is BottomSheetScreenType.SearchScreen -> {
            SearchBottomView(
                tab = currentScreen.selectTab,
                isNeedToBottomSheetOpen = isNeedToBottomSheetOpen
            )
        }

        else -> {
            // Do Nothing
        }
    }
}

@Composable
fun SearchBottomView(
    tab: MyTabType,
    isNeedToBottomSheetOpen: (Boolean) -> Unit
) {
    MySearchBottomScreen(currentTabType = tab, clickEvent = {
        when (it) {
            MySearchClickEvent.CloseClicked -> {
                isNeedToBottomSheetOpen(false)
            }

            is MySearchClickEvent.ItemClicked -> {

            }
        }
    })
}

@Composable
private fun FilterBottomView(
    tab: MyTabType,
    viewModel: MyViewModel,
    isNeedToBottomSheetOpen: (Boolean) -> Unit
) {
    when (tab) {
        is MyTabType.ALL -> {
            MyAllBucketFilterBottomScreen(
                viewModel = viewModel,
                negativeEvent = {
                    isNeedToBottomSheetOpen.invoke(false)
                },
                positiveEvent = {
                    isNeedToBottomSheetOpen.invoke(false)
                })
        }

        is MyTabType.DDAY -> {
            MyDdayBucketFilterBottomScreen(
                negativeEvent = {
                    isNeedToBottomSheetOpen.invoke(false)
                },
                positiveEvent = {
                    isNeedToBottomSheetOpen.invoke(false)
                })
        }

        is MyTabType.CHALLENGE -> TODO()
        else -> {
            // Do Nothing
        }
    }


}

sealed class BottomSheetScreenType {
    data class SearchScreen(
        val selectTab: MyTabType, val viewModel: MyViewModel
    ) : BottomSheetScreenType()

    data class FilterScreen(
        val selectTab: MyTabType, val viewModel: MyViewModel
    ) : BottomSheetScreenType()
}