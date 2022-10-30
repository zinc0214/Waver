package com.zinc.berrybucket.model

sealed class BucketSelected {
    data class GoToDetailBucket(val bucketInfo: UIBucketInfoSimple) : BucketSelected()
}