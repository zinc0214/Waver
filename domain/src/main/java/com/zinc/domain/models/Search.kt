package com.zinc.domain.models

import com.zinc.common.models.YesOrNo

data class SearchResultResponse(
    val data: SearchResult,
    val success: Boolean,
    val code: String,
    val message: String
) {
    data class SearchResult(
        val bucketlist: List<SearchResultBucketList>,
        val users: List<SearchResultUser>
    )

    data class SearchResultBucketList(
        val id: Int,
        val title: String
    )

    data class SearchResultUser(
        val id: Int,
        val name: String,
        val followYn: YesOrNo
    )
}

