package com.zinc.domain.repository

import com.zinc.common.models.KeywordResponse

interface KeywordRepository {
    suspend fun loadKeyword(
        token: String
    ): KeywordResponse
}