package com.zinc.domain.models

data class BucketInfo(
    val id: String,
    val title: String,
    val memo: String? = null,
    val category: Category? = null,
    val currentCount: Int,
    val goalCount: Int? = null,
    val dDay: String? = null,
    val isOpen: Boolean,
    val imageList: List<String>? = null,
    val tag: List<String>? = null
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