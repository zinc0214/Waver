package com.zinc.berrybucket.ui_my.category

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.zinc.common.models.CategoryInfo
import com.zinc.common.models.YesOrNo

@Composable
fun CategoryEditScreen(
    backClicked: (Boolean) -> Unit
) {

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

    Column(modifier = Modifier.fillMaxWidth()) {

        CategoryEditTitleView(backClicked = {

        }) {

        }

        CategoryEditAddView {

        }
        EditCategoryListView(items = categoryItems)

    }
}


@Composable
@Preview
private fun CategoryEditScreenPreview() {
    CategoryEditScreen {

    }
}