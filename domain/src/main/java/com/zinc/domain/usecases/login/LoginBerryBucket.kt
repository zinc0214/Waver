package com.zinc.domain.usecases.login

import com.zinc.common.models.LoginRequest
import com.zinc.domain.repository.LoginRepository
import javax.inject.Inject

// 로그인
class LoginBerryBucket @Inject constructor(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(token: String, loginRequest: LoginRequest) =
        loginRepository.loginBerryBucket(token, loginRequest)
}