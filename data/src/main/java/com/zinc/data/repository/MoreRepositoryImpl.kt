package com.zinc.data.repository

import com.zinc.common.models.CommonResponse
import com.zinc.common.models.ProfileResponse
import com.zinc.data.api.BerryBucketApi
import com.zinc.data.util.fileToMultipartFile
import com.zinc.data.util.toMultipartFile
import com.zinc.domain.models.UpdateProfileRequest
import com.zinc.domain.repository.MoreRepository
import javax.inject.Inject

internal class MoreRepositoryImpl @Inject constructor(
    private val berryBucketApi: BerryBucketApi
) : MoreRepository {
    override suspend fun loadProfileInfo(token: String): ProfileResponse {
        return berryBucketApi.loadMyProfile(token)
    }

    override suspend fun updateProfileInfo(
        token: String,
        request: UpdateProfileRequest
    ): CommonResponse {

        val name = request.name.toMultipartFile("name")
        val bio = request.bio.toMultipartFile("bio")
        val profileImage = request.image?.fileToMultipartFile("profileImage")

        return berryBucketApi.updateMyProfile(
            token,
            name = name,
            bio = bio,
            profileImage = profileImage
        )
    }

    override suspend fun checkAlreadyUsedNickName(name: String): CommonResponse {
        return berryBucketApi.checkAlreadyUsedNickname(name)
    }
}