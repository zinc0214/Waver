package com.zinc.berrybucket.ui.presentation.common.calendar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zinc.berrybucket.ui.design.theme.Gray1
import com.zinc.berrybucket.ui.design.theme.Main4
import com.zinc.berrybucket.ui.presentation.common.calendar.daysofweek.DaysOfTheWeekRow
import com.zinc.berrybucket.ui.presentation.common.calendar.model.CalendarDate
import com.zinc.berrybucket.ui.presentation.common.calendar.month.MonthWithYearView
import com.zinc.berrybucket.ui.presentation.common.calendar.week.WeekRows
import com.zinc.berrybucket.ui_common.R
import java.time.LocalDate

var lengthOfWeek: Int = 0

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarView(
    year: Int = 0,
    month: Int = 0,
    onDayPressed: ((Long) -> Unit)? = null,
    onNavigateMonthPressed: (Int, Int) -> Unit = { _, _ -> /*no op*/ },
    changeViewType: () -> Unit,
    selectedDates: Collection<CalendarDate> = setOf(),
    navigateMonthDrawableIds: Pair<Int, Int> = Pair(
        R.drawable.btn_28_left_circle, R.drawable.btn_28_right_circle
    ),
    dateCircleDiameter: Dp = 32.dp
    // todo (fvalela - #2): allow user to adjust what start and end day is (weekly view, for example)
    //    startDay: Int = 1,
    //    endDay: Int = 100,
) {
    val startDay = 1
    val endDay = 31

    val today = LocalDate.now()
    val resolvedMonth = if (month < 1 || month > 12) today.monthValue else month
    val resolvedYear = if (month < 1 || year < 1) today.year else year

    val lengthOfCurrentMonth = LocalDate.of(resolvedYear, resolvedMonth, 1).lengthOfMonth()
    val datePressed = remember { mutableStateOf(LocalDate.now().toEpochDay()) }

    val resolvedStartDate = resolveLocalDate(
        resolvedYear, resolvedMonth, maybeResolveDay(startDay, lengthOfCurrentMonth, false)
    )

    val resolvedEndDate = resolveLocalDate(
        resolvedYear, resolvedMonth, maybeResolveDay(endDay, lengthOfCurrentMonth, true)
    )
    assert(resolvedStartDate.isBefore(resolvedEndDate))

    lengthOfWeek = calculateLengthOfWeek(resolvedStartDate, resolvedEndDate)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Gray1)
            .padding(28.dp)
    ) {
        MonthWithYearView(
            year = resolvedYear,
            month = resolvedMonth,
            navigateMonthDrawableIds = navigateMonthDrawableIds,
            onNavigateMonthPressed = onNavigateMonthPressed,
            changeViewType = changeViewType
        )

        DaysOfTheWeekRow(
            startDate = resolvedStartDate,
            endDate = resolvedEndDate,
        )

        WeekRows(
            startDate = resolvedStartDate,
            endDate = resolvedEndDate,
            onDayPressed = {
                datePressed.value = it
                onDayPressed?.invoke(it)
            },
            selectedDates = selectedDates,
            dateCircleDiameter = dateCircleDiameter,
        )
    }
}

private fun maybeResolveDay(
    unresolvedDay: Int,
    lengthOfCurrentMonth: Int,
    isEndDate: Boolean = false,
): Int {
    if (unresolvedDay < 1 || unresolvedDay > lengthOfCurrentMonth) {
        return if (isEndDate) lengthOfCurrentMonth else 1
    }
    return unresolvedDay
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {

    val datePressed = remember { mutableStateOf(LocalDate.now().toEpochDay()) }
    CalendarView(
        month = 3, year = 2022, selectedDates = listOf(
            CalendarDate(
                dateInMilli = LocalDate.now().toEpochDay(),
                backgroundColour = Main4,
                backgroundShape = RoundedCornerShape(10.dp)
            )
        ), onDayPressed = { newDayPressed ->
            // i.e. update viewModel
            datePressed.value = newDayPressed
        },
        changeViewType = {}
    )
}
