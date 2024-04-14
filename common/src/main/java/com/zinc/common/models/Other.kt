package com.zinc.common.models

data class OtherProfileLoad(
    val isSuccess: Boolean,
    val data: OtherProfileHomeData?
)

data class OtherProfileHomeData(
    val profile: TopProfile,
    val bucketList: List<OtherBucketInfo>
) {
    data class OtherBucketInfo(
        val title: String,
        val bucketId: Int,
        val isProgress: Boolean
    )
}