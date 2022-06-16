package com.zinc.domain.repository

import com.zinc.common.models.ReportItems

interface ReportRepository {
    suspend fun loadReportItems(): ReportItems
}