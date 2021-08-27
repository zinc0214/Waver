package com.zinc.data.api

import com.zinc.data.models.AllBucketList
import com.zinc.data.models.DdayBucketList
import com.zinc.data.models.MyProfileInfo
import com.zinc.data.models.MyState
import retrofit2.http.GET

interface MyApi {

    @GET("/profile_info")
    suspend fun loadMyProfileInfo(): MyProfileInfo

    @GET("/bucketList")
    suspend fun loadMyDdayBucketList(): DdayBucketList

    @GET("/bucketList")
    suspend fun loadAllBucketList(): AllBucketList

    @GET("/bucketList")
    suspend fun loadMyState(): MyState


}
