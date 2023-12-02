package com.zinc.domain.usecases.detail

import com.zinc.domain.repository.MyRepository
import javax.inject.Inject

class LoadProfileInfo @Inject constructor(
    private val myRepository: MyRepository
) {
    suspend operator fun invoke(token: String, otherId: String, isMine: Boolean) =
        myRepository.loadMyHomeProfileInfo(token)
}
