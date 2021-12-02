package com.zinc.domain.usecases.detail

import com.zinc.data.repository.DetailRepository
import javax.inject.Inject

class LoadBucketDetail @Inject constructor(
    private val detailRepository: DetailRepository
) {
    suspend operator fun invoke(id: String) = detailRepository.loadBucketDetail(id)
}