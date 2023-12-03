package com.zinc.berrybucket.ui_my.screen.category.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.zinc.berrybucket.model.MyPagerClickEvent
import com.zinc.berrybucket.model.MyTabType.CATEGORY
import com.zinc.berrybucket.ui.design.theme.Gray8
import com.zinc.berrybucket.ui.design.theme.Main4
import com.zinc.berrybucket.ui.presentation.component.CategoryListView
import com.zinc.berrybucket.ui.presentation.component.MyText
import com.zinc.berrybucket.ui.presentation.screen.category.component.AddNewCategoryDialog
import com.zinc.berrybucket.ui.presentation.screen.category.model.AddCategoryEvent
import com.zinc.berrybucket.ui.util.dpToSp
import com.zinc.berrybucket.ui.viewmodel.CategoryViewModel
import com.zinc.berrybucket.ui_my.R
import com.zinc.berrybucket.ui_my.screen.category.MyCategoryAddView
import com.zinc.berrybucket.ui_my.view.EditButtonAndSearchImageView

@Composable
fun CategoryLayer(
    modifier: Modifier = Modifier,
    clickEvent: (MyPagerClickEvent) -> Unit
) {
    val recommendCategory = "여향"

    val viewModel: CategoryViewModel = hiltViewModel()
    val categoryList by viewModel.categoryInfoList.observeAsState()

    val addNewCategoryDialogShowAvailable = remember { mutableStateOf(false) } // 카테고리 추가 팝업 노출 여부

    LaunchedEffect(key1 = categoryList, block = {
        addNewCategoryDialogShowAvailable.value = false
    })

    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)
    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { owner, event ->
            if (event == Lifecycle.Event.ON_CREATE) {
                viewModel.loadCategoryList()
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }


    categoryList?.let {
        if (addNewCategoryDialogShowAvailable.value) {
            AddNewCategoryDialog(event = {
                when (it) {
                    is AddCategoryEvent.AddNewAddCategory -> {
                        viewModel.addNewCategory(it.name)
                    }

                    AddCategoryEvent.Close -> {
                        addNewCategoryDialogShowAvailable.value = false
                    }
                }
            })
        }

        Column(
            modifier = modifier.verticalScroll(rememberScrollState())
        ) {
            TopView(modifier = Modifier.fillMaxWidth(), recommendCategory, clickEvent)
            Spacer(modifier = Modifier.height(16.dp))
            CategoryListView(it) {
                clickEvent.invoke(MyPagerClickEvent.GoTo.CategoryItemClicked(it))
            }
            MyCategoryAddView {
                addNewCategoryDialogShowAvailable.value = true
            }
        }
    }
}

@Composable
private fun TopView(
    modifier: Modifier = Modifier,
    recommendCategory: String,
    clickEvent: (MyPagerClickEvent) -> Unit
) {
    Row(
        modifier = modifier
            .padding(start = 22.dp, end = 16.dp, top = 16.dp)
    ) {
        TopLeftView(
            modifier = Modifier.align(Alignment.CenterVertically),
            recommendCategory = recommendCategory
        )
        TopRightView(modifier = Modifier.fillMaxWidth(), clickEvent = clickEvent)
    }
}

@Composable
private fun TopLeftView(modifier: Modifier = Modifier, recommendCategory: String) {
    Row(modifier = modifier) {
        MyText(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = stringResource(id = R.string.recommendCategory),
            color = Gray8,
            fontSize = dpToSp(13.dp),
        )
        MyText(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 8.dp),
            text = stringResource(id = R.string.recommendTag) + recommendCategory,
            color = Main4,
            fontSize = dpToSp(13.dp),
        )
    }

}

@Composable
private fun TopRightView(modifier: Modifier = Modifier, clickEvent: (MyPagerClickEvent) -> Unit) {
    EditButtonAndSearchImageView(
        modifier = modifier
            .fillMaxWidth(),
        editClicked = {
            clickEvent(MyPagerClickEvent.GoTo.CategoryEditClicked)
        },
        searchClicked = {
            clickEvent(MyPagerClickEvent.BottomSheet.SearchClicked(tabType = CATEGORY, true))
        }
    )
}