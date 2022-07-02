package com.zinc.domain.usecases.lgin

import com.zinc.common.models.JoinRequest
import com.zinc.domain.repository.LoginRepository
import javax.inject.Inject

// 회원가입
class JoinBerryBucket @Inject constructor(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(email: String) =
        loginRepository.joinBerryBucket(JoinRequest(email))
}