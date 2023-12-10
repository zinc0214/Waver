package com.zinc.domain.usecases.login

import com.zinc.domain.repository.LoginRepository
import javax.inject.Inject

// 프로필 생성
class CheckEmail @Inject constructor(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(email: String) =
        loginRepository.checkEmail(email)
}