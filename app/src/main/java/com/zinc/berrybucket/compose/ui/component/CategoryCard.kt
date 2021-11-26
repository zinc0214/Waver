package com.zinc.berrybucket.compose.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.zinc.berrybucket.compose.theme.Gray7
import com.zinc.berrybucket.compose.theme.Gray9
import com.zinc.data.models.Category

@Composable
fun CategoryCard(category: Category, clicked: () -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
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
            Box(
                modifier = Modifier.constrainAs(rightContent) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
                contentAlignment = Alignment.CenterEnd
            ) {
                CategoryCountView(count = category.count)
            }

            // Left Contents
            Column(
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
                horizontalAlignment = Alignment.Start
            ) {
                CategoryTextView(
                    name = category.name
                )
            }
        }
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
private fun CategoryTextView(modifier: Modifier = Modifier, name: String) {
    Text(
        modifier = modifier,
        text = name,
        color = Gray9,
        fontSize = 14.sp
    )
}