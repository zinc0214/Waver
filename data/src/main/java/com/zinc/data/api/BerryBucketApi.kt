package com.zinc.data.api

import com.zinc.data.models.*
import retrofit2.http.GET

interface BerryBucketApi {

    @GET("/profile_info")
    suspend fun loadMyProfileInfo(): MyProfileInfo

    @GET("/bucketList")
    suspend fun loadMyDdayBucketList(): DdayBucketList

    @GET("/bucketList")
    suspend fun loadAllBucketList(): AllBucketList

    @GET("/bucketList")
    suspend fun loadMyState(): MyState

    @GET("/detail")
    suspend fun loadBucketDetail(id: String): DetailInfo

}
