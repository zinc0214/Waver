package com.zinc.domain.repository

import com.zinc.common.models.BlockedUserResponse
import com.zinc.common.models.CommonResponse
import com.zinc.common.models.LoadMyWaveInfoResponse
import com.zinc.common.models.ProfileResponse
import com.zinc.domain.models.UpdateProfileRequest

interface MoreRepository {
    suspend fun loadProfileInfo(): ProfileResponse
    suspend fun updateProfileInfo(request: UpdateProfileRequest): CommonResponse
    suspend fun checkAlreadyUsedNickName(name: String): CommonResponse
    suspend fun loadBlockedUsers(): BlockedUserResponse
    suspend fun loadMyWaeInfo(): LoadMyWaveInfoResponse
}