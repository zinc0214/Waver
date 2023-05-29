package com.zinc.berrybucket.ui.presentation.component.calendar.week

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
import com.zinc.berrybucket.ui.design.theme.Main4
import com.zinc.berrybucket.ui.presentation.component.calendar.DAY_IN_MILLI
import com.zinc.berrybucket.ui.presentation.component.calendar.date.SingleDate
import com.zinc.berrybucket.ui.presentation.component.calendar.differenceBetweenTimeLibEndDayOfWeekAndPassedEndDayOfWeek
import com.zinc.berrybucket.ui.presentation.component.calendar.differenceBetweenTimeLibStartDayOfWeekAndPassedStartDayOfWeek
import com.zinc.berrybucket.ui.presentation.component.calendar.lengthOfWeek
import com.zinc.berrybucket.ui.presentation.component.calendar.ui.DateTextStyle
import com.zinc.berrybucket.ui.util.toEpochMilli
import java.time.LocalDate
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
@Composable
internal fun WeekRows(
    startDate: LocalDate,
    endDate: LocalDate,
    onDayPressed: ((LocalDate) -> Unit)?,
    selectedDates: Collection<LocalDate>,
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
    selectedDates: Collection<LocalDate>,
    onDayPressed: ((LocalDate) -> Unit)?,
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
                dateBackgroundShape = dateBackgroundColourAndTextStyle.second,
                dateTextStyle = dateBackgroundColourAndTextStyle.third,
                isOutOfRange = isOutOfRange,
                onDayPressed = onDayPressed,
                localDate = runningDate,
                circleDiameter = dateCircleDiameter,
            )
            runningDate = runningDate.plusDays(1L)
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
private fun getSelectedDateColoursAndTextStyle(
    selectedDates: Collection<LocalDate>,
    dateStartTimeInEpochMilli: Long
): Triple<Color, Shape, TextStyle> {
    val dateEndTimeInEpochMilli = dateStartTimeInEpochMilli + DAY_IN_MILLI
    for (date in selectedDates) {
        if (date.toEpochMilli() in dateStartTimeInEpochMilli until dateEndTimeInEpochMilli) {
            return resolveStyle(true)
        }
    }
    return resolveStyle(false)
}

private fun resolveStyle(isSelected: Boolean): Triple<Color, Shape, TextStyle> {
    return if (isSelected) Triple(Main4, RoundedCornerShape(10.dp), DateTextStyle.selected)
    else Triple(Color.Transparent, RoundedCornerShape(0.dp), DateTextStyle.unSelected)
}
