package com.zinc.domain.usecases.my

import com.zinc.domain.repositories.MyRepository
import javax.inject.Inject

class LoadProfileState @Inject constructor(
    private val myRepository: MyRepository
) {
    suspend operator fun invoke() = myRepository.loadMyState()
}