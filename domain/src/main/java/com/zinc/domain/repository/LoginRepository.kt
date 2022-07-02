package com.zinc.domain.repository

import com.zinc.common.models.JoinRequest
import com.zinc.common.models.JoinResponse

interface LoginRepository {
    suspend fun joinBerryBucket(joinRequest: JoinRequest): JoinResponse
}