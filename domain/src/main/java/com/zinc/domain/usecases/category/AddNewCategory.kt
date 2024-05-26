package com.zinc.domain.usecases.category

import com.zinc.domain.repository.CategoryRepository
import javax.inject.Inject

class AddNewCategory @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(categoryName: String) =
        categoryRepository.addNewCategory(categoryName)
}