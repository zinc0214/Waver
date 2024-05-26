package com.zinc.data.repository

import com.zinc.common.models.CreateProfileRequest
import com.zinc.common.models.JoinResponse
import com.zinc.common.models.LoadTokenByEmailRequest
import com.zinc.common.models.LoadTokenByEmailResponse
import com.zinc.common.models.RefreshTokenResponse
import com.zinc.data.api.BerryBucketApi
import com.zinc.data.util.fileToMultipartFile
import com.zinc.data.util.toMultipartFile
import com.zinc.domain.repository.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val berryBucketApi: BerryBucketApi
) : LoginRepository {
    override suspend fun createProfile(
        createProfileRequest: CreateProfileRequest
    ): JoinResponse {
        val accountType = createProfileRequest.accountType.toMultipartFile("accountType")
        val email = createProfileRequest.email.toMultipartFile("email")
        val name = createProfileRequest.name.toMultipartFile("name")
        val bio = createProfileRequest.bio?.toMultipartFile("bio")
        val profileImage = createProfileRequest.profileImage?.fileToMultipartFile("profileImage")

        return berryBucketApi.crateProfile(accountType, email, name, bio, profileImage)
    }

    override suspend fun refreshToken(): RefreshTokenResponse {
        return berryBucketApi.refreshToken()
    }

    override suspend fun requestLogin(loginRequest: LoadTokenByEmailRequest): LoadTokenByEmailResponse {
        return berryBucketApi.requestLogin(loginRequest)
    }
}