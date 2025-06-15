package com.zinc.domain.repository

import com.zinc.domain.models.OtherHomeResponse

interface OtherRepository {
    suspend fun loadOtherHome(otherUserId: String): OtherHomeResponse
}