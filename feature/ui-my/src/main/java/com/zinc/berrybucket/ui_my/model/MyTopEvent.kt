package com.zinc.berrybucket.ui_my.model

sealed class MyTopEvent {
    object Alarm : MyTopEvent()
    object Following : MyTopEvent()
    object Follower : MyTopEvent()
}