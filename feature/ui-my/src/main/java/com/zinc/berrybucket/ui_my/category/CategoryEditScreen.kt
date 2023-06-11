package com.zinc.berrybucket.ui_my.category

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.zinc.berrybucket.ui_my.model.AddCategoryEvent
import com.zinc.berrybucket.ui_my.model.CategoryEditOptionEvent
import com.zinc.berrybucket.ui_my.model.EditCategoryNameEvent
import com.zinc.berrybucket.ui_my.viewModel.CategoryViewModel
import com.zinc.common.models.CategoryInfo

@Composable
fun CategoryEditScreen(
    backClicked: () -> Unit
) {

    val viewModel: CategoryViewModel = hiltViewModel()
    val categoryList by viewModel.categoryInfoList.observeAsState()
    viewModel.loadCategoryList()

    val addNewCategoryDialogShowAvailable = remember { mutableStateOf(false) } // 카테고리 추가 팝업 노출 여부
    val editCategoryNameDialogShowAvailable =
        remember { mutableStateOf<CategoryInfo?>(null) } // 카테고리 이름 편집 팝업 노출 여부


    val categoryItemState = remember { mutableStateOf(categoryList) }

    if (addNewCategoryDialogShowAvailable.value) {
        AddNewCategoryDialog(event = {
            when (it) {
                is AddCategoryEvent.AddNewAddCategory -> {
                    editCategoryNameDialogShowAvailable.value = null
                    // add new category
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
                        val updateCategory =
                            categoryItemState.value?.firstOrNull { item -> item.id == it.categoryInfo.id }
                        if (updateCategory != null) {
                            viewModel.editCategory(updateCategory)
                        }
                    }
                }
            })
    }


    Column(modifier = Modifier.fillMaxWidth()) {

        CategoryEditTitleView(backClicked = {
            backClicked()
        }) {

        }

        categoryList?.let { categoryList ->
            VerticalReorderList(categoryList = categoryList, addNewCategory = {
                addNewCategoryDialogShowAvailable.value = true
            }, optionEvent = { event ->
                when (event) {
                    is CategoryEditOptionEvent.DeleteCategory -> {

                    }

                    is CategoryEditOptionEvent.EditCategoryName -> {
                        editCategoryNameDialogShowAvailable.value = event.categoryInfo
                    }
                }
            })
        }
    }

}


@Composable
@Preview
private fun CategoryEditScreenPreview() {
    CategoryEditScreen {

    }
}