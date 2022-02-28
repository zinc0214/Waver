package com.zinc.berrybucket.model

//data class DetailInfo(
//    val detailProfileInfo: ProfileInfo,
//    val detailDescInfo: DetailDescInfo,
//    val commentInfo: CommentInfo
//)

data class DetailDescInfo(
    val detailProfileInfo: ProfileInfo,
    val dDay: String,
    val tagList: List<String>,
    val title: String,
    val memo: String = ""
) : DetailType()

data class CommentInfo(
    val commentCount: String,
    val commenterList: List<Commenter>
) : DetailType()

data class Commenter(
    val profileImage: String,
    val nickName: String,
    val comment: String
)

sealed class DetailType {
    object ButtonLayer : DetailType()
}

//    object DetailLayer : DetailType()
//    object ButtonLayer : DetailType()
//    object CommentLayer : DetailType()
//}