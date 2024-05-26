package com.zinc.domain.usecases.more

import com.zinc.domain.models.UpdateProfileRequest
import com.zinc.domain.repository.MoreRepository
import javax.inject.Inject

class UpdateProfileInfo @Inject constructor(
    private val moreRepository: MoreRepository
) {
    suspend operator fun invoke(request: UpdateProfileRequest) =
        moreRepository.updateProfileInfo(request)
}
