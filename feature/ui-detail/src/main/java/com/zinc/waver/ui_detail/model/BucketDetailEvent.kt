package com.zinc.waver.ui_detail.model

import com.zinc.common.models.AddBucketCommentRequest
import com.zinc.waver.model.DetailLoadFailStatus
import com.zinc.waver.model.ReportInfo
import com.zinc.waver.model.WriteTotalInfo

sealed interface OpenBucketDetailEvent {
    data class BucketReport(val reportInfo: ReportInfo) : OpenBucketDetailEvent
    data class Update(val info: WriteTotalInfo) : OpenBucketDetailEvent
    data class GoToOtherProfile(val id: String) : OpenBucketDetailEvent
}

sealed interface OpenBucketDetailEvent2 {
    data class Update(val info: WriteTotalInfo) : OpenBucketDetailEvent2
    data class GoToOtherProfile(val id: String) : OpenBucketDetailEvent2
}

// 공개버킷리스트 상세 > 내부 이벤트 처리
sealed interface OpenBucketDetailInternalEvent {
    data object None : OpenBucketDetailInternalEvent

    // 우상단 더보기 버튼
    data class MoreDialog(val isMine: Boolean) : OpenBucketDetailInternalEvent

    // 멘션 롱클릭 시
    data class CommentOption(val index: Int) : OpenBucketDetailInternalEvent

    // 멘션 @버튼 활성화
    data object CommentTagListDialog : OpenBucketDetailInternalEvent

    // 버킷 신고
    data class ReportBucket(val info: ReportInfo) : OpenBucketDetailInternalEvent

    // 댓글 신고
    data class ReportComment(val info: ReportInfo) : OpenBucketDetailInternalEvent

    // 버킷 달성 횟수 수정
    data class ShowUpdateGoalCountDialog(val count: Int) : OpenBucketDetailInternalEvent

    // 버킷 우측상단 옵션선택
    sealed interface BucketMore : OpenBucketDetailInternalEvent {
        data class My(val event: MyBucketMoreMenuEvent) : BucketMore
        data class Other(val event: OtherBucketMenuEvent) : BucketMore
    }

    // 버킷 로드 실패
    data class Error(val status: DetailLoadFailStatus) : OpenBucketDetailInternalEvent

    sealed interface ViewModelEvent : OpenBucketDetailInternalEvent {

        // 버킷 달성 횟수 수정
        data class UpdateGoalCount(val count: Int) : ViewModelEvent

        // 버킷달성버튼 클릭
        data object Achieve : ViewModelEvent

        data object UpdateBucket : ViewModelEvent

        data class DeleteMyComment(val commentId: String) : ViewModelEvent

        // 댓글 추가
        data class AddComment(val comment: AddBucketCommentRequest) : ViewModelEvent

        data object BucketLike : ViewModelEvent

        // 댓글 숨기기
        data class HideComment(val commentId: String) : ViewModelEvent
    }

}

sealed interface CloseBucketDetailEvent {
    data class Update(val info: WriteTotalInfo) : CloseBucketDetailEvent
}
