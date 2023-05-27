package com.zinc.domain.repository

import com.zinc.common.models.RecommendList

interface SearchRepository {
    suspend fun loadRecommendList(): RecommendList
}