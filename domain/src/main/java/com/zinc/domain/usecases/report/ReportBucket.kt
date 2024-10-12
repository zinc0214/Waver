package com.zinc.domain.usecases.report

import com.zinc.domain.repository.ReportRepository
import javax.inject.Inject

class ReportBucket @Inject constructor(
    private val reportRepository: ReportRepository
) {
    suspend operator fun invoke(id: String, reason: String) =
        reportRepository.reportBucket(id, reason)
}