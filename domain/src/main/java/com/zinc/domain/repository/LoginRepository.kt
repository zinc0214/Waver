package com.zinc.domain.repository

import com.zinc.common.models.CommonResponse
import com.zinc.common.models.CreateProfileRequest
import com.zinc.common.models.JoinRequest
import com.zinc.common.models.JoinResponse
import com.zinc.common.models.LoginRequest

interface LoginRepository {
    suspend fun joinBerryBucket(joinRequest: JoinRequest): JoinResponse
    suspend fun createProfile(
        token: String,
        createProfileRequest: CreateProfileRequest
    ): CommonResponse

    suspend fun loginBerryBucket(token: String, loginRequest: LoginRequest): CommonResponse
}