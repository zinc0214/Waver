package com.zinc.domain.usecases.category

import com.zinc.domain.repository.CategoryRepository
import javax.inject.Inject

class SearchCategoryList @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(query: String) =
        categoryRepository.searchCategoryList(query)
}