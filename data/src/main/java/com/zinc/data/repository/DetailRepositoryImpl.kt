package com.zinc.data.repository

import com.zinc.common.models.AddBucketCommentRequest
import com.zinc.common.models.BucketDetailResponse
import com.zinc.common.models.CommonResponse
import com.zinc.common.models.ProfileResponse
import com.zinc.data.api.WaverApi
import com.zinc.domain.models.RequestGoalCountUpdate
import com.zinc.domain.repository.DetailRepository
import javax.inject.Inject

internal class DetailRepositoryImpl @Inject constructor(
    private val waverApi: WaverApi
) : DetailRepository {

    override suspend fun loadBucketDetail(
        id: String,
        isMine: Boolean
    ): BucketDetailResponse {
        return if (isMine) waverApi.loadBucketDetail(id) else waverApi.loadOtherBucketDetail(
            id
        )
    }

    override suspend fun addBucketComment(
        request: AddBucketCommentRequest
    ): CommonResponse {
        return waverApi.addBucketComment(request)
    }

    override suspend fun requestGoalCountUpdate(
        id: String,
        goalCount: Int
    ): CommonResponse {
        return waverApi.requestGoalCountUpdate(id, RequestGoalCountUpdate(goalCount))
    }

    override suspend fun loadProfile(
        isMine: Boolean,
        writerId: String?
    ): ProfileResponse {
        return if (isMine) {
            waverApi.loadMyProfile()
        } else {
            waverApi.loadOtherProfileInfo(writerId.orEmpty())
        }
    }

    override suspend fun deleteBucketComment(id: String): CommonResponse {
        return waverApi.deleteComment(id)
    }

    override suspend fun hideBucketComment(id: String): CommonResponse {
        return waverApi.hideComment(id)
    }

    override suspend fun deleteMyBucket(id: String): CommonResponse {
        return waverApi.deleteMyBucket(id)
    }
}