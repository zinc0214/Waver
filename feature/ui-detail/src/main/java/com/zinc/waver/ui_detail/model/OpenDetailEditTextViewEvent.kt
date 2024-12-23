package com.zinc.waver.ui_detail.model

sealed interface OpenDetailEditTextViewEvent {
    data class TextChanged(
        val updateText: String,
        val index: Int,
        val taggedList: List<TaggedTextInfo>
    ) : OpenDetailEditTextViewEvent

    data class SendComment(val sendText: String) : OpenDetailEditTextViewEvent

    data object BucketLike : OpenDetailEditTextViewEvent
}