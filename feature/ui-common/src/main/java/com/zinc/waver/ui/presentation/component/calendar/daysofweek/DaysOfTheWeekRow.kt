package com.zinc.waver.ui.presentation.component.calendar.daysofweek

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zinc.waver.ui.design.theme.Gray6
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.presentation.component.calendar.differenceBetweenTimeLibStartDayOfWeekAndPassedStartDayOfWeek
import com.zinc.waver.ui.presentation.component.calendar.lengthOfWeek
import com.zinc.waver.ui.util.dpToSp
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
internal fun DaysOfTheWeekRow(startDate: LocalDate, endDate: LocalDate) {

    var runningDate = startDate.minusDays(
        differenceBetweenTimeLibStartDayOfWeekAndPassedStartDayOfWeek(startDate).toLong()
    )
    var runningCount = 0

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 28.dp)) {
        while (runningDate.isBefore(endDate) && runningCount++ < lengthOfWeek) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, true),
                contentAlignment = Alignment.Center,
            ) {
                MyText(
                    text = runningDate.dayOfWeek.getDisplayName(
                        TextStyle.NARROW,
                        Locale.getDefault(),
                    ),
                    color = Gray6,
                    fontSize = dpToSp(dp = 15.dp),
                )
            }
            runningDate = runningDate.plusDays(1L)
        }
    }
}