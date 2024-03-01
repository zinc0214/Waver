package com.zinc.domain.usecases.login

import com.zinc.common.models.LoadTokenByEmailRequest
import com.zinc.domain.repository.LoginRepository
import javax.inject.Inject

// 로그인
class LoginByEmail @Inject constructor(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(email: String) =
        loginRepository.requestLogin(LoadTokenByEmailRequest(email))
}