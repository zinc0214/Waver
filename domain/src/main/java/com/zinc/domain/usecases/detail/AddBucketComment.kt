package com.zinc.domain.usecases.detail

import com.zinc.common.models.AddBucketCommentRequest
import com.zinc.domain.repository.DetailRepository
import javax.inject.Inject

class AddBucketComment @Inject constructor(
    private val detailRepository: DetailRepository
) {
    suspend operator fun invoke(token: String, request: AddBucketCommentRequest) =
        detailRepository.addBucketComment(token, request)
}
