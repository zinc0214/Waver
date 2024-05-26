package com.zinc.domain.usecases.write

import com.zinc.common.models.AddBucketListRequest
import com.zinc.domain.repository.WriteRepository
import javax.inject.Inject

class AddNewBucketList @Inject constructor(
    private val writeRepository: WriteRepository
) {
    suspend operator fun invoke(
        addBucketListRequest: AddBucketListRequest,
        isForUpdate: Boolean
    ) = writeRepository.addNewBucketList(addBucketListRequest, isForUpdate)
}