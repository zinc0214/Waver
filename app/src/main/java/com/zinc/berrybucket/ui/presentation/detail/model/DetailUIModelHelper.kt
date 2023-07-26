package com.zinc.berrybucket.ui.presentation.detail.model

import com.zinc.berrybucket.model.BucketDetailUiInfo
import com.zinc.berrybucket.model.CommonDetailDescInfo
import com.zinc.berrybucket.model.ImageInfo
import com.zinc.berrybucket.model.MemoInfo
import com.zinc.berrybucket.model.ProfileInfo
import com.zinc.common.models.DetailInfo
import com.zinc.common.models.MyProfileInfo

fun bucketDetailResponseToUiModel(
    bucketInfo: DetailInfo,
    profileInfo: MyProfileInfo
) = BucketDetailUiInfo(
    bucketId = bucketInfo.id,
    imageInfo = if (bucketInfo.images != null) ImageInfo(bucketInfo.images!!) else null,
    profileInfo = ProfileInfo(
        profileImage = profileInfo.imgUrl,
        badgeImage = profileInfo.badgeUrl.orEmpty(),
        titlePosition = profileInfo.bio,
        nickName = profileInfo.name
    ),
    descInfo = CommonDetailDescInfo(
        dDay = bucketInfo.completedDt,
        tagList = bucketInfo.keywords,
        title = bucketInfo.title,
        goalCount = bucketInfo.goalCount,
        userCount = bucketInfo.userCount
    ),
    memoInfo = if (bucketInfo.memo != null) MemoInfo(bucketInfo.memo!!) else null,
    commentInfo = null,
    togetherInfo = null
)

