package com.zinc.waver.ui.presentation.detail.model

sealed interface OpenDetailEditTextViewEvent {
    data class TextChanged(
        val updateText: String,
        val index: Int,
        val taggedList: List<TaggedTextInfo>
    ) : OpenDetailEditTextViewEvent

    data class SendComment(val sendText: String) : OpenDetailEditTextViewEvent
}