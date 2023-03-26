package com.zinc.common.models

import kotlinx.serialization.Serializable

@Serializable
data class AlarmList(
    val alarmList: List<AlarmItem>
)

@Serializable
data class AlarmItem(
    val type: AlarmType,
    val title: String,
    val memberImg: String?,
    val bucketId: String?,
    val memberId: String?
)

enum class AlarmType {
    LIKE, COMMENT, DDAY, INFO, EVENT, TOGETHER, FRIEND,
}