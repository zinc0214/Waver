package com.zinc.domain.models

data class BucketInfo(
    val id: String,
    val title: String,
    val memo: String,
    val category: Category,
    val currentCount: Int,
    val goalCount: Int,
    val dDay: String,
    val isOpen: Boolean,
    val imageList: List<String>,
    val tag: List<String>
)

data class Category(
    val id: Int,
    val name: String
)

data class CommentList(
    val commentList: List<CommentInfo>
)

data class CommentInfo(
    val profileImageView: String,
    val nickName: String,
    val comment: String,
    val time: String
)