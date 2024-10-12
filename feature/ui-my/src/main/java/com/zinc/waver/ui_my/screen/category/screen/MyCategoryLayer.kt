package com.zinc.waver.ui_my.screen.category.screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import com.zinc.waver.model.CategoryLoadFailStatus
import com.zinc.waver.model.MyPagerClickEvent
import com.zinc.waver.model.MyTabType.CATEGORY
import com.zinc.waver.ui.design.theme.Gray8
import com.zinc.waver.ui.design.theme.Main4
import com.zinc.waver.ui.presentation.component.CategoryListView
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.presentation.component.dialog.ApiFailDialog
import com.zinc.waver.ui.presentation.screen.category.component.AddNewCategoryDialog
import com.zinc.waver.ui.presentation.screen.category.model.AddCategoryEvent
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.ui.viewmodel.CategoryViewModel
import com.zinc.waver.ui_my.R
import com.zinc.waver.ui_my.screen.category.component.MyCategoryAddView
import com.zinc.waver.ui_my.view.EditButtonAndSearchImageView
import com.zinc.waver.ui_common.R as CommonR

@Composable
fun CategoryLayer(
    clickEvent: (MyPagerClickEvent) -> Unit
) {
    val recommendCategory = "여향"
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    val viewModel: CategoryViewModel = hiltViewModel()

    val categoryListAsState by viewModel.categoryInfoList.observeAsState()
    val apiFailed by viewModel.loadFail.observeAsState()

    val categoryList = remember { mutableStateOf(categoryListAsState) }
    val addNewCategoryDialogShowAvailable = remember { mutableStateOf(false) } // 카테고리 추가 팝업 노출 여부
    val apiFailState = remember { mutableStateOf(apiFailed) }
    val apiFailDialogShow = remember { mutableStateOf(false) }

    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { owner, event ->
            Log.e("ayhan", "CategoryLayer event  :$event")
            if (event == Lifecycle.Event.ON_CREATE) {
                viewModel.loadCategoryList()
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(key1 = categoryListAsState, block = {
        Log.e("ayhan", "categoryListAsState : $categoryListAsState")
        addNewCategoryDialogShowAvailable.value = false
        categoryList.value = categoryListAsState
    })

    LaunchedEffect(apiFailed) {
        if (apiFailed != null) {
            apiFailDialogShow.value = true
            apiFailState.value = apiFailed
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        TopView(modifier = Modifier.fillMaxWidth(), recommendCategory, clickEvent)
        Spacer(modifier = Modifier.height(16.dp))
        CategoryListView(categoryList.value.orEmpty()) {
            clickEvent.invoke(MyPagerClickEvent.GoTo.CategoryItemClicked(it))
        }
        MyCategoryAddView {
            addNewCategoryDialogShowAvailable.value = true
        }
        Spacer(modifier = Modifier.height(54.dp))
    }
    if (addNewCategoryDialogShowAvailable.value) {
        AddNewCategoryDialog(event = {
            when (it) {
                is AddCategoryEvent.AddNewAddCategory -> {
                    viewModel.requestAddNewCategory(it.name)
                }

                AddCategoryEvent.Close -> {
                    addNewCategoryDialogShowAvailable.value = false
                }
            }
        })
    }

    if (apiFailDialogShow.value) {
        apiFailState.value?.let { failStatus ->
            ApiFailDialog(
                stringResource(id = CommonR.string.apiFailTitle),
                stringResource(id = CommonR.string.apiFailMessage)
            ) {
                when (failStatus) {
                    CategoryLoadFailStatus.LoadFail -> viewModel.loadCategoryList()
                    else -> {
                        // Do Nothing
                    }
                }
                apiFailDialogShow.value = false
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