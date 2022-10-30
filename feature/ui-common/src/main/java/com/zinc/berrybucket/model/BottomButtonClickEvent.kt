package com.zinc.berrybucket.model

sealed class BottomButtonClickEvent {
    object LeftButtonClicked : BottomButtonClickEvent()
    object RightButtonClicked : BottomButtonClickEvent()
}