package com.zinc.domain.repository

import com.zinc.common.models.AlarmList

interface AlarmRepository {
    suspend fun loadAlarmList(token: String): AlarmList
}