package com.zinc.data.api

import com.zinc.common.models.AddBucketListRequest
import com.zinc.common.models.AddBucketListResponse
import com.zinc.common.models.AllBucketList
import com.zinc.common.models.CategoryInfo
import com.zinc.common.models.DdayBucketList
import com.zinc.common.models.DetailInfo
import com.zinc.common.models.FeedInfo
import com.zinc.common.models.FeedKeyWord
import com.zinc.common.models.JoinRequest
import com.zinc.common.models.JoinResponse
import com.zinc.common.models.MyProfileInfo
import com.zinc.common.models.MyState
import com.zinc.common.models.RecommendList
import com.zinc.common.models.ReportItems
import com.zinc.common.models.SearchRecommendCategory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Part

interface BerryBucketApi {

    // 회원가입
    @POST("/user/join")
    suspend fun joinBerryBucket(@Body joinRequest: JoinRequest): JoinResponse

    @GET("/my")
    suspend fun loadMyProfileInfo(@Header("Authorization") token: String): MyProfileInfo

    @GET("/bucketList")
    suspend fun loadMyDdayBucketList(): DdayBucketList

    @GET("/bucketList")
    suspend fun loadAllBucketList(): AllBucketList

    @GET("/category")
    suspend fun loadCategoryList(@Header("Authorization") token: String): List<CategoryInfo>

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

    @GET("/report/comment")
    suspend fun loadReportItems(): ReportItems

    // 버킷리스트 등록
    @POST("/bucketlist")
    suspend fun addNewBucketList(
        @Header("Authorization") token: String,
        @Part request: AddBucketListRequest
    ): AddBucketListResponse
}
