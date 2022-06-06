package com.zinc.data.api

import com.zinc.common.models.*
import retrofit2.http.GET

interface BerryBucketApi {

    @GET("/profile_info")
    suspend fun loadMyProfileInfo(): MyProfileInfo

    @GET("/bucketList")
    suspend fun loadMyDdayBucketList(): DdayBucketList

    @GET("/bucketList")
    suspend fun loadAllBucketList(): AllBucketList

    @GET("/bucketList")
    suspend fun loadCategoryList(): List<Category>

    @GET("/bucketList")
    suspend fun loadMyState(): MyState

    @GET("/detail")
    suspend fun loadBucketDetail(id: String): DetailInfo

    @GET("/feed/keyWord")
    suspend fun loadFeedKeyword(): List<FeedKeyWord>

    @GET("/feed")
    suspend fun loadFeedItems(): List<FeedInfo>

    @GET("/search/recommendCategory")
    suspend fun loadSearchRecommendCategoryItems(): List<SearchRecommendCategory>

    @GET("/search/recommendList")
    suspend fun loadRecommendList(): RecommendList
}
