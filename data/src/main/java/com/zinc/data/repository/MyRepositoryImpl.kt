package com.zinc.data.repository

import com.zinc.data.api.MyApi
import com.zinc.data.models.MyProfileInfo
import javax.inject.Inject

class MyRepositoryImpl @Inject constructor(
    private val myApi: MyApi
) : MyRepository {
    override suspend fun loadMyProfileInfo(): MyProfileInfo {
        return myApi.loadMyProfileInfo()
    }
}