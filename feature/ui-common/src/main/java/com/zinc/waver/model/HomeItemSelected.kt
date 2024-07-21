package com.zinc.waver.model

sealed class HomeItemSelected {
    data class GoToDetailHomeItem(val bucketInfo: UIBucketInfoSimple) : HomeItemSelected()
    data class GoToCategoryBucketList(val categoryInfo: UICategoryInfo) : HomeItemSelected()
}