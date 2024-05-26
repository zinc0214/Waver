package com.zinc.domain.usecases.category

import com.zinc.domain.repository.CategoryRepository
import javax.inject.Inject

class LoadCategoryList @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke() = categoryRepository.loadCategoryList()
}