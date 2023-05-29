package com.zinc.berrybucket.ui_my.category

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zinc.berrybucket.ui.design.theme.Gray1
import com.zinc.berrybucket.ui.design.theme.Gray10
import com.zinc.berrybucket.ui.design.theme.Gray3
import com.zinc.berrybucket.ui.design.theme.Main1
import com.zinc.berrybucket.ui.design.theme.Main4
import com.zinc.berrybucket.ui.presentation.component.IconButton
import com.zinc.berrybucket.ui.presentation.component.MyText
import com.zinc.berrybucket.ui.presentation.component.TitleIconType
import com.zinc.berrybucket.ui.presentation.component.TitleView
import com.zinc.berrybucket.ui_my.R
import com.zinc.common.models.CategoryInfo
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable
import com.zinc.berrybucket.ui_common.R as CommonR

@Composable
internal fun CategoryEditTitleView(
    backClicked: () -> Unit,
    updateCategoryOrder: () -> Unit
) {
    TitleView(
        title = stringResource(id = R.string.categoryEditTitle),
        leftIconType = TitleIconType.BACK,
        isDividerVisible = true,
        onLeftIconClicked = {
            backClicked()
        },
        rightText = stringResource(id = CommonR.string.finishDesc),
        onRightTextClicked = {
            // TODO : 카테고리 편집 정보 저장 필요
        }
    )
}


@Composable
internal fun CategoryEditAddView(
    addNewCategory: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Main1)
            .clickable {
                addNewCategory()
            }
            .padding(horizontal = 26.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ico_20_add),
            contentDescription = null
        )
        MyText(
            modifier = Modifier.padding(start = 6.dp),
            fontSize = 16.sp,
            color = Main4,
            fontWeight = FontWeight.Bold,
            text = stringResource(id = R.string.categoryAdd)
        )
    }
}

@Composable
fun VerticalReorderList(
    categoryList: List<CategoryInfo>,
    addNewCategory: () -> Unit
) {
    val data = remember { mutableStateOf(categoryList) }
    val state = rememberReorderableLazyListState(onMove = { from, to ->
        data.value = data.value.toMutableList().apply {
            Log.e("ayhan", "from : $from to : $to")
            add(to.index - 1, removeAt(from.index - 1))
        }
    })
    LazyColumn(
        state = state.listState,
        modifier = Modifier.run {
            reorderable(state)
                .detectReorderAfterLongPress(state)
        }
    ) {
        item {
            CategoryEditAddView(addNewCategory)
        }
        items(data.value, { it.id }) { item ->
            ReorderableItem(state, key = item, orientationLocked = false) {
                EditCategoryItemView(item)
            }
        }
    }
}

@Composable
private fun EditCategoryItemView(item: CategoryInfo) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Gray1)
                .padding(start = 28.dp, top = 16.dp, bottom = 16.dp, end = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MyText(text = item.name, color = Gray10, fontSize = 16.sp)
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                modifier = Modifier
                    .padding(4.dp)
                    .size(24.dp),
                onClick = {

                },
                image = R.drawable.btn_24_more,
                contentDescription = stringResource(id = R.string.categoryItemEdit)
            )
            IconButton(
                modifier = Modifier
                    .padding(4.dp)
                    .size(24.dp),
                onClick = {
                    // 드래그 ...
                },
                image = R.drawable.btn_24_move,
                contentDescription = stringResource(id = R.string.categoryItemEdit)
            )
        }

        Divider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = Gray3)
    }

}


@Composable
@Preview
private fun CategoryEditAddPreview() {
    CategoryEditAddView {

    }
}