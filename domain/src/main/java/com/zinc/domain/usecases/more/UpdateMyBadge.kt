package com.zinc.domain.usecases.more

import com.zinc.domain.repository.MoreRepository
import javax.inject.Inject

class UpdateMyBadge @Inject constructor(
    private val moreRepository: MoreRepository
) {
    suspend operator fun invoke(badgeId: Int) =
        moreRepository.updateMyBadge(badgeId)
}