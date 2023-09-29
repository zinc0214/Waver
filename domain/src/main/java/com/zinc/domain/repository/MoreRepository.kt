package com.zinc.domain.repository

import com.zinc.common.models.CommonResponse
import com.zinc.common.models.MyProfileResponse
import com.zinc.domain.models.UpdateProfileRequest

interface MoreRepository {
    suspend fun loadProfileInfo(token: String): MyProfileResponse
    suspend fun updateProfileInfo(token: String, request: UpdateProfileRequest): CommonResponse
    suspend fun checkAlreadyUsedNickName(token: String, name: String): CommonResponse
}