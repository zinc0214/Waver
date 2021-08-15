package com.zinc.data.repository

import com.zinc.data.models.MyProfileInfo

interface MyRepository {
    suspend fun loadMyProfileInfo(): MyProfileInfo
}