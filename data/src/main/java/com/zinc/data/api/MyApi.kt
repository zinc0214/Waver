package com.zinc.data.api

import com.zinc.data.models.MyProfileInfo
import retrofit2.http.GET

interface MyApi {

    @GET("/profile_info")
    suspend fun loadMyProfileInfo(): MyProfileInfo
}
