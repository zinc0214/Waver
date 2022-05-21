package com.zinc.berrybucket.model

import com.zinc.berrybucket.R
import com.zinc.berrybucket.compose.theme.Error2
import com.zinc.berrybucket.compose.theme.Gray6
import com.zinc.berrybucket.compose.theme.Main4
import com.zinc.berrybucket.compose.theme.Sub_D3
import com.zinc.common.models.BucketInfoSimple

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
        type = BucketType.values().find { it.text == this.type } ?: BucketType.BASIC,
        id = this.id,
        title = this.title,
        currentCount = this.currentCount,
        goalCount = this.goalCount,
        dDay = this.dDay,
        detailType = DetailType.values().find { it.text == this.detailType } ?: DetailType.MY_OPEN
    )
}

data class UIBucketInfoSimple(
    val type: BucketType = BucketType.BASIC,
    val id: String,
    val title: String,
    var currentCount: Int = 0,
    val goalCount: Int = 0,
    val dDay: Int? = null,
    val detailType: DetailType = DetailType.MY_CLOSE
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

    fun currentCountText() = currentCount.toString()
    fun goalCountText() = goalCount.toString()
}


enum class BucketProgressState {
    STARTED, PROGRESS_END, FINISHED
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

enum class TabType {
    ALL, CATEGORY, D_DAY, CHALLENGE;

    companion object {
        fun getNameResource(type: TabType): Int {
            return when (type) {
                ALL -> {
                    R.string.allTab
                }
                CATEGORY -> {
                    R.string.categoryTab
                }
                D_DAY -> {
                    R.string.ddayTab
                }
                else -> {
                    R.string.challengeTab
                }
            }
        }
    }
}


sealed class MyClickEvent : IconClickEvent() {
    object FilterClicked : MyClickEvent()
    object CloseClicked : MyClickEvent()
}

data class ItemClicked(val info: UIBucketInfoSimple) : MyClickEvent()
data class SearchClicked(val tabType: TabType) : MyClickEvent()
