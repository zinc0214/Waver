package com.zinc.domain.usecases.login

import com.zinc.common.models.JoinEmailCheck
import com.zinc.domain.repository.LoginRepository
import javax.inject.Inject

// 회원가입
class CreateUserToken @Inject constructor(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(email: String) =
        loginRepository.createUserToken(JoinEmailCheck(email))
}