package com.zinc.domain.usecases.more

import com.zinc.domain.repository.MoreRepository
import javax.inject.Inject

class RequestBlockUserRelease @Inject constructor(
    private val moreRepository: MoreRepository
) {
    suspend operator fun invoke(userId: Int) =
        moreRepository.requestBlockUserRelease(userId)
}
