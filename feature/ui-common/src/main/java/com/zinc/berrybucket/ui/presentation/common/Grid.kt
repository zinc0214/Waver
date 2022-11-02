package com.zinc.berrybucket.ui.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import kotlin.math.ceil
import kotlin.math.min
import kotlin.math.roundToInt

fun <T> LazyListScope.gridItems(
    data: List<T>,
    columnCount: Int,
    modifier: Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    itemContent: @Composable BoxScope.(T) -> Unit,
) {
    val size = data.count()
    val rows = if (size == 0) 0 else 1 + (size - 1) / columnCount
    items(rows, key = { data[it].hashCode() }) { rowIndex ->
        Row(
            horizontalArrangement = horizontalArrangement,
            modifier = modifier
        ) {
            for (columnIndex in 0 until columnCount) {
                val itemIndex = rowIndex * columnCount + columnIndex
                if (itemIndex < size) {
                    Box(
                        modifier = Modifier.weight(1F, fill = true),
                        propagateMinConstraints = true
                    ) {
                        itemContent(data[itemIndex])
                    }
                } else {
                    Spacer(Modifier.weight(1F, fill = true))
                }
            }
        }
    }
}

/**
 * GridLazyColumn 을 사용하지 못하는 상황에서
 * 쓸 수 있는 뷰
 *
 * @param data // 데이터 정보
 * @param maxRow // 한 줄에 들어갈 수 있는 최대 아이템 수
 * @param modifier
 * @param horizontalArrangement // 아이템 간의 Arrangement 속성 값
 * @param verticalSpace // 상하 column 간의 여백이 필요할 때
 * @param itemContent // 그려질 아이템
 * @param emptyContent // 좌우 여백이 항상 동일해야 하는 경우, 빈 공간을 미리 그리도록 함
 */
@Composable
fun <T> gridItems(
    data: List<T>,
    maxRow: Int,
    modifier: Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalSpace: Dp,
    itemContent: @Composable (T) -> Unit,
    emptyContent: @Composable () -> Unit
) {

    // 만약에 아이템이  24개고, 한 줄에 넣고싶은게 4개면
    // 전체 줄의 수는 = 6줄
    // 전체 줄의 수
    val columCount = ceil(data.size / maxRow.toFloat()).roundToInt()

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        // 현재 줄부터 전체 줄까지 그리기
        if (columCount > 0) {
            for (columnIndex in 0 until columCount) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = horizontalArrangement
                    ) {
                        val startIndex = columnIndex * maxRow
                        val endIndex = min(startIndex + maxRow, data.size)
                        val rowItems = data.subList(startIndex, endIndex)

                        // 빈 공간을 채우기 위함
                        val emptyViews = maxRow - rowItems.size

                        rowItems.forEach { t ->
                            itemContent(t)
                        }

                        repeat(emptyViews) {
                            emptyContent()
                        }
                    }
                    Spacer(modifier = modifier.height(verticalSpace))
                }
            }
        } else {
            Row(
                horizontalArrangement = horizontalArrangement,
                modifier = modifier
            ) {
                data.forEachIndexed { index, t ->
                    itemContent(t)
                }
            }
        }
    }
}