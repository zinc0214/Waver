package com.zinc.waver.model

import com.zinc.common.models.BucketStatus

sealed class HomeItemSelected {
    data class GoToDetailHomeItem(val bucketInfo: UIBucketInfoSimple) : HomeItemSelected()
    data class GoToCategoryBucketList(val categoryInfo: UICategoryInfo) : HomeItemSelected()
    data class GoToStatusBucketList(val bucketStatus: BucketStatus) : HomeItemSelected()
}