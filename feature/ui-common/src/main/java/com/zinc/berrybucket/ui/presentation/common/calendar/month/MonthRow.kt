package com.zinc.berrybucket.ui.presentation.common.calendar.month

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.zinc.berrybucket.ui.design.theme.Gray1
import com.zinc.berrybucket.ui.design.theme.Gray10
import com.zinc.berrybucket.ui.design.theme.Main2
import com.zinc.berrybucket.ui.presentation.common.IconButton
import com.zinc.berrybucket.ui.presentation.common.MyText
import com.zinc.berrybucket.ui.util.dpToSp
import com.zinc.berrybucket.ui_common.R

@RequiresApi(Build.VERSION_CODES.O)
@Composable
internal fun MonthWithYearView(
    year: Int,
    navigateMonthDrawableIds: Pair<Int, Int>,
    onNavigateMonthPressed: (Int, Int) -> Unit,
    changeViewType: () -> Unit,
    month: Int
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 28.dp)
            .background(Gray1)
            .padding(top = 4.dp)
    ) {

        val (leftButton, rightButton, yearAndMonth, divider) = createRefs()

        IconButton(
            onClick = {
                if (month == 1) {
                    onNavigateMonthPressed(12, year - 1)
                } else {
                    onNavigateMonthPressed(month - 1, year)
                }
            },
            image = navigateMonthDrawableIds.first,
            contentDescription = stringResource(R.string.monthDesc),
            modifier = Modifier.constrainAs(leftButton) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(yearAndMonth.start)
            }
        )

        MyText(
            text = "${year}년 " + parseMonth(month),
            fontSize = dpToSp(dp = 18.dp),
            color = Gray10,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .constrainAs(yearAndMonth) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .clickable {
                    changeViewType()
                }
        )

        Divider(
            color = Main2,
            thickness = 1.dp,
            modifier = Modifier
                .padding(top = 2.dp)
                .constrainAs(divider) {
                    top.linkTo(yearAndMonth.bottom)
                    start.linkTo(yearAndMonth.start)
                    end.linkTo(yearAndMonth.end)
                    width = Dimension.fillToConstraints
                }
        )

        IconButton(
            onClick = {
                if (month == 12) {
                    onNavigateMonthPressed(1, year + 1)
                } else {
                    onNavigateMonthPressed(month + 1, year)
                }
            },
            image = navigateMonthDrawableIds.second,
            contentDescription = stringResource(R.string.monthDesc),
            modifier = Modifier.constrainAs(rightButton) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(yearAndMonth.end)
            }
        )
    }
}

private fun parseMonth(month: Int): String {
    return if (month > 9) "${month}월" else "0${month}월"
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun MonthRowPreview() {
    MonthWithYearView(
        year = 2022,
        navigateMonthDrawableIds = Pair(
            R.drawable.btn_28_left_circle,
            R.drawable.btn_28_right_circle
        ),
        onNavigateMonthPressed = { a, b ->

        },
        month = 5, changeViewType = {}
    )
}