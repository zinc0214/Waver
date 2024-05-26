package com.zinc.domain.usecases.my

import com.zinc.domain.repository.MyRepository
import javax.inject.Inject

class SearchAllBucketList @Inject constructor(
    private val myRepository: MyRepository
) {
    suspend operator fun invoke(
        query: String
    ) = myRepository.searchAllBucketList(query)
}