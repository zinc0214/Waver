package com.zinc.berrybucket.compose.ui.component

import android.util.Log
import android.view.MotionEvent
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.zinc.berrybucket.compose.theme.Gray7
import com.zinc.berrybucket.compose.theme.Gray9
import com.zinc.berrybucket.compose.theme.Main2
import com.zinc.data.models.Category

@Composable
fun CategoryListView(categoryList: List<Category>) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        categoryList.forEach { category ->
            CategoryCard(
                category = category,
                clicked = {}
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CategoryCard(category: Category, clicked: () -> Unit) {

    val borderColor = remember { mutableStateOf(Color.Transparent) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = borderColor.value, shape = RoundedCornerShape(4.dp))
            .pointerInteropFilter {
                Log.e("ayhan", "motionEvent : ${it.action}")
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        borderColor.value = Main2
                    }

                    MotionEvent.ACTION_UP,
                    MotionEvent.ACTION_CANCEL -> {
                        borderColor.value = Color.Transparent
                    }
                }
                true
            }
            .clickable { clicked() },
        shape = RoundedCornerShape(4.dp),
        elevation = 2.dp
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(start = 16.dp, end = 20.dp, top = 21.dp, bottom = 21.dp)
                .fillMaxWidth()
        ) {

            val (leftContent, rightContent) = createRefs()

            // Right Content = SuccessButton
            CategoryRightView(
                modifier = Modifier.constrainAs(rightContent) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
                count = category.count
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
                name = category.name
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
    Text(
        modifier = modifier,
        text = count,
        color = Gray7,
        fontSize = 14.sp
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
    Text(
        modifier = modifier,
        text = name,
        color = Gray9,
        fontSize = 14.sp
    )
}