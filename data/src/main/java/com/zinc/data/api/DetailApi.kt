package com.zinc.data.api

import com.zinc.data.models.DetailInfo
import retrofit2.http.GET

interface DetailApi {

    @GET("/detail")
    suspend fun loadBucketDetail(id: String): DetailInfo

}
