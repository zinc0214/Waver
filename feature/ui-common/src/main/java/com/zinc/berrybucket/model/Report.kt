package com.zinc.berrybucket.model

import com.zinc.common.models.ReportItem
import kotlinx.serialization.Serializable

sealed class ReportClickEvent {
    data class ReportItemSelected(val reportItem: ReportItem) : ReportClickEvent()
}

@Serializable
data class ReportInfo(
    val id: Int,
    val writer: String,
    val contents: String
)