package com.zinc.berrybucket.compose.ui.my

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import com.zinc.berrybucket.model.BottomButtonClickEvent
import com.zinc.berrybucket.model.MySearchClickEvent
import com.zinc.berrybucket.model.MyTabType
import com.zinc.berrybucket.presentation.my.viewModel.MyViewModel

@Composable
fun MyBottomSheetScreen(
    currentScreen: BottomSheetScreenType?,
    isNeedToBottomSheetOpen: (Boolean) -> Unit
) {
    when (currentScreen) {
        is BottomSheetScreenType.FilterScreen -> {
            FilterBottomView(
                viewModel = currentScreen.viewModel,
                isNeedToBottomSheetOpen = isNeedToBottomSheetOpen
            )
        }
        is BottomSheetScreenType.SearchScreen -> {
            SearchBottomView(
                tab = currentScreen.selectTab,
                viewModel = currentScreen.viewModel,
                isNeedToBottomSheetOpen = isNeedToBottomSheetOpen
            )
        }
        else -> {
            // Do Nothing
        }
    }
}

@Composable
private fun SearchBottomView(
    tab: MyTabType,
    viewModel: MyViewModel,
    isNeedToBottomSheetOpen: (Boolean) -> Unit
) {


    MySearchBottomScreen(currentTabType = tab, clickEvent = {
        when (it) {
            MySearchClickEvent.CloseClicked -> {
                isNeedToBottomSheetOpen(false)
            }
            is MySearchClickEvent.ItemClicked -> TODO()
            is MySearchClickEvent.SearchClicked -> TODO()
        }
    }, searchWord = { tab, word ->

    }, result = viewModel.searchResult.observeAsState()
    )
}

@Composable
private fun FilterBottomView(
    viewModel: MyViewModel,
    isNeedToBottomSheetOpen: (Boolean) -> Unit
) {
    MyFilterBottomScreen(viewModel = viewModel, clickEvent = {
        when (it) {
            BottomButtonClickEvent.LeftButtonClicked -> {
                isNeedToBottomSheetOpen.invoke(false)
            }
            BottomButtonClickEvent.RightButtonClicked -> TODO()
        }
    })

}

sealed class BottomSheetScreenType {
    data class SearchScreen(
        val selectTab: MyTabType, val viewModel: MyViewModel
    ) : BottomSheetScreenType()

    data class FilterScreen(
        val viewModel: MyViewModel
    ) : BottomSheetScreenType()
}