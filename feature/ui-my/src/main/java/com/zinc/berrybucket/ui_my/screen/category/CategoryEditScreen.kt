package com.zinc.berrybucket.ui_my.screen.category

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.zinc.berrybucket.ui.presentation.component.dialog.ApiFailDialog
import com.zinc.berrybucket.ui_my.model.AddCategoryEvent
import com.zinc.berrybucket.ui_my.model.CategoryEditOptionEvent
import com.zinc.berrybucket.ui_my.model.EditCategoryNameEvent
import com.zinc.berrybucket.ui_my.viewModel.CategoryViewModel
import com.zinc.common.models.CategoryInfo

@Composable
fun CategoryEditScreen(
    backClicked: () -> Unit,
    viewModel: CategoryViewModel = hiltViewModel()
) {

    val categoryList by viewModel.categoryInfoList.observeAsState()
    val apiFailed by viewModel.apiFailed.observeAsState()

    if (categoryList.isNullOrEmpty()) {
        viewModel.loadCategoryList()
    }

    val addNewCategoryDialogShowAvailable = remember { mutableStateOf(false) } // 카테고리 추가 팝업 노출 여부
    val editCategoryNameDialogShowAvailable =
        remember { mutableStateOf<CategoryInfo?>(null) } // 카테고리 이름 편집 팝업 노출 여부
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
        apiFailState.value?.let { failData ->
            ApiFailDialog(failData.first, failData.second) {
                apiFailDialogShow.value = false
            }
        }
    }

    if (addNewCategoryDialogShowAvailable.value) {
        AddNewCategoryDialog(event = {
            when (it) {
                is AddCategoryEvent.AddNewAddCategory -> {
                    addNewCategoryDialogShowAvailable.value = false
                    viewModel.addNewCategory(it.name)
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


    Column(modifier = Modifier.fillMaxWidth()) {

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
