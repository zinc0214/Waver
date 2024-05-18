package com.zinc.berrybucket.ui.presentation.detail.model

import com.zinc.berrybucket.model.BucketDetailUiInfo
import com.zinc.berrybucket.model.Comment
import com.zinc.berrybucket.model.DetailDescType
import com.zinc.berrybucket.model.UserSelectedImageInfo
import com.zinc.berrybucket.model.WriteCategoryInfo
import com.zinc.berrybucket.model.WriteFriend
import com.zinc.berrybucket.model.WriteKeyWord
import com.zinc.berrybucket.model.WriteOpenType
import com.zinc.berrybucket.model.WriteOption1Info
import com.zinc.berrybucket.model.WriteTotalInfo
import com.zinc.berrybucket.ui.util.parseWithDday
import com.zinc.berrybucket.ui.util.toLocalData
import com.zinc.berrybucket.ui.util.toStringData
import com.zinc.common.models.BucketStatus
import com.zinc.common.models.DetailInfo
import com.zinc.common.models.ProfileInfo
import com.zinc.common.models.YesOrNo

fun bucketDetailResponseToUiModel(
    bucketInfo: DetailInfo,
    profileInfo: ProfileInfo, // TODO : 글쓴사람 프로필로 바꿔야함
    isMine: Boolean
): BucketDetailUiInfo {

    val writerProfileInfoUi = DetailDescType.WriterProfileInfoUi(
        profileImage = profileInfo.imgUrl,
        badgeImage = profileInfo.badgeImgUrl.orEmpty(),
        titlePosition = profileInfo.bio.orEmpty(),
        nickName = profileInfo.name,
        userId = profileInfo.userId.orEmpty()
    )

    val descInfo = DetailDescType.CommonDetailDescInfo(
        dDay = bucketInfo.completedDt,
        keywordList = bucketInfo.keywordIds?.zip(bucketInfo.keywords.orEmpty()) { id, keyword ->
            WriteKeyWord(id, keyword)
        },
        title = bucketInfo.title,
        goalCount = bucketInfo.goalCount,
        userCount = bucketInfo.userCount,
        categoryInfo = WriteCategoryInfo(
            id = bucketInfo.categoryId, name = bucketInfo.categoryName, defaultYn = YesOrNo.Y
        ),
        isScrap = bucketInfo.scrapYn.isYes(),
        status = if (bucketInfo.status == DetailInfo.CompleteStatus.PROGRESS) BucketStatus.PROGRESS else BucketStatus.COMPLETE
    )

    val commentInfo = bucketInfo.comment?.let {
        DetailDescType.CommentInfo(it.size, it.map { comment ->
            Comment(
                commentId = comment.id,
                userId = comment.userId,
                profileImage = comment.imgUrl,
                nickName = comment.name,
                comment = comment.content,
                isMine = comment.isMyComment.isYes(),
                isBlocked = comment.isBlocked.isYes()
            )
        })
    }
    return BucketDetailUiInfo(
        bucketId = bucketInfo.id,
        writeOpenType = WriteOpenType.PUBLIC, // TODO : 변경필요
        imageInfo = if (bucketInfo.images.isNullOrEmpty()) null else DetailDescType.ImageInfo(
            bucketInfo.images!!
        ),
        writerProfileInfo = writerProfileInfoUi,
        descInfo = descInfo,
        memoInfo = if (bucketInfo.memo.isNullOrEmpty()) null else DetailDescType.MemoInfo(bucketInfo.memo!!),
        commentInfo = commentInfo,
        togetherInfo = null,
        isMine = isMine,
        isDone = bucketInfo.status == DetailInfo.CompleteStatus.COMPLETE
    )
}


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
    isScrapUsed = this.descInfo.isScrap,
    isForUpdate = false
)

private fun BucketDetailUiInfo.getOptions(imagesList: List<UserSelectedImageInfo>): List<WriteOption1Info> {
    val optionsList = mutableListOf<WriteOption1Info>()

    memoInfo?.let {
        optionsList.add(
            WriteOption1Info.Memo(it.memo)
        )
    }

    descInfo.dDay?.let {
        val dDayLocalDate = it.toLocalData()
        optionsList.add(
            WriteOption1Info.Dday(dDayLocalDate.toStringData(), dDayLocalDate.parseWithDday())
        )
    }

    optionsList.add(
        WriteOption1Info.GoalCount(descInfo.goalCount)
    )

    optionsList.add(
        WriteOption1Info.Category(descInfo.categoryInfo)
    )

    optionsList.add(
        WriteOption1Info.Images(imagesList.map { it.path })
    )

    return optionsList

}
