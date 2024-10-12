package com.zinc.waver.ui_detail.component

import androidx.compose.runtime.Composable
import com.zinc.waver.model.ReportInfo
import com.zinc.waver.ui_report.ReportScreen

@Composable
fun ShowReportScreen(
    reportInfo: ReportInfo,
    succeedReported: () -> Unit,
    closeEvent: () -> Unit
) {
    ReportScreen(
        reportInfo = reportInfo,
        backPress = {
            closeEvent()
        }, succeedReported = {
            succeedReported()
        }
    )
}