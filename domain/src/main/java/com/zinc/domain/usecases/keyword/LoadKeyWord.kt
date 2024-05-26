package com.zinc.domain.usecases.keyword

import com.zinc.domain.repository.KeywordRepository
import javax.inject.Inject

class LoadKeyWord @Inject constructor(
    private val keywordRepository: KeywordRepository
) {
    suspend operator fun invoke() = keywordRepository.loadKeyword()
}