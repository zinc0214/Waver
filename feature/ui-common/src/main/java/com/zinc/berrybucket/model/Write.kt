package com.zinc.berrybucket.model

import android.os.Parcelable
import com.zinc.common.models.BucketType
import com.zinc.common.models.CategoryInfo
import com.zinc.common.models.ExposureStatus
import com.zinc.common.models.YesOrNo
import com.zinc.common.utils.toYn
import kotlinx.parcelize.Parcelize
import java.io.File
import java.time.LocalDate

data class WriteOption(
    val type: WriteOptionsType1,
    val title: String,
    val info: WriteOption1Info
) : java.io.Serializable {
    fun content(): String {
        return when (info) {
            is WriteOption1Info.Category -> info.categoryInfo.name
            is WriteOption1Info.Dday -> info.dDayText
            is WriteOption1Info.GoalCount -> info.goalCount.toString()
            is WriteOption1Info.Memo -> info.memo
            is WriteOption1Info.Images -> ""
        }
    }
}

data class WriteAddOption(
    val type: WriteOptionsType2,
    val title: String,
    val tagList: List<String>,
    val showDivider: Boolean = false,
    val clicked: (WriteOptionsType2) -> Unit
) : java.io.Serializable

@Parcelize
data class WriteInfo1(
    val title: String = "",
    val options: List<WriteOption> = emptyList(),
) : java.io.Serializable, Parcelable {
    fun getMemo(): String {
        val memoInfo = options.firstOrNull { it.info is WriteOption1Info.Memo } ?: return ""
        return (memoInfo.info as WriteOption1Info.Memo).memo
    }

    fun getDday(): WriteOption1Info.Dday? {
        val dDayInfo = options.firstOrNull { it.info is WriteOption1Info.Dday } ?: return null
        return (dDayInfo.info as WriteOption1Info.Dday)
    }

    fun getGoalCount(): Int {
        val goalCount = options.firstOrNull { it.info is WriteOption1Info.GoalCount } ?: return 1
        return (goalCount.info as WriteOption1Info.GoalCount).goalCount
    }

    fun getImages(): List<UserSeletedImageInfo> {
        val images =
            options.firstOrNull { it.info is WriteOption1Info.Images } ?: return emptyList()
        return (images.info as WriteOption1Info.Images).images
    }

    fun getCategory(): CategoryInfo? {
        val categoryInfo = options.firstOrNull { it.info is WriteOption1Info.Category }
        return (categoryInfo?.info as WriteOption1Info.Category).categoryInfo
    }
}

@Parcelize
data class WriteInfo2(
    val writeOpenType: WriteOpenType = WriteOpenType.PUBLIC,
    val keyWord: List<WriteKeyWord> = emptyList(),
    val tagFriends: List<WriteFriend> = emptyList(),
    val isScrapUsed: Boolean = false
) : java.io.Serializable, Parcelable

@Parcelize
data class WriteTotalInfo(
    val writeInfo1: WriteInfo1,
    val writeInfo2: WriteInfo2
) : java.io.Serializable, Parcelable

@Parcelize
data class WriteKeyWord(
    val id: String,
    val text: String
) : java.io.Serializable, Parcelable

@Parcelize
data class WriteFriend(
    val id: String,
    val imageUrl: String,
    val nickname: String
) : java.io.Serializable, Parcelable

enum class WriteOptionsType1 {
    MEMO, IMAGE, CATEGORY, D_DAY, GOAL
}

enum class WriteOpenType(val text: String) {
    PUBLIC("전체 공개"), PRIVATE("비공개"), FRIENDS_OPEN("친구에게만 공개")
}

sealed class WriteOption1Info {
    data class Memo(val memo: String = "") : WriteOption1Info()
    data class Dday(val localDate: LocalDate, val dDayText: String = "") : WriteOption1Info()
    data class GoalCount(val goalCount: Int = 1) : WriteOption1Info()
    data class Category(val categoryInfo: CategoryInfo) : WriteOption1Info()
    data class Images(val images: List<UserSeletedImageInfo> = emptyList()) : WriteOption1Info()
}

interface WriteOptionsType2 {
    object TAG : WriteOptionsType2
    object FRIENDS : WriteOptionsType2
    object OPEN : WriteOptionsType2
    data class SCRAP(
        var isScrapAvailable: Boolean = false,
        var isScrapUsed: Boolean = false,
    ) : WriteOptionsType2
}

fun parseUIBucketListInfo(
    writeInfo1: WriteInfo1,
    writeOpenType: WriteOpenType,
    keyWord: List<String>,
    tagFriends: List<String>,
    isScrapAvailable: Boolean = false
) = UIAddBucketListInfo(
    bucketType = BucketType.ORIGINAL,
    title = writeInfo1.title,
    content = writeInfo1.title,
    memo = parseMemo(writeInfo1.options),
    images = parseImages(writeInfo1.options),
    targetDate = parseTargetDate(writeInfo1.options),
    goalCount = parseGoalCount(writeInfo1.options),
    categoryId = parseCategoryId(writeInfo1.options),
    tags = keyWord.joinToString(","),
    exposureStatus = parseOpenType(writeOpenType),
    friendUserIds = tagFriends,
    scrapYn = isScrapAvailable.toYn()
)


private fun parseOpenType(openType: WriteOpenType): ExposureStatus {
    return when (openType) {
        WriteOpenType.PUBLIC -> ExposureStatus.PUBLIC
        WriteOpenType.PRIVATE -> ExposureStatus.PRIVATE
        WriteOpenType.FRIENDS_OPEN -> ExposureStatus.FOLLOWER
    }
}

private fun parseMemo(options: List<WriteOption>): String? {
    val memo = options.firstOrNull { it.info is WriteOption1Info.Memo } ?: return null
    val info = memo.info as WriteOption1Info.Memo
    return info.memo
}

private fun parseImages(options: List<WriteOption>): List<File> {
    val images =
        options.firstOrNull { it.info is WriteOption1Info.Images } ?: return emptyList()
    val info = images.info as WriteOption1Info.Images
    return info.images.map { it.file }
}

private fun parseTargetDate(options: List<WriteOption>): String? {
    val dday = options.firstOrNull { it.info is WriteOption1Info.Dday } ?: return null
    val info = dday.info as WriteOption1Info.Dday
    return info.dDayText.replace(".", "-")
}

private fun parseGoalCount(options: List<WriteOption>): Int {
    val goalCount = options.firstOrNull { it.info is WriteOption1Info.GoalCount } ?: return 1
    val info = goalCount.info as WriteOption1Info.GoalCount
    return info.goalCount
}

private fun parseCategoryId(options: List<WriteOption>): Int {
    val category = options.firstOrNull { it.info is WriteOption1Info.Category } ?: return 0
    val info = category.info as WriteOption1Info.Category
    return info.categoryInfo.id
}

data class UIAddBucketListInfo(
    val bucketType: BucketType,
    val exposureStatus: ExposureStatus, // 공개여부
    val title: String,
    val content: String, // 내용
    val memo: String?, // 메모
    val tags: String?, // 태그 목록(최대 5) - ","로 구분
    val friendUserIds: List<String>?, // 함께할 친구 ID - 함께하기인 경우 최소 1명 필수(최대 5)
    val scrapYn: YesOrNo, // 스크랩 여부
    val images: List<File>? = emptyList(), // 이미지 목록(최대 3)
    val targetDate: String?, // 목표완료일(yyyy-MM-dd)
    val goalCount: Int = 0, //  목표 횟수
    val categoryId: Int = 0 // 카테고리 ID
)