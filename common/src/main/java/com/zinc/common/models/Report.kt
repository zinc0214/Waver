package com.zinc.common.models

data class ReportItems(
    val items: List<ReportItem>
)

data class ReportItem(
    val text: String,
    val id: String
)
