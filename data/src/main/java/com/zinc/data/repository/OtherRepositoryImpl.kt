package com.zinc.data.repository

import com.zinc.data.api.WaverApi
import com.zinc.domain.models.OtherHomeResponse
import com.zinc.domain.repository.OtherRepository
import javax.inject.Inject

class OtherRepositoryImpl @Inject constructor(
    private val waverApi: WaverApi
) : OtherRepository {
    override suspend fun loadOtherHome(otherUserId: String): OtherHomeResponse {
        return waverApi.loadOtherHome(otherUserId)
    }
}