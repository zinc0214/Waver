package com.zinc.data.repository

import com.zinc.common.models.CommonResponse
import com.zinc.common.models.CreateProfileRequest
import com.zinc.common.models.JoinRequest
import com.zinc.common.models.JoinResponse
import com.zinc.common.models.LoginRequest
import com.zinc.data.api.BerryBucketApi
import com.zinc.data.util.fileToMultipartFile
import com.zinc.data.util.toMultipartFile
import com.zinc.domain.repository.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val berryBucketApi: BerryBucketApi
) : LoginRepository {
    override suspend fun joinBerryBucket(joinRequest: JoinRequest): JoinResponse {
        return berryBucketApi.joinBerryBucket(joinRequest)
    }

    override suspend fun createProfile(
        token: String,
        createProfileRequest: CreateProfileRequest
    ): CommonResponse {
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
}