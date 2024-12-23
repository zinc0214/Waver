package com.zinc.domain.usecases.common

import com.zinc.domain.repository.CommonRepository
import javax.inject.Inject

class CopyOtherBucket @Inject constructor(
    private val commonRepository: CommonRepository
) {
    suspend operator fun invoke(id: String) =
        commonRepository.copyOtherBucket(id)
}