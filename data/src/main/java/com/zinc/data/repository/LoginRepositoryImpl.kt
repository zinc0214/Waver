package com.zinc.data.repository

import com.zinc.common.models.CommonResponse
import com.zinc.common.models.CreateProfileRequest
import com.zinc.common.models.EmailCheckResponse
import com.zinc.common.models.JoinEmailCheck
import com.zinc.common.models.JoinResponse
import com.zinc.common.models.LoginRequest
import com.zinc.common.models.RefreshTokenResponse
import com.zinc.data.api.BerryBucketApi
import com.zinc.data.util.fileToMultipartFile
import com.zinc.data.util.toMultipartFile
import com.zinc.domain.models.CheckEmailIsLogined
import com.zinc.domain.repository.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val berryBucketApi: BerryBucketApi
) : LoginRepository {
    override suspend fun checkEmail(email: String): EmailCheckResponse {
        return berryBucketApi.checkEmailIsLogined(CheckEmailIsLogined(email))
    }

    override suspend fun createUserToken(joinEmailCheck: JoinEmailCheck): JoinResponse {
        return berryBucketApi.joinBerryBucket(joinEmailCheck)
    }

    override suspend fun createProfile(
        token: String,
        createProfileRequest: CreateProfileRequest
    ): JoinResponse {
        val accountType = createProfileRequest.accountType.toMultipartFile("accountType")
        val name = createProfileRequest.name.toMultipartFile("name")
        val bio = createProfileRequest.bio?.toMultipartFile("bio")
        val profileImage = createProfileRequest.profileImage?.fileToMultipartFile("profileImage")

        return berryBucketApi.crateProfile(token, accountType, name, bio, profileImage)
    }

    override suspend fun loginBerryBucket(
        token: String,
        loginRequest: LoginRequest
    ): CommonResponse {
        TODO("Not yet implemented")
    }

    override suspend fun refreshToken(token: String): RefreshTokenResponse {
        return berryBucketApi.refreshToken(token)
    }
}