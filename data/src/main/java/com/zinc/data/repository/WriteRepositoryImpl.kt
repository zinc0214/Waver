package com.zinc.data.repository

import android.util.Log
import com.zinc.common.models.AddBucketListRequest
import com.zinc.common.models.CommonResponse
import com.zinc.common.models.LoadWriteSelectableFriendsResponse
import com.zinc.data.api.WaverApi
import com.zinc.data.util.fileToMultipartFile
import com.zinc.data.util.toMultipartFile
import com.zinc.domain.repository.WriteRepository
import javax.inject.Inject

internal class WriteRepositoryImpl @Inject constructor(
    private val waverApi: WaverApi
) : WriteRepository {
    override suspend fun addNewBucketList(
        addBucketListRequest: AddBucketListRequest,
        isForUpdate: Boolean
    ): CommonResponse {
        Log.e("ayhan", "addBucketListRequest : $addBucketListRequest\n isForUpdate : $isForUpdate")

        val bucketType = addBucketListRequest.bucketType.toMultipartFile("bucketType")
        val exposureStatus = addBucketListRequest.exposureStatus.toMultipartFile("exposureStatus")
        val title = addBucketListRequest.title.toMultipartFile("title")
        val memo = addBucketListRequest.memo?.toMultipartFile("memo")
        val keywordIds = addBucketListRequest.keywords?.toMultipartFile("keywords")
        val friendUserIds = addBucketListRequest.friendUserIds?.toMultipartFile("friendUserIds")
        val scrapYn = addBucketListRequest.scrapYn.toMultipartFile("scrapYn")
        val images = addBucketListRequest.images?.map {
            it.fileToMultipartFile("images")
        }
        val targetDate = addBucketListRequest.targetDate?.toMultipartFile("targetDate")
        val goalCount = addBucketListRequest.goalCount.toMultipartFile("goalCount")
        val categoryId = addBucketListRequest.categoryId.toMultipartFile("categoryId")

        if (isForUpdate) {
            return waverApi.updateBucketList(
                bucketType,
                exposureStatus,
                title,
                memo,
                keywordIds,
                friendUserIds,
                scrapYn,
                images,
                targetDate,
                goalCount,
                categoryId,
                addBucketListRequest.bucketId.orEmpty()
            )
        } else {
            return waverApi.addNewBucketList(
                bucketType,
                exposureStatus,
                title,
                memo,
                keywordIds,
                friendUserIds,
                scrapYn,
                images,
                targetDate,
                goalCount,
                categoryId
            )
        }
    }

    override suspend fun loadFriends(): LoadWriteSelectableFriendsResponse {
        return waverApi.loadWriteSelectableFriends()
    }
}