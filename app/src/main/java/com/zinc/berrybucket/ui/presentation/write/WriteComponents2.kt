package com.zinc.berrybucket.ui.presentation.write

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.zinc.berrybucket.R
import com.zinc.berrybucket.model.WriteAddOption
import com.zinc.berrybucket.ui.compose.theme.Gray10
import com.zinc.berrybucket.ui.compose.theme.Gray3
import com.zinc.berrybucket.ui.compose.theme.Main3

@Composable
fun WriteTitleView(
    modifier: Modifier = Modifier, title: String
) {
    Text(
        modifier = modifier
            .padding(start = 28.dp, top = 28.dp, end = 28.dp)
            .fillMaxWidth(),
        text = title,
        fontSize = 24.sp,
        color = Gray10
    )
}

@Composable
fun WriteAddOptionView(
    modifier: Modifier,
    option: WriteAddOption, isLastItem: Boolean
) {

    Log.e("ayhan", "option  :$option, ${option.title}")
    Column(modifier = modifier) {
        Divider(color = Gray3)

        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (title, arrow, tag) = createRefs()

            Image(modifier = Modifier
                .constrainAs(arrow) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
                .padding(20.dp),
                painter = painterResource(R.drawable.ico_16_right),
                contentDescription = null)

            Text(text = option.title,
                modifier = Modifier
                    .constrainAs(title) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(arrow.start)
                        width = Dimension.fillToConstraints
                    }
                    .padding(start = 28.dp, top = 18.dp, end = 28.dp),
                color = Gray10,
                fontSize = 16.sp)

            Text(text = option.tagToString(),
                modifier = Modifier
                    .constrainAs(tag) {
                        top.linkTo(title.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(arrow.start)
                        width = Dimension.fillToConstraints
                    }
                    .padding(top = 12.dp, start = 28.dp, end = 28.dp, bottom = 20.dp),
                color = Main3,
                fontSize = 16.sp)
        }

        if (isLastItem) {
            Divider(color = Gray3)
        }
    }
}