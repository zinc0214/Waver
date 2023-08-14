package com.zinc.berrybucket.model

import android.os.Parcelable
import com.zinc.berrybucket.util.parseNavigationValue
import com.zinc.berrybucket.util.toNavigationValue
import com.zinc.common.models.BucketType
import com.zinc.common.models.ExposureStatus
import com.zinc.common.models.KeywordInfo
import com.zinc.common.models.YesOrNo
import com.zinc.common.utils.toYn
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Contextual
import java.io.File
import java.io.Serializable
import java.time.LocalDate
import java.util.Date

data class WriteAddOption(
    val type: WriteOptionsType2,
    val title: String,
    val tagList: List<String>,
    val showDivider: Boolean = false,
    val clicked: (WriteOptionsType2) -> Unit
) : Serializable

@Parcelize
@kotlinx.serialization.Serializable
data class WriteTotalInfo(
    val bucketId: String? = null,
    val title: String = "",
    val options: List<WriteOption1Info> = emptyList(),
    val writeOpenType: WriteOpenType = WriteOpenType.PUBLIC,
    val keyWord: List<WriteKeyWord> = emptyList(),
    val tagFriends: List<WriteFriend> = emptyList(),
    val isScrapUsed: Boolean = false
) : Serializable, Parcelable {
    companion object {
        fun toNavigationValue(value: WriteTotalInfo): String =
            value.toNavigationValue()

        fun parseNavigationValue(value: String): WriteTotalInfo =
            value.parseNavigationValue()
    }
}

fun WriteTotalInfo.parseWrite1Info() = WriteInfo1(this.title, this.options)

data class WriteInfo1(
    val title: String = "",
    val options: List<WriteOption1Info> = emptyList(),
) {
    fun getMemo(): String {
        val memoInfo = options.firstOrNull { it is WriteOption1Info.Memo } ?: return ""
        return (memoInfo as WriteOption1Info.Memo).memo
    }

    fun getDday(): WriteOption1Info.Dday? {
        val dDayInfo = options.firstOrNull { it is WriteOption1Info.Dday } ?: return null
        return (dDayInfo as WriteOption1Info.Dday)
    }

    fun getGoalCount(): Int {
        val goalCount = options.firstOrNull { it is WriteOption1Info.GoalCount } ?: return 1
        return (goalCount as WriteOption1Info.GoalCount).goalCount
    }

    fun getImagesPaths(): List<String> {
        val images =
            options.firstOrNull { it is WriteOption1Info.Images } ?: return emptyList()
        return (images as WriteOption1Info.Images).imagePaths
    }

    fun getCategory(): WriteCategoryInfo {
        val categoryInfo = options.firstOrNull { it is WriteOption1Info.Category }
        return (categoryInfo as WriteOption1Info.Category).categoryInfo
    }
}

fun List<KeywordInfo>.toUiModel(): List<WriteKeyWord> {
    return map {
        WriteKeyWord(it.id, it.name)
    }
}

@kotlinx.serialization.Serializable
data class WriteKeyWord(
    val id: Int,
    val text: String
) : Serializable

@kotlinx.serialization.Serializable
data class WriteFriend(
    val id: String,
    val imageUrl: String,
    val nickname: String
) : Serializable

enum class WriteOptionsType1 : Serializable {
    MEMO, IMAGE, CATEGORY, D_DAY, GOAL
}

enum class WriteOpenType(val text: String) {
    PUBLIC("전체 공개"), PRIVATE("비공개"), FRIENDS_OPEN("친구에게만 공개")
}

@Parcelize
@kotlinx.serialization.Serializable
sealed class WriteOption1Info : Serializable, Parcelable {
    @kotlinx.serialization.Serializable
    data class Memo(val memo: String = "") : WriteOption1Info(), Serializable

    @kotlinx.serialization.Serializable
    data class Dday(val localDate: String, val dDayText: String = "") :
        WriteOption1Info(), Serializable

    @kotlinx.serialization.Serializable
    data class GoalCount(val goalCount: Int = 1) : WriteOption1Info(), Serializable

    @kotlinx.serialization.Serializable
    data class Category(val categoryInfo: WriteCategoryInfo) : WriteOption1Info(), Serializable

    @kotlinx.serialization.Serializable
    data class Images(val imagePaths: List<String> = emptyList()) : WriteOption1Info(),
        Serializable

    fun content(): String {
        return when (this) {
            is Category -> categoryInfo.name
            is Dday -> dDayText
            is GoalCount -> goalCount.toString()
            is Memo -> memo
            is Images -> ""
        }
    }

    fun title(): String {
        return when (this) {
            is Category -> "카테고리"
            is Dday -> "디데이"
            is GoalCount -> "목표 달성 횟수"
            is Memo -> "메모"
            is Images -> "이미지"
        }
    }

    fun type(): WriteOptionsType1 {
        return when (this) {
            is Category -> WriteOptionsType1.CATEGORY
            is Dday -> WriteOptionsType1.D_DAY
            is GoalCount -> WriteOptionsType1.GOAL
            is Images -> WriteOptionsType1.IMAGE
            is Memo -> WriteOptionsType1.MEMO
        }
    }
}

@Parcelize
@kotlinx.serialization.Serializable
data class WriteCategoryInfo(
    val id: Int,
    val name: String,
    val defaultYn: YesOrNo = YesOrNo.Y
) : Serializable, Parcelable

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
    title: String = "",
    options: List<WriteOption1Info> = emptyList(),
    writeOpenType: WriteOpenType,
    imageFiles: List<File>,
    keyWord: List<String>,
    tagFriends: List<String>,
    isScrapAvailable: Boolean = false
) = UIAddBucketListInfo(
    bucketType = BucketType.ORIGINAL,
    title = title,
    memo = parseMemo(options),
    images = imageFiles,
    targetDate = parseTargetDate(options),
    goalCount = parseGoalCount(options),
    categoryId = parseCategoryId(options),
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

private fun parseMemo(options: List<WriteOption1Info>): String? {
    return options.firstOrNull { it is WriteOption1Info.Memo }?.content()
}

private fun parseTargetDate(options: List<WriteOption1Info>): String? {
    return options.firstOrNull { it is WriteOption1Info.Dday }?.content()?.replace(".", "-")
}

private fun parseGoalCount(options: List<WriteOption1Info>): Int {
    return options.firstOrNull { it is WriteOption1Info.GoalCount }?.let {
        (it as WriteOption1Info.GoalCount).goalCount
    } ?: 1
}

private fun parseCategoryId(options: List<WriteOption1Info>): Int {
    return options.firstOrNull { it is WriteOption1Info.Category }?.let {
        (it as WriteOption1Info.Category).categoryInfo.id
    } ?: 0
}

data class UIAddBucketListInfo(
    val bucketType: BucketType,
    val exposureStatus: ExposureStatus, // 공개여부
    val title: String,
    val memo: String?, // 메모
    val tags: String = "", // 태그 목록(최대 5) - ","로 구분
    val friendUserIds: List<String>?, // 함께할 친구 ID - 함께하기인 경우 최소 1명 필수(최대 5)
    val scrapYn: YesOrNo, // 스크랩 여부
    val images: List<File>? = emptyList(), // 이미지 목록(최대 3)
    val targetDate: String?, // 목표완료일(yyyy-MM-dd)
    val goalCount: Int = 0, //  목표 횟수
    val categoryId: Int = 0 // 카테고리 ID
)