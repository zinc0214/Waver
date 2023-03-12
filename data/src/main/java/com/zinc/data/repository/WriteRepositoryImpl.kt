package com.zinc.data.repository

import com.zinc.common.models.AddBucketListRequest
import com.zinc.common.models.AddBucketListResponse
import com.zinc.data.api.BerryBucketApi
import com.zinc.data.util.fileToMultipartFile
import com.zinc.data.util.toMultipartFile
import com.zinc.domain.repository.WriteRepository
import javax.inject.Inject

internal class WriteRepositoryImpl @Inject constructor(
    private val berryBucketApi: BerryBucketApi
) : WriteRepository {
    override suspend fun addNewBucketList(
        token: String,
        addBucketListRequest: AddBucketListRequest
    ): AddBucketListResponse {

        val bucketType = addBucketListRequest.bucketType.toMultipartFile("bucketType")
        val exposureStatus = addBucketListRequest.exposureStatus.toMultipartFile("exposureStatus")
        val title = addBucketListRequest.title.toMultipartFile("title")
        val content = addBucketListRequest.content.toMultipartFile("content")
        val memo = addBucketListRequest.memo?.toMultipartFile("memo")
        val tags = addBucketListRequest.tags?.toMultipartFile("tags")
        val friendUserIds = addBucketListRequest.friendUserIds?.map {
            it.toMultipartFile("friendUserIds")
        }
        val scrapYn = addBucketListRequest.scrapYn.toMultipartFile("scrapYn")
        val images = addBucketListRequest.images?.map {
            it.fileToMultipartFile("images")
        }
        val targetDate = addBucketListRequest.targetDate?.toMultipartFile("targetDate")
        val goalCount = addBucketListRequest.goalCount.toMultipartFile("goalCount")
        val categoryId = addBucketListRequest.categoryId.toMultipartFile("categoryId")

        return berryBucketApi.addNewBucketList(
            token,
            bucketType,
            exposureStatus,
            title,
            content,
            memo,
            tags,
            friendUserIds,
            scrapYn,
            images,
            targetDate,
            goalCount,
            categoryId
        )
    }
}