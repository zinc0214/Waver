package com.zinc.berrybucket.ui_my.category

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zinc.berrybucket.model.MyPagerClickEvent
import com.zinc.berrybucket.model.MyTabType
import com.zinc.berrybucket.ui.design.theme.Gray8
import com.zinc.berrybucket.ui.design.theme.Main4
import com.zinc.berrybucket.ui.presentation.component.CategoryListView
import com.zinc.berrybucket.ui.presentation.component.MyText
import com.zinc.berrybucket.ui.util.dpToSp
import com.zinc.berrybucket.ui_my.EditButtonAndSearchImageView
import com.zinc.berrybucket.ui_my.R
import com.zinc.berrybucket.ui_my.viewModel.MyViewModel

@Composable
fun CategoryLayer(
    modifier: Modifier = Modifier,
    viewModel: MyViewModel,
    clickEvent: (MyPagerClickEvent) -> Unit
) {
    val recommendCategory = "여향"

    viewModel.loadCategoryList()
    val categoryList by viewModel.categoryInfoItems.observeAsState()

    categoryList?.let {
        Column(
            modifier = modifier
        ) {
            TopView(modifier = Modifier.fillMaxWidth(), recommendCategory, clickEvent)
            Spacer(modifier = Modifier.height(16.dp))
            CategoryListView(it)
            CategoryAddView()
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
            clickEvent(MyPagerClickEvent.CategoryEditClicked)
        },
        searchClicked = {
            clickEvent(MyPagerClickEvent.SearchClicked(tabType = MyTabType.CATEGORY))
        }
    )
}