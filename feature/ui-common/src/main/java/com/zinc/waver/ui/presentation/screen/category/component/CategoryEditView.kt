package com.zinc.waver.ui.presentation.screen.category.component

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import com.zinc.waver.model.UICategoryInfo
import com.zinc.waver.ui.design.theme.Error2
import com.zinc.waver.ui.design.theme.Gray1
import com.zinc.waver.ui.design.theme.Gray10
import com.zinc.waver.ui.design.theme.Gray3
import com.zinc.waver.ui.design.theme.Main1
import com.zinc.waver.ui.design.theme.Main4
import com.zinc.waver.ui.presentation.component.IconButton
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.presentation.component.TitleIconType
import com.zinc.waver.ui.presentation.component.TitleView
import com.zinc.waver.ui.presentation.screen.category.model.CategoryEditOptionEvent
import com.zinc.waver.ui.util.dpToSp
import kotlinx.coroutines.launch
import com.zinc.waver.ui_common.R as CommonR

@Composable
internal fun CategoryEditTitleView(
    backClicked: () -> Unit,
    updateCategoryOrder: () -> Unit
) {
    TitleView(
        title = stringResource(id = CommonR.string.categoryEditTitle),
        leftIconType = TitleIconType.BACK,
        isDividerVisible = true,
        onLeftIconClicked = {
            backClicked()
        },
        rightText = stringResource(id = CommonR.string.finishDesc),
        onRightTextClicked = {
            updateCategoryOrder()
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
            painter = painterResource(id = CommonR.drawable.ico_20_add),
            contentDescription = null
        )
        MyText(
            modifier = Modifier.padding(start = 6.dp),
            fontSize = 16.sp,
            color = Main4,
            fontWeight = FontWeight.Bold,
            text = stringResource(id = CommonR.string.categoryAdd)
        )
    }
}

@Composable
fun VerticalReorderList(
    categoryList: List<UICategoryInfo>,
    addNewCategory: () -> Unit,
    optionEvent: (CategoryEditOptionEvent) -> Unit
) {
    //val data = remember { categoryList.toMutableList() }

    // 내부 관찰 가능한 리스트 유지
    val items: SnapshotStateList<UICategoryInfo> = remember { mutableStateListOf() }

    // 외부에서 전달된 categoryList가 바뀌면 내부 상태를 동기화
    LaunchedEffect(categoryList) {
        items.clear()
        items.addAll(categoryList)
    }

    // 예: 드래그 콜백에서 items.move(...) 를 호출하고 변경된 리스트 전파
    // 실제 UI/Drag&Drop 구현 부분은 기존 코드와 결합해서 사용
    // 아래는 이동 처리 예시 함수 사용 방법
    fun onReorder(from: Int, to: Int) {
        items.move(from, to)
        optionEvent(CategoryEditOptionEvent.ReorderedCategory(items.toList()))
    }

    Column(modifier = Modifier.fillMaxSize()) {

        CategoryEditAddView(addNewCategory)

        val scope = rememberCoroutineScope()
        val listState =
            rememberDragDropListState(onMove = { from, to ->
                items.move(from, to)
                Log.e("ayhan", "VerticalReorderListOnMove: ${items.toList()}")
                optionEvent(CategoryEditOptionEvent.ReorderedCategory(items.toList()))
            })

        LazyColumn(
            state = listState.lazyListState,
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGesturesAfterLongPress(
                        onDragStart = { offset -> listState.onDragStart(offset) },
                        onDragEnd = {
                            listState.onDragInterrupted()
                        },
                        onDragCancel = { listState.onDragInterrupted() },
                        onDrag = { change, offset ->
                            change.consume()
                            listState.onDrag(offset)
                            if (listState.overscrollJob?.isActive == true) return@detectDragGesturesAfterLongPress
                            listState.checkForOverScroll().takeIf { it != 0f }?.let {
                                listState.overscrollJob =
                                    scope.launch { listState.lazyListState.scrollBy(it) }
                            } ?: listState.overscrollJob?.cancel()
                        }
                    )
                }
        ) {
            itemsIndexed(items) { index, category ->
                EditCategoryItemView(
                    Modifier
                        .zIndex(if (index == listState.currentIndexOfDraggedItem) 1f else 0f)
                        .graphicsLayer {
                            translationY =
                                listState.elementDisplacement.takeIf { index == listState.currentIndexOfDraggedItem }
                                    ?: 0f
                            shadowElevation =
                                4.dp.toPx().takeIf { index == listState.currentIndexOfDraggedItem }
                                    ?: 0f
                        },
                    index,
                    category,
                    optionEvent = optionEvent
                )
            }
        }
    }
}

fun <T> MutableList<T>.move(from: Int, to: Int) {
    if (from == to) return
    val element = this.removeAt(from) ?: return
    this.add(to, element)
}

@Composable
private fun EditCategoryItemView(
    modifier: Modifier,
    index: Int,
    item: UICategoryInfo,
    optionEvent: (CategoryEditOptionEvent) -> Unit
) {

    val expandedMenuIndex = remember { mutableStateOf(-1) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 1.dp)
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
                    .size(24.dp)
                    .rotate(if (expandedMenuIndex.value == index) 180f else 0f),
                onClick = {
                    expandedMenuIndex.value = index
                },
                image = CommonR.drawable.btn_24_more,
                contentDescription = stringResource(id = CommonR.string.categoryItemEdit)
            )
            IconButton(
                modifier = Modifier
                    .padding(4.dp)
                    .size(24.dp),
                onClick = {
                    // 드래그 ...
                },
                image = CommonR.drawable.btn_24_move,
                contentDescription = stringResource(id = CommonR.string.categoryItemEdit)
            )
        }

        Divider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = Gray3)

        Card(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.TopEnd),
            backgroundColor = Color.Transparent,
            elevation = 1.dp
        ) {
            MaterialTheme(shapes = MaterialTheme.shapes.copy(medium = RoundedCornerShape(8.dp))) {

                if (expandedMenuIndex.value == index) {
                    DropdownMenu(
                        expanded = true,
                        onDismissRequest = { expandedMenuIndex.value = -1 },
                        offset = DpOffset(58.dp, (-30).dp),
                        properties = PopupProperties(clippingEnabled = false)
                    ) {

                        MyText(
                            text = stringResource(id = CommonR.string.categoryNameEdit),
                            color = Gray10,
                            fontSize = dpToSp(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    optionEvent.invoke(
                                        CategoryEditOptionEvent.EditCategoryName(
                                            item
                                        )
                                    )
                                    expandedMenuIndex.value = -1
                                }
                                .padding(
                                    start = 16.dp,
                                    end = 38.dp,
                                    top = 8.dp,
                                    bottom = 8.dp
                                )
                        )

                        if (item.defaultYn.isNo()) {
                            MyText(
                                text = stringResource(id = CommonR.string.categoryDelete),
                                color = Error2,
                                fontSize = dpToSp(14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        optionEvent.invoke(
                                            CategoryEditOptionEvent.DeleteCategory(
                                                item
                                            )
                                        )
                                        expandedMenuIndex.value = -1
                                    }
                                    .padding(
                                        start = 16.dp,
                                        end = 38.dp,
                                        top = 8.dp,
                                        bottom = 8.dp
                                    )
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
@Preview
private fun CategoryEditAddPreview() {
    CategoryEditAddView {

    }
}