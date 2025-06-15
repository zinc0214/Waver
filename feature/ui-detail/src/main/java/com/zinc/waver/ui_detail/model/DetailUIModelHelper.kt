package com.zinc.waver.ui_detail.model

import com.zinc.common.models.BucketStatus
import com.zinc.common.models.DetailInfo
import com.zinc.common.models.ProfileInfo
import com.zinc.common.models.YesOrNo
import com.zinc.waver.model.BucketDetailUiInfo
import com.zinc.waver.model.Comment
import com.zinc.waver.model.DetailDescType
import com.zinc.waver.model.UserSelectedImageInfo
import com.zinc.waver.model.WriteCategoryInfo
import com.zinc.waver.model.WriteFriend
import com.zinc.waver.model.WriteKeyWord
import com.zinc.waver.model.WriteOpenType
import com.zinc.waver.model.WriteOption1Info
import com.zinc.waver.model.WriteTotalInfo
import com.zinc.waver.ui.util.parseWithDday
import com.zinc.waver.ui.util.toLocalData
import com.zinc.waver.ui.util.toStringData

fun bucketDetailResponseToUiModel(
    bucketInfo: DetailInfo,
    profileInfo: ProfileInfo, // TODO : 글쓴사람 프로필로 바꿔야함
    isMine: Boolean,
    writerId: String?
): BucketDetailUiInfo {

    val writerProfileInfoUi = DetailDescType.WriterProfileInfoUi(
        profileImage = profileInfo.imgUrl,
        badgeImage = profileInfo.badgeImgUrl.orEmpty(),
        titlePosition = profileInfo.bio.orEmpty(),
        nickName = profileInfo.name,
        userId = writerId.orEmpty()
    )

    val descInfo = DetailDescType.CommonDetailDescInfo(
        dDay = bucketInfo.completedDt,
        keywordList = bucketInfo.keywords.map {
            WriteKeyWord(it.id.orEmpty(), it.name)
        },
        title = bucketInfo.title,
        goalCount = bucketInfo.goalCount,
        userCount = bucketInfo.userCount,
        categoryInfo = WriteCategoryInfo(
            id = bucketInfo.category.id, name = bucketInfo.category.name, defaultYn = YesOrNo.Y
        ),
        isScrap = bucketInfo.pin.isYes(),
        status = if (bucketInfo.status == DetailInfo.CompleteStatus.PROGRESS) BucketStatus.PROGRESS else BucketStatus.COMPLETE,
        isMine = isMine
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
        isDone = bucketInfo.status == DetailInfo.CompleteStatus.COMPLETE,
        isLiked = false // TODO : 데이터 받아야됨
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
