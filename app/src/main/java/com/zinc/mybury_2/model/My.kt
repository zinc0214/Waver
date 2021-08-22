package com.zinc.mybury_2.model

import com.zinc.domain.models.BucketInfoSimple

data class AllBucketList(
        val proceedingBucketCount: String,
        val succeedBucketCount: String,
        val bucketList: List<BucketInfoSimple>
)