package com.zinc.berrybucket.ui_write.presentation.bottomScreens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.hana.berrybucket.ui_write.R
import com.zinc.berrybucket.ui.design.theme.Gray4
import com.zinc.berrybucket.ui.design.theme.Main4
import com.zinc.berrybucket.ui.presentation.component.MyText
import com.zinc.berrybucket.ui.util.dpToSp
import com.zinc.berrybucket.ui_write.viewmodel.WriteCategoryViewModel
import com.zinc.common.models.CategoryInfo
import com.zinc.common.models.YesOrNo

@Composable
fun CategorySelectBottomScreen(
    confirmed: (CategoryInfo) -> Unit
) {
    val viewModel: WriteCategoryViewModel = hiltViewModel()
    val categoryList by viewModel.categoryInfoList.observeAsState()
    viewModel.loadCategoryList()

    categoryList?.let {
        CategorySelectView(it) { category ->
            confirmed(category)
        }
    }
}

@Composable
private fun CategorySelectView(
    categoryInfoList: List<CategoryInfo>,
    confirmed: (CategoryInfo) -> Unit
) {
    if (categoryInfoList.isNotEmpty()) {

        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (category, addButton) = createRefs()

            LazyColumn(
                modifier = Modifier.constrainAs(category) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(addButton.top)
                },
                contentPadding = PaddingValues(
                    start = 28.dp,
                    end = 28.dp,
                    top = 39.dp,
                    bottom = 26.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = categoryInfoList, key = { keyCategory ->
                    keyCategory.id
                }, itemContent = { category ->
                    MyText(
                        text = category.name,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                confirmed(category)
                            }
                            .padding(vertical = 10.dp)
                    )
                })
            }


            Column(modifier = Modifier
                .fillMaxWidth()
                .constrainAs(addButton) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }) {

                Divider(color = Gray4, thickness = 1.dp)
                MyText(
                    text = stringResource(id = R.string.optionCategoryAddButton),
                    color = Main4,
                    fontSize = dpToSp(dp = 15.dp),
                    style = TextStyle(textAlign = TextAlign.Center),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 28.dp, vertical = 18.dp)
                )
            }
        }
    }
}


@Composable
@Preview
private fun CategorySelectBottomScreenPreview() {
    CategorySelectView(categoryInfoList = buildList {
        add(CategoryInfo(id = 0, name = "여행", defaultYn = YesOrNo.Y, bucketlistCount = "10"))
        add(CategoryInfo(id = 11, name = "여행234", defaultYn = YesOrNo.Y, bucketlistCount = "10"))
    }, confirmed = {})
}