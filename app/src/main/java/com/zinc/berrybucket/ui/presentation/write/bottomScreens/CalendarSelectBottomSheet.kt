package com.zinc.berrybucket.ui.presentation.write.bottomScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.zinc.berrybucket.model.BottomButtonClickEvent
import com.zinc.berrybucket.ui.design.theme.Gray1
import com.zinc.berrybucket.ui.design.theme.Gray10
import com.zinc.berrybucket.ui.design.theme.Main2
import com.zinc.berrybucket.ui.presentation.component.BottomButtonView
import com.zinc.berrybucket.ui.presentation.component.MyText
import com.zinc.berrybucket.ui.presentation.component.NumberPicker
import com.zinc.berrybucket.ui.presentation.component.calendar.CalendarView
import com.zinc.berrybucket.ui.presentation.write.DateViewModel
import com.zinc.berrybucket.ui.presentation.write.bottomScreens.CalendarViewType.CALENDAR
import com.zinc.berrybucket.ui.presentation.write.bottomScreens.CalendarViewType.PICKER
import com.zinc.berrybucket.ui.util.dpToSp
import java.time.LocalDate
import java.util.Calendar


@Composable
fun CalendarSelectBottomSheet(
    selectedDate: LocalDate = LocalDate.now(), confirmed: (LocalDate) -> Unit, canceled: () -> Unit
) {

    val viewModel: DateViewModel = hiltViewModel()

    // 선택된 연, 월, 일 정보
    var currentLocalDate by remember { mutableStateOf(selectedDate) }

    var viewType by remember { mutableStateOf(CALENDAR) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 420.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        when (viewType) {
            CALENDAR -> {
                CalendarTypeView(
                    currentYear = currentLocalDate.year,
                    currentMonth = currentLocalDate.monthValue,
                    navigateMonth = { year: Int, month: Int ->
                        currentLocalDate = LocalDate.of(year, month, currentLocalDate.dayOfMonth)
                    },
                    selectedDate = currentLocalDate,
                    updateSelectedDate = {
                        currentLocalDate = it
                    },
                    changeViewType = {
                        viewType = it
                    })
            }
            PICKER -> {
                DatePickerView(
                    currentDate = currentLocalDate,
                    changeViewType = {
                        viewType = it
                    },
                    updateDated = { year: Int, month: Int, date: Int ->
                        val checkValidValue = date <= getValidPickerDate(year, month)
                        if (checkValidValue) {
                            currentLocalDate = LocalDate.of(year, month, date)
                        }
                    },
                )
            }
        }

        BottomButtonView(
            clickEvent = {
                when (it) {
                    BottomButtonClickEvent.LeftButtonClicked -> canceled()
                    BottomButtonClickEvent.RightButtonClicked -> {
                        confirmed(currentLocalDate)
                    }
                }
            }, rightText = com.zinc.berrybucket.ui_common.R.string.confirm
        )
    }
}

@Composable
private fun CalendarTypeView(
    currentYear: Int,
    currentMonth: Int,
    selectedDate: LocalDate,
    navigateMonth: (Int, Int) -> Unit,
    updateSelectedDate: (LocalDate) -> Unit,
    changeViewType: (CalendarViewType) -> Unit
) {
    CalendarView(year = currentYear, month = currentMonth,
        onDayPressed = { localDate ->
            updateSelectedDate(localDate)
        }, onNavigateMonthPressed = { month: Int, year: Int ->
            navigateMonth(year, month)
        }, selectedDates = listOf(selectedDate),
        changeViewType = {
            changeViewType(PICKER)
        })
}

@Composable
private fun DatePickerView(
    currentDate: LocalDate,
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

        var maxValidDate by remember {
            mutableStateOf(
                getValidPickerDate(
                    currentDate.year,
                    currentDate.monthValue
                )
            )
        }

        MyText(
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
        Row(
            modifier = Modifier
                .constrainAs(picker) {
                    top.linkTo(divider.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                space = 24.dp,
                alignment = Alignment.CenterHorizontally
            )
        ) {

            NumberPicker(
                label = { it },
                value = year,
                onValueChange = { value ->
                    year = value
                    maxValidDate = getValidPickerDate(year, month)
                    updateDated(year, month, date)
                },
                rangeList = (today.year - 100..today.year + 100).toList()
            )

            NumberPicker(
                label = { it },
                value = month,
                onValueChange = { value ->
                    month = value
                    maxValidDate = getValidPickerDate(year, month)
                    updateDated(year, month, date)
                },
                rangeList = (1..12).toList()
            )

            NumberPicker(
                label = { it },
                value = date,
                onValueChange = { value ->
                    date = value
                    updateDated(year, month, date)
                },
                rangeList = (1..maxValidDate).toList()
            )
        }
    }
}

private enum class CalendarViewType {
    CALENDAR, PICKER
}

private fun getValidPickerDate(year: Int, month: Int): Int {
    val calendar = Calendar.getInstance()
    calendar.set(year, month - 1, 1)
    return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
}

@Preview
@Composable
private fun CalendarSelectBottomSheetPreview() {
    CalendarSelectBottomSheet(confirmed = {}, canceled = {})
}