package com.zinc.berrybucket.ui.presentation.detail.model

data class MentionSearchInfo(
    val searchText: String = "",
    val startIndex: Int = 0,
    val endIndex: Int = 0,
    val index: Int = 0
)

data class EditTextCommentInfo(
    val id: String,
    val text: String,
    val tagged: Boolean
)