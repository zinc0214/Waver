package com.zinc.data.repository

import com.zinc.common.models.CommonResponse
import com.zinc.common.models.ReportItem
import com.zinc.common.models.ReportItems
import com.zinc.data.api.WaverApi
import com.zinc.domain.models.ReportReasonRequest
import com.zinc.domain.repository.ReportRepository
import javax.inject.Inject

internal class ReportRepositoryImpl @Inject constructor(
    private val waverApi: WaverApi
) : ReportRepository {

    override suspend fun loadReportItems(): ReportItems {

        return ReportItems(
            items = listOf(
                ReportItem(
                    text = "광고/홍보성",
                    id = "1"
                ),
                ReportItem(
                    text = "욕설/인신 공격",
                    id = "2"
                ),
                ReportItem(
                    text = "불법정보",
                    id = "3"
                ),
                ReportItem(
                    text = "불법정보",
                    id = "4"
                ),
                ReportItem(
                    text = "음란성/선정성",
                    id = "5"
                ),
                ReportItem(
                    text = "개인정보 노출",
                    id = "6"
                ),
                ReportItem(
                    text = "같은 내용 도배",
                    id = "7"
                ),
                ReportItem(
                    text = "정치적/사회적 의견",
                    id = "8"
                ),
                ReportItem(
                    text = "기타",
                    id = "9"
                )
            )
        )
    }

    override suspend fun reportComment(id: String, reason: String): CommonResponse {
        return waverApi.requestBucketCommentReport(id, ReportReasonRequest(reason))
    }

    override suspend fun reportBucket(id: String, reason: String): CommonResponse {
        return waverApi.requestBucketReport(id, ReportReasonRequest(reason))
    }
}