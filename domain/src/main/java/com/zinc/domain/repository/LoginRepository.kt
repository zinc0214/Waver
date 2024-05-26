package com.zinc.domain.repository

import com.zinc.common.models.CreateProfileRequest
import com.zinc.common.models.JoinResponse
import com.zinc.common.models.LoadTokenByEmailRequest
import com.zinc.common.models.LoadTokenByEmailResponse
import com.zinc.common.models.RefreshTokenResponse

interface LoginRepository {
    suspend fun createProfile(createProfileRequest: CreateProfileRequest): JoinResponse
    suspend fun refreshToken(): RefreshTokenResponse
    suspend fun requestLogin(loginRequest: LoadTokenByEmailRequest): LoadTokenByEmailResponse
}