package com.zinc.domain.usecases.category

import com.zinc.domain.repository.CategoryRepository
import javax.inject.Inject

class ReorderCategory @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(ids: List<String>) =
        categoryRepository.reorderCategory(ids)
}