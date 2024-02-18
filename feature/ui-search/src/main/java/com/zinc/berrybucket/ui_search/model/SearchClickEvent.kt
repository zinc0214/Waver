package com.zinc.berrybucket.ui_search.model

sealed class SearchClickEvent {
    data object GoToSearch : SearchClickEvent()
    data class GoToOpenBucket(val id: String) : SearchClickEvent()
    data class GoToOtherUser(val id: String) : SearchClickEvent()
}