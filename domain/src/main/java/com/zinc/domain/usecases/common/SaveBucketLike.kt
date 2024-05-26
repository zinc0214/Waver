package com.zinc.domain.usecases.common

import com.zinc.domain.repository.CommonRepository
import javax.inject.Inject

class SaveBucketLike @Inject constructor(
    private val commonRepository: CommonRepository
) {
    suspend operator fun invoke(id: String) =
        commonRepository.saveBucketLike(id)
}