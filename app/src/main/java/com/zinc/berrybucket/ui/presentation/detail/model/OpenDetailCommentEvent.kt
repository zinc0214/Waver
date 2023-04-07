package com.zinc.berrybucket.ui.presentation.detail.model

sealed interface OpenDetailCommentEvent {
    object MentaionButtonClicked : OpenDetailCommentEvent
    data class TextChenaged(val updateText: String) : OpenDetailCommentEvent
    data class SendComment(val sendText: String) : OpenDetailCommentEvent
}