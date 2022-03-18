package com.zinc.berrybucket.model

data class ReportInfo(
    val writer: String,
    val contents: String
)

data class ReportItems(
    val items: List<ReportItem>
)

data class ReportItem(
    val text: String,
    val id: String
)

sealed class ReportClickEvent {
    object BackClicked : ReportClickEvent()
    data class ReportClicked(val reportItem: ReportItem) : ReportClickEvent()
    data class ReportItemSelected(val reportItem: ReportItem) : ReportClickEvent()
}

