package com.zinc.domain.usecases.my

import com.zinc.domain.repository.MyRepository
import javax.inject.Inject

class RequestFollowUser @Inject constructor(
    private val myRepository: MyRepository
) {
    suspend operator fun invoke(token: String, userId: String) =
        myRepository.requestFollow(token, userId)
}