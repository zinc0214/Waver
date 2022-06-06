package com.zinc.domain.usecases.search

import com.zinc.domain.repository.SearchRepository
import javax.inject.Inject

class LoadSearchRecommendCategoryItems @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke() =
        searchRepository.loadSearchRecommendCategoryItems()
}