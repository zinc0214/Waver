package com.zinc.waver.ui_feed.models

import com.zinc.common.models.KeywordInfo
import com.zinc.domain.models.FeedListResponse
import com.zinc.waver.model.MyProfileInfoUi
import kotlinx.serialization.Serializable

data class UIFeedKeyword(
    val id: Int,
    val keyword: String
)

fun List<KeywordInfo>.parseUI() = map { UIFeedKeyword(id = it.id, keyword = it.name) }

@Serializable
data class UIFeedInfo(
    val bucketId: String,
    val writerId: String,
    val profileImage: String?,
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
        writerId = item.user.id,
        profileImage = item.user.imgUrl,
        badgeImage = item.user.badgeImgUrl.orEmpty(),
        titlePosition = item.user.badgeTitle.orEmpty(),
        nickName = item.user.name,
        imageList = item.images,
        isProcessing = item.status == FeedListResponse.FeedItemResponse.FeedItemBucketStatus.PROGRESS,
        title = item.title,
        liked = item.likeYn?.isYes() ?: false,
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

sealed interface FeedClickEvent {
    data class Like(val isLike: Boolean, val id: String) : FeedClickEvent
    data class Scrap(val id: String) : FeedClickEvent
    data class GoToBucket(val bucketId: String, val userId: String) : FeedClickEvent
}

sealed class FeedLoadStatus {
    data object RefreshLoading : FeedLoadStatus()
    data object PagingLoading : FeedLoadStatus()
    data object KeywordLoading : FeedLoadStatus()
    data object Success : FeedLoadStatus()
    data object CopySuccess : FeedLoadStatus()
    data object ToastFail : FeedLoadStatus()
    data class LoadFail(val hasData: Boolean) : FeedLoadStatus()
    data object None : FeedLoadStatus()
}