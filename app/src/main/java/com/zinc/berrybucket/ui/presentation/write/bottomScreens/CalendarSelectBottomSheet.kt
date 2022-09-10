package com.zinc.berrybucket.ui.presentation.write.bottomScreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zinc.berrybucket.common.R
import com.zinc.berrybucket.model.BottomButtonClickEvent
import com.zinc.berrybucket.ui.design.theme.Main4
import com.zinc.berrybucket.ui.presentation.common.BottomButtonView
import com.zinc.berrybucket.ui.presentation.common.calendar.CalendarView
import com.zinc.berrybucket.ui.presentation.common.calendar.model.CalendarDate
import com.zinc.berrybucket.ui.presentation.common.calendar.ui.DateTextStyle
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun CalendarSelectBottomSheet(
    selectDate: (LocalDate) -> Unit,
    canceled: () -> Unit
) {
    var currentYear by remember { mutableStateOf(0) }
    var currentMonth by remember { mutableStateOf(0) }
    var selectedDate: CalendarDate? by remember { mutableStateOf(null) }

    Column(modifier = Modifier.fillMaxWidth()) {
        CalendarView(
            year = currentYear,
            month = currentMonth,
            onDayPressed = { mill ->
                selectedDate = CalendarDate(
                    dateInMilli = mill,
                    backgroundColour = Main4,
                    backgroundShape = RoundedCornerShape(10.dp),
                    textStyle = DateTextStyle.selected
                )
            },
            onNavigateMonthPressed = { month: Int, year: Int ->
                currentYear = year
                currentMonth = month
            },
            selectedDates = if (selectedDate != null) listOf(selectedDate!!) else emptyList()
        )

        BottomButtonView(clickEvent = {
            when (it) {
                BottomButtonClickEvent.LeftButtonClicked -> canceled()
                BottomButtonClickEvent.RightButtonClicked -> {
                    selectedDate?.let { c ->
                        val date: LocalDate = Instant.ofEpochMilli(c.dateInMilli)
                            .atZone(ZoneId.systemDefault()).toLocalDate()
                        selectDate(date)
                    }
                }
            }
        }, rightText = R.string.confirm)
    }
}

@Preview
@Composable
private fun CalendarSelectBottomSheetPreview() {
    CalendarSelectBottomSheet(selectDate = {}, canceled = {})
}