package com.zinc.waver.ui.presentation.component.calendar

import java.time.DayOfWeek
import java.time.LocalDate

internal const val DAY_IN_MILLI = 23 * 60 * 60 * 1000

//internal fun resolveLocalDate(year: Int, month: Int, day: Int = 1): LocalDate {
//    return LocalDate.of(year, month, day)
//}


fun resolveLocalDate(year: Int, month: Int, day: Int): LocalDate {
    // 해당 월의 마지막 날짜를 구합니다
    val lastDayOfMonth = LocalDate.of(year, month, 1).lengthOfMonth()

    // 날짜가 해당 월의 마지막 날짜를 초과하는 경우 마지막 날짜로 조정
    val validDay = if (day > lastDayOfMonth) lastDayOfMonth else day

    return LocalDate.of(year, month, validDay)
}


internal fun calculateLengthOfWeek(
    resolvedStartDate: LocalDate,
    resolvedEndDate: LocalDate,
): Int {
    return 7
    // todo (fvalela - #5): allow library caller to configure how many days in a week
    //    val daysBetweenStartAndEndDate = resolvedStartDate.until(resolvedEndDate, ChronoUnit.DAYS)
    //    return if (daysBetweenStartAndEndDate < 7)
    //        daysBetweenStartAndEndDate.toInt()
    //    else
    //        7
}

// todo (fvalela - #3): allow library caller to configure what
//  day of the week is the start (i.e. Sunday, Monday, Saturday, etc...)
internal fun differenceBetweenTimeLibEndDayOfWeekAndPassedEndDayOfWeek(endDate: LocalDate): Int {
    val endDateDayOfWeek = endDate.dayOfWeek.value
    return DayOfWeek.SATURDAY.value -
            if (endDateDayOfWeek == DayOfWeek.SUNDAY.value)
                0
            else
                endDateDayOfWeek
}

// todo (fvalela - #3): allow library caller to configure what
//  day of the week is the start (i.e. Sunday, Monday, Saturday, etc...)
internal fun differenceBetweenTimeLibStartDayOfWeekAndPassedStartDayOfWeek(startDate: LocalDate): Int {
    val startDateDayOfWeek = startDate.dayOfWeek.value
    return if (startDateDayOfWeek == DayOfWeek.SUNDAY.value)
        0
    else
        startDateDayOfWeek
}