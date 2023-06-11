package com.zinc.domain.usecases.category

import com.zinc.domain.repository.CategoryRepository
import javax.inject.Inject

class EditCategoryName @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(token: String, id: Int, categoryName: String) =
        categoryRepository.editCategoryName(token, id, categoryName)
}