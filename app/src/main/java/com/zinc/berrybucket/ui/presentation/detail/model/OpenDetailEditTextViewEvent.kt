package com.zinc.berrybucket.ui.presentation.detail.model

sealed interface OpenDetailEditTextViewEvent {
    object MentionButtonClicked : OpenDetailEditTextViewEvent
    data class TextChanged(val updateText: String, val index: Int) : OpenDetailEditTextViewEvent
    data class SendComment(val sendText: String) : OpenDetailEditTextViewEvent
}