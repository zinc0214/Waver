package com.zinc.berrybucket.ui_search.model

sealed class SearchGoToEvent {
    data object GoToSearch : SearchGoToEvent()
    data class GoToOpenBucket(val id: String) : SearchGoToEvent()
    data class GoToOtherUser(val id: String) : SearchGoToEvent()
}

sealed interface SearchActionEvent {
    data class RequestFollow(val userId: String, val alreadyFollowed: Boolean) : SearchActionEvent
}