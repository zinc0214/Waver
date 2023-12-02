package com.zinc.berrybucket.ui.presentation.detail.screen

interface MyBucketMenuEvent {
    object GoToEdit : MyBucketMenuEvent
    object GoToGoalUpdate : MyBucketMenuEvent
    object GoToDelete : MyBucketMenuEvent
}


interface OtherBucketMenuEvent {
    object GoToHide : OtherBucketMenuEvent
    object GoToReport : OtherBucketMenuEvent
}