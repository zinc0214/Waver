package com.zinc.domain.repository

import com.zinc.common.models.MyProfileResponse

interface MoreRepository {
    suspend fun loadProfileInfo(token: String): MyProfileResponse
}