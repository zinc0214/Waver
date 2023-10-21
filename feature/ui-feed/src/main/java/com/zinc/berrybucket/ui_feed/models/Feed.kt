package com.zinc.berrybucket.ui_feed.models

import com.zinc.berrybucket.model.MyProfileInfoUi
import com.zinc.domain.models.FeedKeyWord
import com.zinc.domain.models.FeedListResponse
import kotlinx.serialization.Serializable

data class UIFeedKeyword(
    val id: String,
    val keyword: String
)

fun List<FeedKeyWord>.parseUI() = map { UIFeedKeyword(id = it.id, keyword = it.name) }

@Serializable
data class UIFeedInfo(
    val bucketId: String,
    val profileImage: String,
    val badgeImage: String,
    val titlePosition: String,
    val nickName: String,
    val imageList: List<String>? = null,
    val isProcessing: Boolean,
    val title: String,
    val liked: Boolean,
    val likeCount: Int,
    val commentCount: Int,
    val isScraped: Boolean,
) {
    fun hasImage(): Boolean {
        return !imageList.isNullOrEmpty()
    }
}

fun FeedListResponse.FeedData.toUIModel() = this.list.map { item ->
    UIFeedInfo(
        bucketId = item.id,
        profileImage = item.user.imgUrl,
        badgeImage = "",
        titlePosition = "",
        nickName = item.user.name,
        imageList = item.images,
        isProcessing = item.status == FeedListResponse.FeedItemResponse.FeedItemBucketStatus.PROGRESS,
        title = item.title,
        liked = false,
        likeCount = item.like,
        commentCount = item.commentCount,
        isScraped = item.isScraped
    )
}

fun UIFeedInfo.profileInfo(): MyProfileInfoUi {
    return MyProfileInfoUi(
        profileImage = this.profileImage,
        badgeImage = this.badgeImage,
        titlePosition = this.titlePosition,
        nickName = this.nickName
    )
}