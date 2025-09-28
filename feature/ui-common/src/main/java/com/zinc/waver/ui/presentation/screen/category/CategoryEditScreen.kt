package com.zinc.waver.ui.presentation.screen.category

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.zinc.waver.model.CategoryLoadFailStatus
import com.zinc.waver.model.UICategoryInfo
import com.zinc.waver.ui.design.theme.Gray1
import com.zinc.waver.ui.presentation.component.dialog.ApiFailDialog
import com.zinc.waver.ui.presentation.screen.category.component.AddNewCategoryDialog
import com.zinc.waver.ui.presentation.screen.category.component.CategoryEditTitleView
import com.zinc.waver.ui.presentation.screen.category.component.EditCategoryNameDialog
import com.zinc.waver.ui.presentation.screen.category.component.VerticalReorderList
import com.zinc.waver.ui.presentation.screen.category.model.AddCategoryEvent
import com.zinc.waver.ui.presentation.screen.category.model.CategoryEditOptionEvent
import com.zinc.waver.ui.presentation.screen.category.model.EditCategoryNameEvent
import com.zinc.waver.ui.viewmodel.CategoryViewModel
import com.zinc.waver.ui_common.R

@Composable
fun CategoryEditScreen(
    backClicked: () -> Unit,
    viewModel: CategoryViewModel = hiltViewModel()
) {

    val categoryList by viewModel.categoryInfoList.observeAsState()
    val apiFailed by viewModel.loadFail.observeAsState()

    if (categoryList.isNullOrEmpty()) {
        viewModel.loadCategoryList()
    }

    val addNewCategoryDialogShowAvailable = remember { mutableStateOf(false) } // 카테고리 추가 팝업 노출 여부
    val editCategoryNameDialogShowAvailable =
        remember { mutableStateOf<UICategoryInfo?>(null) } // 카테고리 이름 편집 팝업 노출 여부
    val apiFailDialogShow = remember { mutableStateOf(false) }
    val apiFailState = remember { mutableStateOf(apiFailed) }
    val categoryItemState = remember { mutableStateOf(categoryList) }

    LaunchedEffect(categoryList) {
        categoryItemState.value = categoryList
    }

    LaunchedEffect(apiFailed) {
        if (apiFailed != null) {
            apiFailDialogShow.value = true
            apiFailState.value = apiFailed
        }
    }

    if (apiFailDialogShow.value) {
        apiFailState.value?.let { failStatus ->
            ApiFailDialog(
                title = stringResource(id = R.string.apiFailTitle),
                message = stringResource(id = R.string.apiFailMessage)
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

    if (addNewCategoryDialogShowAvailable.value) {
        AddNewCategoryDialog(event = {
            when (it) {
                is AddCategoryEvent.AddNewAddCategory -> {
                    addNewCategoryDialogShowAvailable.value = false
                    viewModel.requestAddNewCategory(it.name)
                }

                AddCategoryEvent.Close -> addNewCategoryDialogShowAvailable.value = false
            }
        })
    }

    if (editCategoryNameDialogShowAvailable.value != null) {
        EditCategoryNameDialog(
            originCategoryInfo = editCategoryNameDialogShowAvailable.value!!,
            event = {
                when (it) {
                    EditCategoryNameEvent.Close -> {
                        editCategoryNameDialogShowAvailable.value = null
                    }

                    is EditCategoryNameEvent.EditCategoryName -> {
                        editCategoryNameDialogShowAvailable.value = null
                        viewModel.editCategory(it.categoryInfo)
                    }
                }
            })
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray1)
            .statusBarsPadding()
    ) {

        CategoryEditTitleView(backClicked = {
            backClicked()
        }, updateCategoryOrder = {
            viewModel.reorderCategory()
        })

        VerticalReorderList(categoryList = categoryItemState.value.orEmpty(), addNewCategory = {
            addNewCategoryDialogShowAvailable.value = true
        }, optionEvent = { event ->
            when (event) {
                is CategoryEditOptionEvent.DeleteCategory -> {
                    viewModel.removeCategory(event.categoryInfo.id)
                }

                is CategoryEditOptionEvent.EditCategoryName -> {
                    editCategoryNameDialogShowAvailable.value = event.categoryInfo
                }

                is CategoryEditOptionEvent.ReorderedCategory -> {
                    Log.e("ayhan", "CategoryEditScreenReorder: ${event.categoryList}")
                    viewModel.updateCategoryOrder(event.categoryList)
                }
            }
        })
    }

}
