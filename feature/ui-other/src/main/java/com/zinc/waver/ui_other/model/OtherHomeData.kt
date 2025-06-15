package com.zinc.waver.ui_other.model

import com.zinc.common.models.BucketStatus
import com.zinc.common.models.OtherProfileHomeData
import com.zinc.common.models.TopProfile
import com.zinc.domain.models.OtherHomeResponse

fun OtherHomeResponse.OtherHomeInfo.toUiModel(): OtherProfileHomeData {
    return OtherProfileHomeData(
        profile = TopProfile(
            isFollowed = followYn?.isYes() ?: false,
            name = name,
            imgUrl = imgUrl,
            percent = completedCount.toFloat() / totalCount.toFloat(),
            badgeImgUrl = badgeImgUrl,
            badgeTitle = badgeTitle,
            bio = bio,
            followerCount = followerCount.toString(),
            followingCount = followingCount.toString()
        ), bucketList = bucketInfo.bucketlist.map {
            OtherProfileHomeData.OtherBucketInfo(
                title = it.title,
                bucketId = it.id,
                isProgress = it.status == BucketStatus.PROGRESS
            )
        }
    )
}