package com.zinc.waver.ui_search.model

sealed class SearchGoToEvent {
    data object GoToSearch : SearchGoToEvent()
    data class GoToOpenBucket(val bucketId: String, val userId: String) : SearchGoToEvent()
    data class GoToOtherUser(val id: String) : SearchGoToEvent()
}

sealed interface SearchActionEvent {
    data class RequestFollow(val userId: String, val alreadyFollowed: Boolean) : SearchActionEvent
}