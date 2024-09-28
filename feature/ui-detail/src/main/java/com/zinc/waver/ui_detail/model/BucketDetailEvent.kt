package com.zinc.waver.ui_detail.model

import com.zinc.waver.model.ReportInfo
import com.zinc.waver.model.WriteTotalInfo

sealed interface OpenBucketDetailEvent {
    data class BucketReport(val reportInfo: ReportInfo) : OpenBucketDetailEvent
    data class Update(val info: WriteTotalInfo) : OpenBucketDetailEvent
    data class GoToOtherProfile(val id: String) : OpenBucketDetailEvent
}

sealed interface CloseBucketDetailEvent {
    data class Update(val info: WriteTotalInfo) : CloseBucketDetailEvent
}
