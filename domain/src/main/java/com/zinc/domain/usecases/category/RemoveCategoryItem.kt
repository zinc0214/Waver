package com.zinc.domain.usecases.category

import com.zinc.domain.repository.CategoryRepository
import javax.inject.Inject

class RemoveCategoryItem @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(id: Int) =
        categoryRepository.removeCategory(id)
}