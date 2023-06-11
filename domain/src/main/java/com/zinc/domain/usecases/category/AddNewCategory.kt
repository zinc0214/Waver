package com.zinc.domain.usecases.category

import com.zinc.domain.repository.CategoryRepository
import javax.inject.Inject

class AddNewCategory @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(token: String, categoryName: String) =
        categoryRepository.addNewCategory(token, categoryName)
}