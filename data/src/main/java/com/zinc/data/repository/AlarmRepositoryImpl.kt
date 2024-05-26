package com.zinc.data.repository

import com.zinc.common.models.AlarmList
import com.zinc.data.api.BerryBucketApi
import com.zinc.domain.repository.AlarmRepository
import javax.inject.Inject

internal class AlarmRepositoryImpl @Inject constructor(
    private val berryBucketApi: BerryBucketApi
) : AlarmRepository {
    override suspend fun loadAlarmList(): AlarmList {
        return berryBucketApi.loadAlarmList()
    }
}