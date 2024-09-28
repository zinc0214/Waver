package com.zinc.waver.ui_detail.model

interface MyBucketMenuEvent {
    object GoToEdit : MyBucketMenuEvent
    object GoToGoalUpdate : MyBucketMenuEvent
    object GoToDelete : MyBucketMenuEvent
}


interface OtherBucketMenuEvent {
    object GoToHide : OtherBucketMenuEvent
    object GoToReport : OtherBucketMenuEvent
}