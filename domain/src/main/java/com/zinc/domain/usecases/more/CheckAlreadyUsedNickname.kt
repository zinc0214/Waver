package com.zinc.domain.usecases.more

import com.zinc.domain.repository.MoreRepository
import javax.inject.Inject

class CheckAlreadyUsedNickname @Inject constructor(
    private val moreRepository: MoreRepository
) {
    suspend operator fun invoke(token: String, nickName: String) =
        moreRepository.checkAlreadyUsedNickName(token, nickName)
}
