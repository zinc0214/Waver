package com.zinc.berrybucket.ui_my

import androidx.compose.runtime.Composable
import com.zinc.berrybucket.model.BottomButtonClickEvent
import com.zinc.berrybucket.model.MySearchClickEvent
import com.zinc.berrybucket.model.MyTabType
import com.zinc.berrybucket.ui_my.all.MyAllBucketFilterBottomScreen
import com.zinc.berrybucket.ui_my.dday.MyDdayBucketFilterBottomScreen
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
        MyTabType.ALL -> {
            MyAllBucketFilterBottomScreen(viewModel = viewModel, clickEvent = {
                when (it) {
                    BottomButtonClickEvent.LeftButtonClicked -> {
                        isNeedToBottomSheetOpen.invoke(false)
                    }
                    BottomButtonClickEvent.RightButtonClicked -> TODO()
                }
            })
        }
        MyTabType.DDAY -> {
            MyDdayBucketFilterBottomScreen(viewModel = viewModel, clickEvent = {
                when (it) {
                    BottomButtonClickEvent.LeftButtonClicked -> {
                        isNeedToBottomSheetOpen.invoke(false)
                    }
                    BottomButtonClickEvent.RightButtonClicked -> TODO()
                }
            })
        }
        MyTabType.CHALLENGE -> TODO()
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