package com.zinc.data.repository

import com.zinc.common.models.KeywordResponse
import com.zinc.data.api.BerryBucketApi
import com.zinc.domain.repository.KeywordRepository
import javax.inject.Inject

internal class KeywordRepositoryImpl @Inject constructor(
    private val berryBucketApi: BerryBucketApi
) : KeywordRepository {
    override suspend fun loadKeyword(): KeywordResponse {
        return berryBucketApi.loadKeywords()
    }
}