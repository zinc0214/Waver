package com.zinc.berrybucket.ui_my.category

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.zinc.berrybucket.ui_my.model.AddCategoryEvent
import com.zinc.berrybucket.ui_my.model.CategoryEditOptionEvent
import com.zinc.berrybucket.ui_my.model.EditCategoryNameEvent
import com.zinc.common.models.CategoryInfo
import com.zinc.common.models.YesOrNo

@Composable
fun CategoryEditScreen(
    backClicked: () -> Unit
) {

    val addNewCategoryDialogShowAvailable = remember { mutableStateOf(false) } // 카테고리 추가 팝업 노출 여부
    val editCategoryNameDialogShowAvailable =
        remember { mutableStateOf<CategoryInfo?>(null) } // 카테고리 이름 편집 팝업 노출 여부

    val categoryItems = buildList {
        repeat(50) {
            add(
                CategoryInfo(
                    id = it,
                    name = "여행 + $it",
                    defaultYn = YesOrNo.N,
                    bucketlistCount = "1"
                )
            )
        }
    }.toMutableList()

    val categoryItemState = remember { mutableStateOf(categoryItems) }

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
                            categoryItemState.value.indexOfFirst { item -> item.id == it.categoryInfo.id }
                        if (updateCategory != -1) {
                            categoryItemState.value[updateCategory] = it.categoryInfo
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


        VerticalReorderList(categoryList = categoryItemState.value, addNewCategory = {
            addNewCategoryDialogShowAvailable.value = true
        }, optionEvent = {
            when (it) {
                is CategoryEditOptionEvent.DeleteCategory -> {

                }

                is CategoryEditOptionEvent.EditCategoryName -> {
                    editCategoryNameDialogShowAvailable.value = it.categoryInfo
                }
            }
        })

    }
}


@Composable
@Preview
private fun CategoryEditScreenPreview() {
    CategoryEditScreen {

    }
}