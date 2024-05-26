package com.zinc.domain.usecases.more

import com.zinc.domain.repository.MoreRepository
import javax.inject.Inject

class LoadProfileInfo @Inject constructor(
    private val moreRepository: MoreRepository
) {
    suspend operator fun invoke() =
        moreRepository.loadProfileInfo()
}
