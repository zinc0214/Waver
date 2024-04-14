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
        token: String,
        id: String,
        isMine: Boolean
    ): BucketDetailResponse {
        return if (isMine) berryBucketApi.loadBucketDetail(
            token, id
        ) else berryBucketApi.loadOtherBucketDetail(token, id)
    }

    override suspend fun addBucketComment(
        token: String,
        request: AddBucketCommentRequest
    ): CommonResponse {
        return berryBucketApi.addBucketComment(token, request)
    }

    override suspend fun requestGoalCountUpdate(
        token: String,
        id: String,
        goalCount: Int
    ): CommonResponse {
        return berryBucketApi.requestGoalCountUpdate(token, id, RequestGoalCountUpdate(goalCount))
    }

    override suspend fun loadProfile(
        token: String,
        isMine: Boolean,
        writerId: String?
    ): ProfileResponse {
        return if (isMine) {
            berryBucketApi.loadMyProfile(token)
        } else {
            berryBucketApi.loadOtherProfileInfo(token, writerId.orEmpty())
        }
    }

    override suspend fun deleteBucketComment(token: String, id: String): CommonResponse {
        return berryBucketApi.deleteComment(token, id)
    }
}