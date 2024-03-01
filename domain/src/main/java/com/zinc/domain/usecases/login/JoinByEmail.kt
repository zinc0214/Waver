package com.zinc.domain.usecases.login

import com.zinc.common.models.JoinEmailCheck
import com.zinc.domain.repository.LoginRepository
import javax.inject.Inject

// 회원가입 (이메일전송, 토큰받기)
class JoinByEmail @Inject constructor(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(email: String) =
        loginRepository.joinByEmail(JoinEmailCheck(email))
}