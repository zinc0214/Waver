package com.zinc.berrybucket.ui_search.model

import com.zinc.domain.models.SearchRecommendResponse
import com.zinc.domain.models.SearchResultResponse

fun SearchResultResponse.SearchResult.parseUI(): SearchResultItems {
    val bucketItems = this.bucketlist.map {
        SearchBucketItem(
            bucketId = it.id.toString(),
            writerId = it.id.toString(), // TODO : writer id 붙이기
            thumbnail = null,
            title = it.title,
            isCopied = false
        )
    }

    val users = this.users.map {
        UserItem(
            userId = it.id.toString(),
            profileImageUrl = "null",
            nickName = it.name,
            isFollowed = it.followYn.isYes()
        )
    }

    return SearchResultItems(
        bucketItems = bucketItems, userItems = users
    )
}

fun SearchRecommendResponse.RecommendData.parseUI(): SearchRecommendItems {
    val recentWords = this.recentSearch
    val recommendWords = this.recommendKeywords.map {
        KeyWordItem(
            id = it.id.toString(),
            keyword = it.name,
            count = it.count.toString()
        )
    }

    return SearchRecommendItems(recentWords = recentWords, recommendWords = recommendWords)
}

data class RecommendList(
    val items: List<RecommendItem>
)

enum class RecommendType() {
    POPULAR, RECOMMEND
}

enum class SearchRecommendType {
    RECENT, RECOMMEND
}

data class RecommendItem(
    val type: RecommendType,
    val tagList: List<String>,
    val items: List<SearchBucketItem>
)

data class SearchBucketItem(
    val bucketId: String,
    val writerId: String,
    val thumbnail: String? = null,
    val title: String,
    val isCopied: Boolean,
)

data class SearchRecommendItems(
    val recentWords: List<String>,
    val recommendWords: List<KeyWordItem>
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
    val profileImageUrl: String? = "null",
    val nickName: String,
    val isFollowed: Boolean
)

