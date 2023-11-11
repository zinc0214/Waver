package com.zinc.domain.usecases.search

import com.zinc.domain.repository.SearchRepository
import javax.inject.Inject

class LoadSearchResult @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(token: String, searchWord: String) =
        searchRepository.loadSearchResult(token, searchWord)
}