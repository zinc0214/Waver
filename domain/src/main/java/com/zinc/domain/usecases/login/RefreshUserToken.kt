package com.zinc.domain.usecases.login

import com.zinc.domain.repository.LoginRepository
import javax.inject.Inject

// 회원가입
class RefreshUserToken @Inject constructor(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke() =
        loginRepository.refreshToken()
}