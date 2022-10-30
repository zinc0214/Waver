package com.zinc.berrybucket.model

import com.zinc.common.models.ReportItem

sealed class ReportClickEvent {
    object BackClicked : ReportClickEvent()
    data class ReportClicked(val reportItem: ReportItem) : ReportClickEvent()
    data class ReportItemSelected(val reportItem: ReportItem) : ReportClickEvent()
}

