package com.zinc.berrybucket.model

//data class DetailInfo(
//    val detailProfileInfo: ProfileInfo,
//    val detailDescInfo: DetailDescInfo,
//    val commentInfo: CommentInfo
//)

data class ImageInfo(
    val imageList: List<String>
) : DetailType()

data class CommonDetailDescInfo(
    val dDay: String,
    val tagList: List<String>,
    val title: String,
) : DetailType()

data class CloseDetailDescInfo(
    val commonDetailDescInfo: CommonDetailDescInfo,
    val goalCount: Int,
    val userCount: Int
) : DetailType()

data class CommentInfo(
    val commentCount: Int,
    val commenterList: List<Commenter>
) : DetailType()

data class MemoInfo(
    val memo: String
) : DetailType()

data class Commenter(
    val commentId: String = "",
    val profileImage: String,
    val nickName: String,
    val comment: String
)

sealed class DetailType {
    object Button : DetailType()
}

fun detailId(descType: DetailType): Int {
    return when (descType) {
        is ImageInfo -> 0
        is ProfileInfo -> 1
        is CommonDetailDescInfo, is CloseDetailDescInfo -> 2
        is MemoInfo -> 3
        is CommentInfo -> 4
        DetailType.Button -> 5
    }
}