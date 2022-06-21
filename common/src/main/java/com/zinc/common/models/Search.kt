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
    val items: List<RecommendBucketItem>
)

data class RecommendBucketItem(
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