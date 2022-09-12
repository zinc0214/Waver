package com.zinc.berrybucket.ui.presentation.write.bottomScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.zinc.berrybucket.common.R
import com.zinc.berrybucket.model.BottomButtonClickEvent
import com.zinc.berrybucket.ui.design.theme.Gray1
import com.zinc.berrybucket.ui.design.theme.Gray10
import com.zinc.berrybucket.ui.design.theme.Main2
import com.zinc.berrybucket.ui.design.theme.Main4
import com.zinc.berrybucket.ui.presentation.common.BottomButtonView
import com.zinc.berrybucket.ui.presentation.common.NumberPicker
import com.zinc.berrybucket.ui.presentation.common.calendar.CalendarView
import com.zinc.berrybucket.ui.presentation.common.calendar.model.CalendarDate
import com.zinc.berrybucket.ui.presentation.common.calendar.ui.DateTextStyle
import com.zinc.berrybucket.ui.presentation.write.DateViewModel
import com.zinc.berrybucket.ui.presentation.write.bottomScreens.CalendarViewType.CALENDAR
import com.zinc.berrybucket.ui.presentation.write.bottomScreens.CalendarViewType.PICKER
import com.zinc.berrybucket.ui.util.dpToSp
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun CalendarSelectBottomSheet(
    selectedDate: LocalDate = LocalDate.now(), confirmed: (LocalDate) -> Unit, canceled: () -> Unit
) {

    val viewModel: DateViewModel = hiltViewModel()

    // 선택된 연, 월, 일 정보
    var currentLocalDate by remember { mutableStateOf(selectedDate) }

    var selectedCalendarDate: CalendarDate by remember {
        mutableStateOf(
            CalendarDate(
                dateInMilli = currentLocalDate.toEpochDay(),
                backgroundColour = Main4,
                backgroundShape = RoundedCornerShape(10.dp),
                textStyle = DateTextStyle.selected
            )
        )
    }

    var viewType by remember { mutableStateOf(CALENDAR) }

    Column(modifier = Modifier.fillMaxWidth()) {
        when (viewType) {
            CALENDAR -> {
                CalendarTypeView(currentYear = currentLocalDate.year,
                    currentMonth = currentLocalDate.monthValue,
                    navigateMonth = { year: Int, month: Int ->
                        currentLocalDate = LocalDate.of(year, month, currentLocalDate.dayOfMonth)
                    },
                    selectedDate = selectedCalendarDate,
                    updateSelectedDate = {
                        selectedCalendarDate = it
                        currentLocalDate =
                            Instant.ofEpochMilli(it.dateInMilli).atZone(ZoneId.systemDefault())
                                .toLocalDate()
                    },
                    changeViewType = {
                        viewType = it
                    })
            }
            PICKER -> {
                DatePickerView(
                    currentDate = currentLocalDate,
                    viewModel = viewModel,
                    changeViewType = {
                        viewType = it
                    },
                    updateDated = { year: Int, month: Int, date: Int ->
                        currentLocalDate = LocalDate.of(year, month, date)
                    },
                )
            }
        }

        BottomButtonView(clickEvent = {
            when (it) {
                BottomButtonClickEvent.LeftButtonClicked -> canceled()
                BottomButtonClickEvent.RightButtonClicked -> {
                    confirmed(currentLocalDate)
                }
            }
        }, rightText = R.string.confirm)
    }
}

@Composable
private fun CalendarTypeView(
    currentYear: Int,
    currentMonth: Int,
    selectedDate: CalendarDate,
    navigateMonth: (Int, Int) -> Unit,
    updateSelectedDate: (CalendarDate) -> Unit,
    changeViewType: (CalendarViewType) -> Unit
) {
    CalendarView(year = currentYear, month = currentMonth, onDayPressed = { mill ->
        updateSelectedDate(selectedDate.copy(dateInMilli = mill))
    }, onNavigateMonthPressed = { month: Int, year: Int ->
        navigateMonth(year, month)
    }, selectedDates = listOf(selectedDate), changeViewType = {
        changeViewType(PICKER)
    })
}

@Composable
private fun DatePickerView(
    currentDate: LocalDate,
    viewModel: DateViewModel,
    updateDated: (Int, Int, Int) -> Unit,
    changeViewType: (CalendarViewType) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 28.dp)
            .background(Gray1)
            .padding(top = 36.dp)
    ) {

        val today = LocalDate.now()
        val (textView, divider, picker) = createRefs()

        var year by remember { mutableStateOf(currentDate.year) }
        var month by remember { mutableStateOf(currentDate.monthValue) }
        var date by remember { mutableStateOf(currentDate.dayOfMonth) }

        val maxValidDate by viewModel.validDateRange.observeAsState()

        Text(
            text = stringResource(id = com.zinc.berrybucket.R.string.optionCalendarSelect),
            fontSize = dpToSp(dp = 18.dp),
            color = Gray10,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .constrainAs(textView) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .clickable {
                    changeViewType(CALENDAR)
                })

        Divider(color = Main2,
            thickness = 1.dp,
            modifier = Modifier
                .padding(top = 2.dp)
                .constrainAs(divider) {
                    top.linkTo(textView.bottom)
                    start.linkTo(textView.start)
                    end.linkTo(textView.end)
                    width = Dimension.fillToConstraints
                })

        // 연월일 picker
        Row(modifier = Modifier
            .constrainAs(picker) {
                top.linkTo(divider.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
                width = Dimension.fillToConstraints
            }
            .padding(top = 60.dp, bottom = 70.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(24.dp)) {

            NumberPicker(
                label = { it },
                value = year,
                onValueChange = { value ->
                    year = value
                    updateDated(year, month, date)
                    viewModel.updateYearAndMonth(year, month)
                },
                rangeList = (today.year - 100..today.year + 100).toList()
            )

            NumberPicker(
                label = { it },
                value = month,
                onValueChange = { value ->
                    month = value
                    updateDated(year, month, date)
                    viewModel.updateYearAndMonth(year, month)
                },
                rangeList = (1..12).toList()
            )

            NumberPicker(
                label = { it },
                value = date,
                onValueChange = { value ->
                    date = value
                    updateDated(year, month, date)
                    viewModel.updateYearAndMonth(year, month)
                },
                rangeList = (1..(if (maxValidDate != null) maxValidDate else 30)!!).toList()
            )
        }
    }
}

private enum class CalendarViewType {
    CALENDAR, PICKER
}

@Preview
@Composable
private fun CalendarSelectBottomSheetPreview() {
    CalendarSelectBottomSheet(confirmed = {}, canceled = {})
}