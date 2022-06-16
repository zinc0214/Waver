package com.zinc.common.models

import kotlinx.serialization.Serializable

@Serializable
data class ReportInfo(
    val id: String,
    val writer: String,
    val contents: String
) : java.io.Serializable

data class ReportItems(
    val items: List<ReportItem>
)

data class ReportItem(
    val text: String,
    val id: String
)
