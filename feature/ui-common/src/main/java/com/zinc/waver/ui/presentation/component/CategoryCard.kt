package com.zinc.waver.ui.presentation.component

import android.util.Log
import android.view.MotionEvent
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.zinc.waver.model.UICategoryInfo
import com.zinc.waver.ui.design.theme.Gray5
import com.zinc.waver.ui.design.theme.Gray7
import com.zinc.waver.ui.design.theme.Gray9
import com.zinc.waver.ui.design.theme.Main2
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.util.shadow

@Composable
fun CategoryListView(categoryInfoList: List<UICategoryInfo>, clicked: (UICategoryInfo) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(categoryInfoList, key = { category -> category.id }) {
            CategoryCard(
                categoryInfo = it,
                clicked = { info ->
                    clicked(info)
                }
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun CategoryCard(categoryInfo: UICategoryInfo, clicked: (UICategoryInfo) -> Unit) {

    val borderColor = remember { mutableStateOf(Color.Transparent) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = borderColor.value, shape = RoundedCornerShape(4.dp))
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        borderColor.value = Main2
                    }

                    MotionEvent.ACTION_UP -> {
                        borderColor.value = Color.Transparent
                        Log.e("ayhan", "category click 1")
                        clicked(categoryInfo)
                    }

                    MotionEvent.ACTION_CANCEL -> {
                        borderColor.value = Color.Transparent
                    }
                }
                true
            }
            .shadow(
                color = Gray5.copy(alpha = 0.2f),
                offsetX = (0).dp,
                offsetY = (0).dp,
                blurRadius = 4.dp,
            )
            .clip(RoundedCornerShape(4.dp)),
        shape = RoundedCornerShape(4.dp)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(start = 16.dp, end = 20.dp, top = 21.dp, bottom = 21.dp)
                .fillMaxWidth()
                .clickable {
                    Log.e("ayhan", "category click 1")
                    clicked(categoryInfo)
                }
        ) {

            val (leftContent, rightContent) = createRefs()

            // Right Content = SuccessButton
            CategoryRightView(
                modifier = Modifier.constrainAs(rightContent) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
                count = categoryInfo.count
            )

            // Left Contents
            CategoryLeftView(
                modifier = Modifier
                    .constrainAs(leftContent) {
                        end.linkTo(rightContent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        linkTo(
                            parent.start,
                            rightContent.start,
                            startMargin = 0.dp,
                            endMargin = 10.dp,
                            bias = 0f
                        )
                    }
                    .fillMaxWidth(.8f),
                name = categoryInfo.name
            )
        }
    }
}


@Composable
private fun CategoryRightView(modifier: Modifier = Modifier, count: String) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterEnd
    ) {
        CategoryCountView(count = count)
    }
}

@Composable
private fun CategoryCountView(modifier: Modifier = Modifier, count: String) {
    MyText(
        modifier = modifier,
        text = count,
        color = Gray7,
        fontSize = dpToSp(14.dp),
    )
}

@Composable
private fun CategoryLeftView(modifier: Modifier = Modifier, name: String) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        CategoryTextView(
            name = name
        )
    }
}


@Composable
private fun CategoryTextView(modifier: Modifier = Modifier, name: String) {
    MyText(
        modifier = modifier,
        text = name,
        color = Gray9,
        fontSize = dpToSp(14.dp),
    )
}

@Composable
@Preview
private fun CategoryListPreview() {
    CategoryListView(
        categoryInfoList = listOf(
            UICategoryInfo(id = 1, name = "여행", count = "10")
        )
    ) {

    }
}