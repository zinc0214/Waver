package com.zinc.domain.usecases.my

import com.zinc.domain.repository.MyRepository
import javax.inject.Inject

class RequestUnfollowUser @Inject constructor(
    private val myRepository: MyRepository
) {
    suspend operator fun invoke(token: String, userId: String) =
        myRepository.requestUnfollow(token, userId)
}