package com.zinc.berrybucket.model

import androidx.annotation.StringRes
import com.zinc.berrybucket.ui.design.theme.Error2
import com.zinc.berrybucket.ui.design.theme.Gray6
import com.zinc.berrybucket.ui.design.theme.Main4
import com.zinc.berrybucket.ui.design.theme.Sub_D3
import com.zinc.berrybucket.ui_common.R
import com.zinc.common.models.BucketInfoSimple
import com.zinc.common.models.BucketStatus
import com.zinc.common.models.CategoryInfo
import com.zinc.common.models.DetailType

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
        type = setBucketType(this.dDay),
        id = this.id,
        title = this.title,
        currentCount = this.userCount,
        goalCount = this.goalCount,
        dDay = this.dDay,
        status = this.status,
        detailType = this.detailType ?: DetailType.MY_CLOSE
    )
}

private fun setBucketType(dDay: Int?): BucketType {
    return if (dDay == null) BucketType.BASIC
    else if (dDay < 0) BucketType.D_MINUS
    else if (dDay == 0) BucketType.D_DAY
    else BucketType.D_PLUS
}

data class UIBucketInfoSimple(
    val type: BucketType = BucketType.BASIC,
    val id: String,
    val title: String,
    var currentCount: Int = 0,
    val goalCount: Int = 0,
    val dDay: Int? = null,
    val detailType: DetailType = DetailType.MY_CLOSE,
    val status: BucketStatus = BucketStatus.PROGRESS
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
            if (type == BucketType.BASIC) {
                if (it <= 0) Sub_D3 else Error2
            } else if (type == BucketType.CHALLENGE) {
                if (it <= 0) Main4 else Gray6
            } else {
                Sub_D3
            }
        }

    val isProgress = status == BucketStatus.PROGRESS

    fun currentCountText() = currentCount.toString()
    fun goalCountText() = goalCount.toString()
}

enum class BucketType(
    val text: String
) {
    BASIC("BASIC"),
    D_DAY("D_DAY"),
    D_PLUS("D_PLUS"),
    D_MINUS("D_MINUS"),
    CHALLENGE("CHALLENGE")
}

enum class MyTabType(
    @StringRes val title: Int
) {
    ALL(title = R.string.allTab),
    CATEGORY(title = R.string.categoryTab),
    DDAY(title = R.string.ddayTab),
    CHALLENGE(title = R.string.challengeTab)
}

sealed class MyPagerClickEvent {
    object FilterClicked : MyPagerClickEvent()
    object CategoryEditClicked : MyPagerClickEvent()
    data class SearchClicked(val tabType: MyTabType) : MyPagerClickEvent()
    data class BucketItemClicked(val info: UIBucketInfoSimple) : MyPagerClickEvent()
    data class CategoryItemClicked(val categoryInfo: CategoryInfo) : MyPagerClickEvent()
    object AddNewCategory : MyPagerClickEvent()
}

sealed class MySearchClickEvent {
    object CloseClicked : MySearchClickEvent()
    data class ItemClicked(val info: UIBucketInfoSimple) : MySearchClickEvent()
}

