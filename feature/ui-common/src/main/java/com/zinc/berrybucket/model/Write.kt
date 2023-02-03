package com.zinc.berrybucket.model

import android.os.Parcelable
import com.zinc.common.models.BucketType
import com.zinc.common.models.CategoryInfo
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
            is WriteOption1Info.GoalCount -> info.goalCount
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

    fun getGoalCount(): String {
        val goalCount = options.firstOrNull { it.info is WriteOption1Info.GoalCount } ?: return "1"
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

data class WriteKeyWord(
    val id: String,
    val text: String
)

data class WriteFriend(
    val id: String,
    val imageUrl: String,
    val nickname: String
)

enum class WriteOptionsType1 {
    MEMO, IMAGE, CATEGORY, D_DAY, GOAL
}

sealed class WriteOption1Info {
    data class Memo(val memo: String = "") : WriteOption1Info()
    data class Dday(val localDate: LocalDate, val dDayText: String = "") : WriteOption1Info()
    data class GoalCount(val goalCount: String = "1") : WriteOption1Info()
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

fun ParseUIBucketListInfo(
    writeInfo1: WriteInfo1,
    keyWord: List<String>,
    tagFriends: List<String>,
    isScrapAvailable: Boolean = false
) = UIAddBucketListInfo(
    bucketType = BucketType.ORIGINAL,
    content = writeInfo1.title,
    memo = parseMemo(writeInfo1.options),
    images = parseImages(writeInfo1.options),
    targetDate = parseTargetDate(writeInfo1.options),
    goalCount = parseGoalCount(writeInfo1.options),
    categoryId = parseCategoryId(writeInfo1.options),
    tags = keyWord
)

private fun parseMemo(options: List<WriteOption>): String? {
    val memo = options.firstOrNull { it.info is WriteOption1Info.Memo } ?: return null
    val info = memo.info as WriteOption1Info.Memo
    return info.memo
}

private fun parseImages(options: List<WriteOption>): List<File> {
    val images = options.firstOrNull { it.info is WriteOption1Info.Images } ?: return emptyList()
    val info = images.info as WriteOption1Info.Images
    return info.images.map { it.file }
}

private fun parseTargetDate(options: List<WriteOption>): String? {
    val dday = options.firstOrNull { it.info is WriteOption1Info.Dday } ?: return null
    val info = dday.info as WriteOption1Info.Dday
    return info.dDayText.replace(".", "-")
}

private fun parseGoalCount(options: List<WriteOption>): String {
    val goalCount = options.firstOrNull { it.info is WriteOption1Info.GoalCount } ?: return "1"
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
    val content: String, // 내용
    val memo: String?, // 메모
    val tags: List<String> = emptyList(), // 태그 목록 (최대 5)
    val images: List<File> = emptyList(), // 이미지 목록(최대 3)
    val targetDate: String?, // 목표완료일(yyyy-MM-dd)
    val goalCount: String = "1", //  목표 횟수
    val categoryId: Int // 카테고리 ID
)