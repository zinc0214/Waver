package com.zinc.berrybucket.model

import com.zinc.common.models.SearchBucketItem
import com.zinc.common.models.SearchResultItems
import com.zinc.common.models.UserItem
import com.zinc.domain.models.SearchResultResponse

fun SearchResultResponse.SearchResult.parseUI(): SearchResultItems {
    val bucketItems = this.bucketlist.map {
        SearchBucketItem(
            id = it.id.toString(),
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