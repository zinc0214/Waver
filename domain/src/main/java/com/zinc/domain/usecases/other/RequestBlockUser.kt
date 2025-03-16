package com.zinc.domain.usecases.other

import com.zinc.domain.repository.MyRepository
import javax.inject.Inject

class RequestBlockUser @Inject constructor(
    private val myRepository: MyRepository
) {
    suspend operator fun invoke(userId: String) =
        myRepository.requestUserBlock(userId)
}