package com.zinc.berrybucket.ui.presentation.write.options

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.zinc.berrybucket.model.WriteOption
import com.zinc.berrybucket.model.WriteOptionsType1
import com.zinc.berrybucket.ui.design.theme.Gray1
import com.zinc.berrybucket.ui.design.theme.Gray10
import com.zinc.berrybucket.ui.design.theme.Gray4
import com.zinc.berrybucket.ui.design.theme.Gray8
import com.zinc.berrybucket.ui.presentation.component.MyText
import com.zinc.berrybucket.ui.util.dpToSp

@Composable
fun OptionScreen(options: List<WriteOption>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 28.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val showableOptions =
            options.filterNot { it.type == WriteOptionsType1.IMAGE || it.type == WriteOptionsType1.MEMO }
        showableOptions.forEach { option ->
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Gray1, shape = RoundedCornerShape(4.dp))
                    .border(width = 1.dp, color = Gray4, shape = RoundedCornerShape(4.dp))
            ) {
                val (title, content) = createRefs()

                MyText(
                    modifier = Modifier
                        .padding(vertical = 21.dp, horizontal = 16.dp)
                        .constrainAs(title) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                        },
                    fontSize = dpToSp(14.dp),
                    color = Gray8,
                    text = option.title
                )

                MyText(
                    modifier = Modifier
                        .padding(vertical = 21.dp, horizontal = 20.dp)
                        .constrainAs(content) {
                            start.linkTo(title.start)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints
                        },
                    fontSize = dpToSp(15.dp),
                    color = Gray10,
                    text = option.content(),
                    textAlign = TextAlign.End
                )
            }
        }

    }
}