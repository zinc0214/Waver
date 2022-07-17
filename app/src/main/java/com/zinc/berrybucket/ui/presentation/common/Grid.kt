package com.zinc.berrybucket.ui.presentation.common

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlin.math.min

fun <T> LazyListScope.gridItems(
    data: List<T>,
    columnCount: Int,
    modifier: Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    itemContent: @Composable BoxScope.(T) -> Unit,
) {
    val size = data.count()
    val rows = if (size == 0) 0 else 1 + (size - 1) / columnCount
    if (rows > 0) {
        Log.e("ayhan", " { data[it].hashCode() : ${data[0].hashCode()} }")
    }
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

@Composable
fun <T> gridItems2(
    data: List<T>,
    maxRow: Int,
    modifier: Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    itemContent: @Composable (T) -> Unit,
) {

    // 만약에 아이템이  24개고, 한 줄에 넣고싶은게 4개면
    // 전체 줄의 수는 = 6줄

    // 전체 줄의 수
    val columCount = data.size / maxRow

    Column(modifier = modifier) {
        // 현재 줄부터 전체 줄까지 그리기
        if (columCount > 0) {
            for (columnIndex in 0 until columCount) {
                Column(
                    verticalArrangement = verticalArrangement
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = horizontalArrangement
                    ) {
                        val startIndex = columnIndex * maxRow
                        val endIndex = min(startIndex + 4, data.size)
                        val rowItems = data.subList(startIndex, endIndex)
                        rowItems.forEach { t ->
                            itemContent(t)
                        }
                    }
                    //   Spacer(Modifier.height(8.dp))
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