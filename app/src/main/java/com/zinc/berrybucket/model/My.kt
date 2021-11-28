package com.zinc.berrybucket.model

import com.zinc.berrybucket.R
import com.zinc.berrybucket.compose.theme.Error2
import com.zinc.berrybucket.compose.theme.Gray6
import com.zinc.berrybucket.compose.theme.Main4
import com.zinc.berrybucket.compose.theme.Sub_D3

data class AllBucketList(
    val proceedingBucketCount: String,
    val succeedBucketCount: String,
    val bucketList: List<BucketInfoSimple>
)

data class DDayBucketList(
    val bucketList: List<BucketInfoSimple>
)


data class BucketInfoSimple(
    val type: BucketType = BucketType.BASIC,
    val id: String,
    val title: String,
    var currentCount: Int = 0,
    val goalCount: Int = 0,
    val dDay: Int? = null
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
    STARTED, BACK, FINISHED
}

enum class BucketType {
    BASIC, D_DAY, D_PLUS, D_MINUS, CHALLENGE
}

enum class TabType {
    ALL, D_DAY, CATEGORY, CHALLENGE;

    companion object {
        fun getNameResource(type: TabType): Int {
            return when (type) {
                ALL -> {
                    R.string.allTab
                }
                D_DAY -> {
                    R.string.ddayTab
                }
                CATEGORY -> {
                    R.string.categoryTab
                }
                else -> {
                    R.string.challengeTab
                }
            }
        }
    }
}


sealed class MyClickEvent {
    object SearchClicked : MyClickEvent()
    object FilterClicked : MyClickEvent()
    object CloseClicked : MyClickEvent()
}
