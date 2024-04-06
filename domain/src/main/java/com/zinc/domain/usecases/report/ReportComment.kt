package com.zinc.domain.usecases.report

import com.zinc.domain.repository.ReportRepository
import javax.inject.Inject

// 신고 항목 로드
class ReportComment @Inject constructor(
    private val reportRepository: ReportRepository
) {
    suspend operator fun invoke(token: String, id: String, reason: String) =
        reportRepository.reportComment(token, id, reason)
}