package com.zinc.berrybucket.model

sealed class HomeItemSelected {
    data class GoToDetailHomeItem(val bucketInfo: UIBucketInfoSimple) : HomeItemSelected()
    data class GoToCategoryBucketList(val categoryInfo: UICategoryInfo) : HomeItemSelected()
}