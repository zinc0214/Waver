package com.zinc.berrybucket.ui.presentation.write.options

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.zinc.berrybucket.model.WriteOption
import com.zinc.berrybucket.ui.compose.theme.Gray1
import com.zinc.berrybucket.ui.compose.theme.Gray10
import com.zinc.berrybucket.ui.compose.theme.Gray4
import com.zinc.berrybucket.ui.compose.theme.Gray8

@Composable
fun OptionScreen(options: List<WriteOption>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 28.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items = options, key = { it.title }, itemContent = { option ->
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Gray1, shape = RoundedCornerShape(4.dp))
                    .border(width = 1.dp, color = Gray4, shape = RoundedCornerShape(4.dp))
            ) {
                val (title, content) = createRefs()

                Text(
                    modifier = Modifier
                        .padding(vertical = 21.dp, horizontal = 16.dp)
                        .constrainAs(title) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                        },
                    fontSize = 14.sp,
                    color = Gray8,
                    text = option.title
                )

                Text(
                    modifier = Modifier
                        .padding(vertical = 21.dp, horizontal = 20.dp)
                        .constrainAs(content) {
                            start.linkTo(title.start)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints
                        },
                    fontSize = 15.sp,
                    color = Gray10,
                    text = option.content,
                    textAlign = TextAlign.End
                )
            }
        })
    }
}