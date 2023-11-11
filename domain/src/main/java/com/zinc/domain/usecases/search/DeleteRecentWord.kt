package com.zinc.domain.usecases.search

import com.zinc.domain.repository.SearchRepository
import javax.inject.Inject

class DeleteRecentWord @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(token: String, keyWord: String) =
        searchRepository.deleteSearchRecentWord(token, keyWord)
}