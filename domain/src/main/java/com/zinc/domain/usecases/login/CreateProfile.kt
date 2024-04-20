package com.zinc.domain.usecases.login

import com.zinc.common.models.CreateProfileRequest
import com.zinc.domain.repository.LoginRepository
import javax.inject.Inject

// 프로필 생성
class CreateProfile @Inject constructor(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(profileRequest: CreateProfileRequest) =
        loginRepository.createProfile(profileRequest)
}