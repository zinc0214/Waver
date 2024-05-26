package com.zinc.domain.repository

import com.zinc.common.models.AddBucketListRequest
import com.zinc.common.models.CommonResponse
import com.zinc.common.models.LoadWriteSelectableFriendsResponse

interface WriteRepository {
    suspend fun addNewBucketList(
        addBucketListRequest: AddBucketListRequest,
        isForUpdate: Boolean
    ): CommonResponse

    suspend fun loadFriends(): LoadWriteSelectableFriendsResponse
}