package com.zinc.waver.ui.presentation.component.calendar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zinc.waver.ui.design.theme.Gray1
import com.zinc.waver.ui.presentation.component.calendar.daysofweek.DaysOfTheWeekRow
import com.zinc.waver.ui.presentation.component.calendar.month.MonthWithYearView
import com.zinc.waver.ui.presentation.component.calendar.week.WeekRows
import com.zinc.waver.ui_common.R
import java.time.LocalDate

var lengthOfWeek: Int = 0

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarView(
    year: Int = 0,
    month: Int = 0,
    onDayPressed: ((LocalDate) -> Unit)? = null,
    onNavigateMonthPressed: (Int, Int) -> Unit = { _, _ -> /*no op*/ },
    changeViewType: () -> Unit,
    selectedDates: Collection<LocalDate> = setOf(),
    navigateMonthDrawableIds: Pair<Int, Int> = Pair(
        R.drawable.btn_28_left_circle, R.drawable.btn_28_right_circle
    ),
    dateCircleDiameter: Dp = 32.dp
) {
    val startDay = 1
    val endDay = 31

    val today = LocalDate.now()
    val resolvedMonth = if (month < 1 || month > 12) today.monthValue else month
    val resolvedYear = if (month < 1 || year < 1) today.year else year

    val lengthOfCurrentMonth = LocalDate.of(resolvedYear, resolvedMonth, 1).lengthOfMonth()
    val datePressed = remember { mutableStateOf(LocalDate.now()) }

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

    val datePressed = remember { mutableStateOf(LocalDate.now()) }
    CalendarView(
        month = 3,
        year = 2022,
        selectedDates = listOf(LocalDate.now()),
        onDayPressed = { newDayPressed ->
            // i.e. update viewModel
            datePressed.value = newDayPressed
        },
        changeViewType = {}
    )
}
