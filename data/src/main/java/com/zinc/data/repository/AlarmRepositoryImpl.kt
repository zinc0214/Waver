package com.zinc.data.repository

import com.zinc.common.models.AlarmList
import com.zinc.data.api.WaverApi
import com.zinc.domain.repository.AlarmRepository
import javax.inject.Inject

internal class AlarmRepositoryImpl @Inject constructor(
    private val waverApi: WaverApi
) : AlarmRepository {
    override suspend fun loadAlarmList(): AlarmList {
        return waverApi.loadAlarmList()
    }
}