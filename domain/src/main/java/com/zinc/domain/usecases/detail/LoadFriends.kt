package com.zinc.domain.usecases.detail

import com.zinc.domain.repository.WriteRepository
import javax.inject.Inject

// 함께할 친구 목록
class LoadFriends @Inject constructor(
    private val writeRepository: WriteRepository
) {
    suspend operator fun invoke() = writeRepository.loadFriends()
}