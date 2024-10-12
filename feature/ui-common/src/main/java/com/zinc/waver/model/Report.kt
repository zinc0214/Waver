package com.zinc.waver.model

import com.zinc.common.models.ReportItem
import kotlinx.serialization.Serializable

sealed class ReportClickEvent {
    data class ReportItemSelected(val reportItem: ReportItem) : ReportClickEvent()
}

@Serializable
data class ReportInfo(
    val id: String,
    val writer: String,
    val contents: String,
    val reportType: ReportType
)

enum class ReportType {
    BUCKET, COMMENT
}