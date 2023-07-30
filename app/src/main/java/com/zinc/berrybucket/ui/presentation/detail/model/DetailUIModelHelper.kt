package com.zinc.berrybucket.ui.presentation.detail.model

import com.zinc.berrybucket.model.BucketDetailUiInfo
import com.zinc.berrybucket.model.CommonDetailDescInfo
import com.zinc.berrybucket.model.ImageInfo
import com.zinc.berrybucket.model.MemoInfo
import com.zinc.berrybucket.model.ProfileInfo
import com.zinc.berrybucket.model.UserSelectedImageInfo
import com.zinc.berrybucket.model.WriteCategoryInfo
import com.zinc.berrybucket.model.WriteFriend
import com.zinc.berrybucket.model.WriteKeyWord
import com.zinc.berrybucket.model.WriteOpenType
import com.zinc.berrybucket.model.WriteOption1Info
import com.zinc.berrybucket.model.WriteTotalInfo
import com.zinc.berrybucket.ui.util.parseWithDday
import com.zinc.common.models.BucketStatus
import com.zinc.common.models.DetailInfo
import com.zinc.common.models.MyProfileInfo
import com.zinc.common.models.YesOrNo
import java.time.LocalDate

fun bucketDetailResponseToUiModel(
    bucketInfo: DetailInfo,
    profileInfo: MyProfileInfo
) = BucketDetailUiInfo(
    bucketId = bucketInfo.id,
    writeOpenType = WriteOpenType.PUBLIC, // TODO : 변경필요
    imageInfo = if (bucketInfo.images.isNullOrEmpty()) null else ImageInfo(bucketInfo.images!!),
    profileInfo = ProfileInfo(
        profileImage = profileInfo.imgUrl,
        badgeImage = profileInfo.badgeUrl.orEmpty(),
        titlePosition = profileInfo.bio,
        nickName = profileInfo.name
    ),
    descInfo = CommonDetailDescInfo(
        dDay = bucketInfo.completedDt,
        keywordList = bucketInfo.keywords?.map {
            WriteKeyWord(it.id, it.name)
        },
        title = bucketInfo.title,
        goalCount = bucketInfo.goalCount,
        userCount = bucketInfo.userCount,
        categoryInfo = WriteCategoryInfo(
            id = 0, name = bucketInfo.categoryName, defaultYn = YesOrNo.Y
        ),
        isScrap = bucketInfo.scrapYn.isYes(),
        status = if (bucketInfo.status == DetailInfo.Status.PROGRESS) BucketStatus.PROGRESS else BucketStatus.COMPLETE
    ),
    memoInfo = if (bucketInfo.memo.isNullOrEmpty()) null else MemoInfo(bucketInfo.memo!!),
    commentInfo = null,
    togetherInfo = null
)


fun BucketDetailUiInfo.toUpdateUiModel(
    imagesList: List<UserSelectedImageInfo>
) = WriteTotalInfo(
    bucketId = this.bucketId,
    title = this.descInfo.title,
    options = getOptions(imagesList),
    writeOpenType = this.writeOpenType,
    keyWord = this.descInfo.keywordList.orEmpty(),
    tagFriends = this.togetherInfo?.togetherMembers?.map {
        WriteFriend(
            id = it.memberId, imageUrl = it.profileImage, nickname = it.nickName
        )
    }.orEmpty(),
    isScrapUsed = this.descInfo.isScrap
)

private fun BucketDetailUiInfo.getOptions(imagesList: List<UserSelectedImageInfo>): List<WriteOption1Info> {
    val optionsList = mutableListOf<WriteOption1Info>()

    memoInfo?.let {
        optionsList.add(
            WriteOption1Info.Memo(it.memo)
        )
    }

    descInfo.dDay?.let {
        val dDayLocalDate = LocalDate.of(2023, 10, 2)
        optionsList.add(
            WriteOption1Info.Dday(dDayLocalDate, dDayLocalDate.parseWithDday())
        )
    }

    optionsList.add(
        WriteOption1Info.GoalCount(descInfo.goalCount)
    )

    optionsList.add(
        WriteOption1Info.Category(descInfo.categoryInfo)
    )

    optionsList.add(
        WriteOption1Info.Images(imagesList)
    )

    return optionsList

}
