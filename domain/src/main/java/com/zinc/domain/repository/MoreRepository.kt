package com.zinc.domain.repository

import com.zinc.common.models.CommonResponse
import com.zinc.common.models.CommonResponse2
import com.zinc.common.models.LoadMyWaveBadgeResponse
import com.zinc.common.models.LoadMyWaveInfoResponse
import com.zinc.common.models.ProfileResponse
import com.zinc.domain.models.UpdateProfileRequest

interface MoreRepository {
    suspend fun loadProfileInfo(): ProfileResponse
    suspend fun updateProfileInfo(request: UpdateProfileRequest): CommonResponse
    suspend fun checkAlreadyUsedNickName(name: String): CommonResponse2
    suspend fun loadMyBadgeInfo(): LoadMyWaveBadgeResponse
    suspend fun loadMyWaveInfo(): LoadMyWaveInfoResponse
    suspend fun requestWithdrawal(): CommonResponse
    suspend fun updateMyBadge(badgeId: Int): CommonResponse
}