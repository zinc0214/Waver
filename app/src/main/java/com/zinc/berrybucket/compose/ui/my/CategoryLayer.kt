package com.zinc.berrybucket.compose.ui.my

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zinc.berrybucket.R
import com.zinc.berrybucket.compose.theme.Gray2
import com.zinc.berrybucket.compose.theme.Gray8
import com.zinc.berrybucket.compose.theme.Main4
import com.zinc.berrybucket.compose.ui.common.CategoryAddView
import com.zinc.berrybucket.compose.ui.common.CategoryListView
import com.zinc.berrybucket.compose.ui.common.EditButtonAndSearchImageView
import com.zinc.data.models.Category


@Composable
fun CategoryLayer(categoryList: List<Category>, recommendCategory: String) {
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TopView(modifier = Modifier.fillMaxWidth(), recommendCategory)
            Spacer(modifier = Modifier.height(16.dp))
            CategoryListView(categoryList)
            CategoryAddView()
        }
    }
}

@Composable
private fun TopView(modifier: Modifier = Modifier, recommendCategory: String) {
    Row(
        modifier = modifier
            .background(Gray2)
            .padding(start = 22.dp, end = 16.dp, top = 16.dp)
    ) {
        TopLeftView(
            modifier = Modifier.align(Alignment.CenterVertically),
            recommendCategory = recommendCategory
        )
        TopRightView(modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun TopLeftView(modifier: Modifier = Modifier, recommendCategory: String) {
    Row(modifier = modifier) {
        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = stringResource(id = R.string.recommendCategory),
            color = Gray8,
            fontSize = 13.sp
        )
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 8.dp),
            text = stringResource(id = R.string.recommendTag) + recommendCategory,
            color = Main4,
            fontSize = 13.sp
        )
    }

}

@Composable
private fun TopRightView(modifier: Modifier = Modifier) {
    EditButtonAndSearchImageView(
        modifier = modifier
            .fillMaxWidth(),
        editClicked = {},
        searchClicked = {}
    )
}