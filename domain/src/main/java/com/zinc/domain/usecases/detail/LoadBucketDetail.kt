package com.zinc.domain.usecases.detail

import com.zinc.common.models.AddBucketCommentRequest
import com.zinc.domain.repository.DetailRepository
import javax.inject.Inject

class LoadBucketDetail @Inject constructor(
    private val detailRepository: DetailRepository
) {
    suspend operator fun invoke(id: String, isMine: Boolean) =
        detailRepository.loadBucketDetail(id, isMine)

    suspend operator fun invoke(request: AddBucketCommentRequest) =
        detailRepository.addBucketComment(request)
}