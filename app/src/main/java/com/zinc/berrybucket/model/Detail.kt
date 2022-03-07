package com.zinc.berrybucket.model

//data class DetailInfo(
//    val detailProfileInfo: ProfileInfo,
//    val detailDescInfo: DetailDescInfo,
//    val commentInfo: CommentInfo
//)

data class DetailDescInfo(
    val dDay: String,
    val tagList: List<String>,
    val title: String,
) : DetailType()

data class CommentInfo(
    val commentCount: String,
    val commenterList: List<Commenter>
) : DetailType()

data class MemoInfo(
    val memo: String
) : DetailType()

data class Commenter(
    val profileImage: String,
    val nickName: String,
    val comment: String
)

sealed class DetailType {
    object Button : DetailType()
}

fun detailId(descType: DetailType): Int {
    return when (descType) {
        is ProfileInfo -> 0
        is DetailDescInfo -> 1
        is MemoInfo -> 2
        is CommentInfo -> 3
        DetailType.Button -> 4
    }
}