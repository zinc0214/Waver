package com.zinc.berrybucket.ui_feed.models

import com.zinc.domain.models.FeedKeyWord

data class UIFeedKeyword(
    val id: String,
    val keyword: String
)

fun List<FeedKeyWord>.parseUI() = map { UIFeedKeyword(id = it.id, keyword = it.name) }