package com.zinc.waver.model

import com.zinc.common.models.BucketInfoSimple
import com.zinc.common.models.BucketStatus
import com.zinc.common.models.BucketType
import com.zinc.common.models.DdaySortType
import com.zinc.common.models.ExposureStatus
import com.zinc.waver.ui.design.theme.Error2
import com.zinc.waver.ui.design.theme.Gray6
import com.zinc.waver.ui.design.theme.Main4
import com.zinc.waver.ui.design.theme.Sub_D3
import com.zinc.waver.ui_common.R
import com.zinc.waver.util.parseNavigationValue
import com.zinc.waver.util.toNavigationValue
import java.io.Serializable

data class AllBucketList(
    val processingCount: String,
    val succeedCount: String,
    val bucketList: List<UIBucketInfoSimple>
)

fun List<BucketInfoSimple>.parseToUI(): List<UIBucketInfoSimple> {
    val list = mutableListOf<UIBucketInfoSimple>()
    this.forEach {
        list.add(it.parseToUI())
    }
    return list
}

fun BucketInfoSimple.parseToUI(): UIBucketInfoSimple {
    return UIBucketInfoSimple(
        id = this.id,
        title = this.title,
        currentCount = this.userCount,
        goalCount = this.goalCount,
        dDay = this.dDay,
        status = this.status,
        exposureStatues = this.exposureStatus,
        bucketType = this.bucketType
    )
}

data class UIBucketInfoSimple(
    val id: String,
    val title: String,
    var currentCount: Int = 0,
    val goalCount: Int = 0,
    val dDay: Int? = null,
    val bucketType: BucketType,
    val exposureStatues: ExposureStatus,
    val status: BucketStatus = BucketStatus.PROGRESS,
    val isChallenge: Boolean = false
) {
    val dDayText = dDay?.let {
        when {
            it == 0 -> "D-day"
            it < 0 -> "D$it"
            else -> "D+$it"
        }
    }

    val dDayBadgeColor =
        dDay?.let {
            if (isChallenge.not()) {
                if (it <= 0) Sub_D3 else Error2
            } else if (isChallenge) {
                if (it <= 0) Main4 else Gray6
            } else {
                Sub_D3
            }
        }

    fun getDdayType(): DdaySortType? = dDay?.let {
        if (it == 0) DdaySortType.D_DAY
        else if (it < 0) DdaySortType.MINUS
        else DdaySortType.PLUS
    }

    fun isProgress() = status == BucketStatus.PROGRESS && goalCount > currentCount
    fun isPrivate() = exposureStatues == ExposureStatus.PRIVATE
    fun goalCountText() = goalCount.toString()
}

@kotlinx.serialization.Serializable
sealed interface MyTabType : Serializable {

    @kotlinx.serialization.Serializable
    data object ALL : MyTabType, Serializable

    @kotlinx.serialization.Serializable
    data object CATEGORY : MyTabType, Serializable

    @kotlinx.serialization.Serializable
    data object DDAY : MyTabType, Serializable

    @kotlinx.serialization.Serializable
    data object CHALLENGE : MyTabType, Serializable

    fun getTitle() = when (this) {
        is ALL -> R.string.allTab
        is CATEGORY -> R.string.categoryTab
        is CHALLENGE -> R.string.challengeTab
        is DDAY -> R.string.ddayTab
    }


    companion object {
        fun toNavigationValue(value: MyTabType): String =
            value.toNavigationValue()

        fun parseNavigationValue(value: String): MyTabType =
            value.parseNavigationValue()

        fun values() = listOf(ALL, CATEGORY, DDAY)

    }
}

sealed interface MyPagerClickEvent {
    data class AchieveBucketClicked(val id: String) : MyPagerClickEvent


    sealed interface BottomSheet : MyPagerClickEvent {
        data class FilterClicked(val tabType: MyTabType, val isOpened: Boolean) : BottomSheet
        data class SearchClicked(val tabType: MyTabType, val isOpened: Boolean) : BottomSheet
    }

    sealed interface GoTo : MyPagerClickEvent {
        data class BucketItemClicked(val info: UIBucketInfoSimple) : GoTo
        data class CategoryItemClicked(val info: UICategoryInfo) : GoTo
        data object CategoryEditClicked : GoTo
    }
}

sealed class MySearchClickEvent {
    object CloseClicked : MySearchClickEvent()
    data class BucketItemClicked(val id: String, val isPrivate: Boolean) : MySearchClickEvent()
    data class CategoryItemClicked(val id: String) : MySearchClickEvent()
}

