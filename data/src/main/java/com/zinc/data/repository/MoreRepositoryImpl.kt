package com.zinc.data.repository

import com.zinc.common.models.BlockedUserResponse
import com.zinc.common.models.CommonResponse
import com.zinc.common.models.LoadMyWaveInfoResponse
import com.zinc.common.models.ProfileResponse
import com.zinc.data.api.WaverApi
import com.zinc.data.util.fileToMultipartFile
import com.zinc.data.util.toMultipartFile
import com.zinc.domain.models.UpdateProfileRequest
import com.zinc.domain.repository.MoreRepository
import javax.inject.Inject

internal class MoreRepositoryImpl @Inject constructor(
    private val waverApi: WaverApi
) : MoreRepository {
    override suspend fun loadProfileInfo(): ProfileResponse {
        return waverApi.loadMyProfile()
    }

    override suspend fun updateProfileInfo(
        request: UpdateProfileRequest
    ): CommonResponse {

        val name = request.name.toMultipartFile("name")
        val bio = request.bio.toMultipartFile("bio")
        val profileImage = request.image?.fileToMultipartFile("profileImage")

        return waverApi.updateMyProfile(
            name = name,
            bio = bio,
            profileImage = profileImage
        )
    }

    override suspend fun checkAlreadyUsedNickName(name: String): CommonResponse {
        return waverApi.checkAlreadyUsedNickname(name)
    }

    override suspend fun loadBlockedUsers(): BlockedUserResponse {
        return waverApi.loadBlockedUsers()
    }

    override suspend fun loadMyWaeInfo(): LoadMyWaveInfoResponse {
        return waverApi.loadMyWaveInfo()
    }
}