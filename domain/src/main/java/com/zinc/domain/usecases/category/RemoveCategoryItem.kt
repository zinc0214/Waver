package com.zinc.domain.usecases.category

import com.zinc.domain.repository.CategoryRepository
import javax.inject.Inject

class RemoveCategoryItem @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(token: String, id: Int) =
        categoryRepository.removeCategory(token, id)
}