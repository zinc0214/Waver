package com.zinc.domain.repository

import com.zinc.common.models.CommonResponse
import com.zinc.common.models.CreateProfileRequest
import com.zinc.common.models.JoinEmailCheck
import com.zinc.common.models.JoinResponse
import com.zinc.common.models.LoginRequest
import com.zinc.common.models.RefreshTokenResponse

interface LoginRepository {
    suspend fun checkEmail(email: String): CommonResponse
    suspend fun createUserToken(joinEmailCheck: JoinEmailCheck): JoinResponse
    suspend fun createProfile(
        token: String,
        createProfileRequest: CreateProfileRequest
    ): JoinResponse

    suspend fun loginBerryBucket(token: String, loginRequest: LoginRequest): CommonResponse
    suspend fun refreshToken(token: String): RefreshTokenResponse
}