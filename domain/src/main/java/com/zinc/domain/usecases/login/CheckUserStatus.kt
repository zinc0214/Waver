package com.zinc.domain.usecases.login

import com.zinc.common.models.CheckUserStatusRequest
import com.zinc.domain.repository.LoginRepository
import javax.inject.Inject

// 프로필 생성
class CheckUserStatus @Inject constructor(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(checkUserStatusRequest: CheckUserStatusRequest) =
        loginRepository.checkUserStatus(checkUserStatusRequest)
}