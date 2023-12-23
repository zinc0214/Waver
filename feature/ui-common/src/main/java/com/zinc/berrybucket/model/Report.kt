package com.zinc.berrybucket.model

import com.zinc.berrybucket.util.parseNavigationValue
import com.zinc.berrybucket.util.toNavigationValue
import com.zinc.common.models.ReportItem
import kotlinx.serialization.Serializable

sealed class ReportClickEvent {
    data class ReportItemSelected(val reportItem: ReportItem) : ReportClickEvent()
}

@Serializable
data class ReportInfo(
    val id: String,
    val writer: String,
    val contents: String
) : java.io.Serializable {
    companion object {
        fun toNavigationValue(value: ReportInfo): String =
            value.toNavigationValue()

        fun parseNavigationValue(value: String): ReportInfo =
            value.parseNavigationValue()
    }
}