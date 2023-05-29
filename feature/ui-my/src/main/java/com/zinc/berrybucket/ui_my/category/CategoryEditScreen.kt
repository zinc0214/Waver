package com.zinc.berrybucket.ui_my.category

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.zinc.berrybucket.ui_my.model.AddNewCategoryEvent
import com.zinc.common.models.CategoryInfo
import com.zinc.common.models.YesOrNo

@Composable
fun CategoryEditScreen(
    backClicked: () -> Unit
) {

    val addNewCategoryDialogShowAvailable = remember { mutableStateOf(false) } // 카테고리 추가 팝업 노출 여부

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
    }

    if (addNewCategoryDialogShowAvailable.value) {
        AddNewCategoryDialog(event = {
            when (it) {
                is AddNewCategoryEvent.AddNewCategory -> {
                    // add new category
                }

                AddNewCategoryEvent.Close -> addNewCategoryDialogShowAvailable.value = false
            }
        })
    }

    Column(modifier = Modifier.fillMaxWidth()) {

        CategoryEditTitleView(backClicked = {

        }) {

        }


        VerticalReorderList(categoryList = categoryItems, addNewCategory = {
            addNewCategoryDialogShowAvailable.value = true
        })

    }
}


@Composable
@Preview
private fun CategoryEditScreenPreview() {
    CategoryEditScreen {

    }
}