package com.zinc.berrybucket.ui.presentation.common.calendar.week

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zinc.berrybucket.ui.presentation.common.calendar.DAY_IN_MILLI
import com.zinc.berrybucket.ui.presentation.common.calendar.date.SingleDate
import com.zinc.berrybucket.ui.presentation.common.calendar.differenceBetweenTimeLibEndDayOfWeekAndPassedEndDayOfWeek
import com.zinc.berrybucket.ui.presentation.common.calendar.differenceBetweenTimeLibStartDayOfWeekAndPassedStartDayOfWeek
import com.zinc.berrybucket.ui.presentation.common.calendar.lengthOfWeek
import com.zinc.berrybucket.ui.presentation.common.calendar.model.CalendarDate
import com.zinc.berrybucket.ui.presentation.common.calendar.ui.DateTextStyle
import java.time.LocalDate
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
@Composable
internal fun WeekRows(
    startDate: LocalDate,
    endDate: LocalDate,
    onDayPressed: ((Long) -> Unit)?,
    selectedDates: Collection<CalendarDate>,
    dateCircleDiameter: Dp,
) {
    // todo (fvalela - #3): add functionality to start and end week at specific weekdays
    //    val startWeekDay = startDate.dayOfWeek.value
    //    val endWeekDay = endDate.dayOfWeek.value

    val nbDaysPriorToStartDateToShow =
        differenceBetweenTimeLibStartDayOfWeekAndPassedStartDayOfWeek(startDate)
    val nbDaysAfterEndDateToShow =
        differenceBetweenTimeLibEndDayOfWeekAndPassedEndDayOfWeek(endDate)

    var currentWorkingDate = startDate.minusDays(nbDaysPriorToStartDateToShow.toLong())

    while (currentWorkingDate.isBefore(endDate.plusDays(nbDaysAfterEndDateToShow.toLong()))) {
        when {
            // date is before set starting date
            currentWorkingDate.isBefore(startDate) ->
                WeekRow(
                    weekStartDate = currentWorkingDate,
                    absoluteStartDate = startDate,
                    absoluteEndDate = endDate,
                    onDayPressed = onDayPressed,
                    selectedDates = selectedDates,
                    dateCircleDiameter = dateCircleDiameter,
                )
            // date is after set end date
            currentWorkingDate.isAfter(endDate.minusDays(6)) ->
                WeekRow(
                    weekStartDate = currentWorkingDate,
                    absoluteEndDate = endDate,
                    onDayPressed = onDayPressed,
                    selectedDates = selectedDates,
                    dateCircleDiameter = dateCircleDiameter,
                )
            // date is inbetween set start date and set end date
            currentWorkingDate.isAfter(startDate.minusDays(1)) && currentWorkingDate.isBefore(
                endDate.plusDays(1)
            ) ->
                WeekRow(
                    weekStartDate = currentWorkingDate,
                    onDayPressed = onDayPressed,
                    selectedDates = selectedDates,
                    dateCircleDiameter = dateCircleDiameter,
                )
        }
        currentWorkingDate = currentWorkingDate.plusDays(lengthOfWeek.toLong())
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun WeekRow(
    weekStartDate: LocalDate,
    absoluteStartDate: LocalDate = LocalDate.MIN,
    absoluteEndDate: LocalDate = LocalDate.MAX,
    selectedDates: Collection<CalendarDate>,
    onDayPressed: ((Long) -> Unit)?,
    dateCircleDiameter: Dp,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp)
    ) {
        var runningDate = weekStartDate

        for (day in 0 until lengthOfWeek) {
            val runningDateEpochMilli =
                runningDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val isOutOfRange =
                runningDate.isBefore(absoluteStartDate) || runningDate.isAfter(absoluteEndDate)
            val dateBackgroundColourAndTextStyle = getSelectedDateColoursAndTextStyle(
                selectedDates,
                runningDateEpochMilli
            )
            SingleDate(
                modifier = Modifier.weight(1f, true),
                day = runningDate.dayOfMonth,
                dateBackgroundColour = dateBackgroundColourAndTextStyle.first,
                dateTextStyle = dateBackgroundColourAndTextStyle.second,
                dateBackgroundShape = dateBackgroundColourAndTextStyle.third,
                isOutOfRange = isOutOfRange,
                onDayPressed = onDayPressed,
                dayInMilli = runningDateEpochMilli,
                circleDiameter = dateCircleDiameter,
            )
            runningDate = runningDate.plusDays(1L)
        }
    }
}


private fun getSelectedDateColoursAndTextStyle(
    selectedDates: Collection<CalendarDate>,
    dateStartTimeInEpochMilli: Long
): Triple<Color, TextStyle, Shape> {
    val dateEndTimeInEpochMilli = dateStartTimeInEpochMilli + DAY_IN_MILLI
    for (date in selectedDates) {
        if (date.dateInMilli in dateStartTimeInEpochMilli until dateEndTimeInEpochMilli) {
            val resolvedTextStyle = resolveTextStyle(true)
            val resolvedBackgroundColour = date.backgroundColour
            val resolveBackgroundShape = resolveBackgroundShape(date.backgroundShape)
            return Triple(
                resolvedBackgroundColour,
                resolvedTextStyle,
                resolveBackgroundShape
            )
        }
    }
    return Triple(Color.Unspecified, resolveTextStyle(false), RoundedCornerShape(0.dp))
}

private fun resolveTextStyle(isSelected: Boolean): TextStyle {
    return if (isSelected) DateTextStyle.selected else DateTextStyle.unSelected
}

private fun resolveBackgroundShape(shape: Shape): Shape {
    return shape
}
