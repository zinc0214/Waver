package com.zinc.common.models

data class SearchRecommendCategory(
    val imageUrl: String,
    val category: String
)

data class RecommendList(
    val items: List<RecommendItem>
)

data class RecommendItem(
    val type: String,
    val tagList: List<String>,
    val items: List<SearchBucketItem>
)

data class SearchBucketItem(
    val id: String,
    val thumbnail: String? = null,
    val title: String,
    val isCopied: Boolean,
)

data class SearchRecommendItems(
    val recentWords: List<RecentItem>,
    val recommendWords: List<KeyWordItem>
)

data class RecentItem(
    val id: String,
    val word: String
)

data class KeyWordItem(
    val id: String,
    val keyword: String,
    val count: String
)

data class SearchResultItems(
    val bucketItems: List<SearchBucketItem>,
    val userItems: List<UserItem>
)

data class UserItem(
    val userId: String,
    val profileImageUrl: String? = null,
    val nickName: String,
    val isFollowed: Boolean
)

