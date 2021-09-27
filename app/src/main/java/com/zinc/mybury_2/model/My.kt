package com.zinc.mybury_2.model

import com.zinc.data.models.BucketInfoSimple

data class AllBucketList(
    val proceedingBucketCount: String,
    val succeedBucketCount: String,
    val bucketList: List<BucketInfoSimple>
)

enum class BucketProgressState {
    STARTED, BACK
}

enum class BucketType {
    BASIC, D_PLUS, D_MINUS
}