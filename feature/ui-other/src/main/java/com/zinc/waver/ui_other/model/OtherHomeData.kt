package com.zinc.waver.ui_other.model

import com.zinc.common.models.BucketStatus
import com.zinc.common.models.OtherProfileHomeData
import com.zinc.common.models.TopProfile
import com.zinc.domain.models.OtherHomeResponse

fun OtherHomeResponse.OtherHomeInfo.toUiModel(): OtherProfileHomeData {
    return OtherProfileHomeData(
        profile = TopProfile(
            isFollowed = isFollowing.isYes(),
            name = name,
            imgUrl = imgUrl,
            percent = run {
                val completed = bucketInfo?.completedCount?.toFloat() ?: 0f
                val total = bucketInfo?.totalCount?.toFloat() ?: 0f
                if (total > 0f) completed / total else 0f
            },
            badgeImgUrl = badgeImgUrl,
            badgeTitle = badgeTitle,
            bio = bio,
            followerCount = followerCount.toString(),
            followingCount = followingCount.toString()
        ), bucketList = bucketInfo?.bucketlist?.map {
            OtherProfileHomeData.OtherBucketInfo(
                title = it.title,
                bucketId = it.id,
                isProgress = it.status == BucketStatus.PROGRESS
            )
        }.orEmpty()
    )
}