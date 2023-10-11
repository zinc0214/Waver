package com.zinc.domain.models

data class FeedKeywordResponse(
    val data: List<FeedKeyWord>,
    val success: Boolean,
    val code: String,
    val message: String
)

data class FeedKeyWord(
    val id: String,
    val name: String
)

data class SavedKeywordItemsRequest(
    val keywordIds: List<String>
)