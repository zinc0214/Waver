package com.zinc.waver.ui_detail.model

interface MyBucketMoreMenuEvent {
    object GoToEdit : MyBucketMoreMenuEvent
    object GoToGoalUpdate : MyBucketMoreMenuEvent
    object GoToDelete : MyBucketMoreMenuEvent
}


interface OtherBucketMenuEvent {
    object GoToBlock : OtherBucketMenuEvent
    object GoToReport : OtherBucketMenuEvent
}