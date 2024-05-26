package com.zinc.data.repository

import com.zinc.common.models.AddBucketCommentRequest
import com.zinc.common.models.BucketDetailResponse
import com.zinc.common.models.CommonResponse
import com.zinc.common.models.ProfileResponse
import com.zinc.data.api.BerryBucketApi
import com.zinc.domain.models.RequestGoalCountUpdate
import com.zinc.domain.repository.DetailRepository
import javax.inject.Inject

internal class DetailRepositoryImpl @Inject constructor(
    private val berryBucketApi: BerryBucketApi
) : DetailRepository {

    override suspend fun loadBucketDetail(
        id: String,
        isMine: Boolean
    ): BucketDetailResponse {
        return if (isMine) berryBucketApi.loadBucketDetail(id) else berryBucketApi.loadOtherBucketDetail(
            id
        )
    }

    override suspend fun addBucketComment(
        request: AddBucketCommentRequest
    ): CommonResponse {
        return berryBucketApi.addBucketComment(request)
    }

    override suspend fun requestGoalCountUpdate(
        id: String,
        goalCount: Int
    ): CommonResponse {
        return berryBucketApi.requestGoalCountUpdate(id, RequestGoalCountUpdate(goalCount))
    }

    override suspend fun loadProfile(
        isMine: Boolean,
        writerId: String?
    ): ProfileResponse {
        return if (isMine) {
            berryBucketApi.loadMyProfile()
        } else {
            berryBucketApi.loadOtherProfileInfo(writerId.orEmpty())
        }
    }

    override suspend fun deleteBucketComment(id: String): CommonResponse {
        return berryBucketApi.deleteComment(id)
    }
}