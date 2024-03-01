package com.zinc.domain.repository

import com.zinc.common.models.CreateProfileRequest
import com.zinc.common.models.EmailCheckResponse
import com.zinc.common.models.JoinEmailCheck
import com.zinc.common.models.JoinResponse
import com.zinc.common.models.LoadTokenByEmailRequest
import com.zinc.common.models.LoadTokenByEmailResponse
import com.zinc.common.models.RefreshTokenResponse

interface LoginRepository {
    suspend fun checkEmail(email: String): EmailCheckResponse
    suspend fun joinByEmail(joinEmailCheck: JoinEmailCheck): JoinResponse
    suspend fun createProfile(
        token: String,
        createProfileRequest: CreateProfileRequest
    ): JoinResponse

    suspend fun refreshToken(token: String): RefreshTokenResponse
    suspend fun requestLogin(loginRequest: LoadTokenByEmailRequest): LoadTokenByEmailResponse
}