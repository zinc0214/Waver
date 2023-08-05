package com.zinc.data.api

import com.zinc.common.models.AddBucketListResponse
import com.zinc.common.models.AlarmList
import com.zinc.common.models.AllBucketListResponse
import com.zinc.common.models.AllBucketListSortType
import com.zinc.common.models.BucketDetailResponse
import com.zinc.common.models.BucketStatus
import com.zinc.common.models.CommonResponse
import com.zinc.common.models.FeedInfo
import com.zinc.common.models.FeedKeyWord
import com.zinc.common.models.FollowResponse
import com.zinc.common.models.JoinRequest
import com.zinc.common.models.JoinResponse
import com.zinc.common.models.KeywordResponse
import com.zinc.common.models.MyProfileResponse
import com.zinc.common.models.MyState
import com.zinc.common.models.RecommendList
import com.zinc.common.models.ReportItems
import com.zinc.data.model.AddNewCategoryRequest
import com.zinc.data.model.EditCategoryNameRequest
import com.zinc.data.model.LoadCategoryResponse
import com.zinc.data.model.ReorderedCategoryRequest
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface BerryBucketApi {

    // 회원가입
    @POST("/user/join")
    suspend fun joinBerryBucket(@Body joinRequest: JoinRequest): JoinResponse

    @GET("/my")
    suspend fun loadMyProfileInfo(@Header("Authorization") token: String): MyProfileResponse

    @GET("/bucketList")
    suspend fun loadAllBucketList(
        @Header("Authorization") token: String,
        @Query("dDayBucketOnly") dDayBucketOnly: String,
        @Query("isPassed") isPassed: String?,
        @Query("status") status: BucketStatus?,
        @Query("sort") sort: AllBucketListSortType
    ): AllBucketListResponse

    @GET("/category")
    suspend fun loadCategoryList(@Header("Authorization") token: String): LoadCategoryResponse

    @POST("/category")
    suspend fun addNewCategory(
        @Header("Authorization") token: String,
        @Body request: AddNewCategoryRequest
    ): CommonResponse

    @PATCH("/category")
    suspend fun editCategoryName(
        @Header("Authorization") token: String,
        @Body request: EditCategoryNameRequest
    ): CommonResponse

    @HTTP(method = "DELETE", path = "/category/{id}")
    suspend fun removeCategoryItem(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): CommonResponse

    @PATCH("/category/seq")
    suspend fun reorderedCategory(
        @Header("Authorization") token: String,
        @Body request: ReorderedCategoryRequest
    ): CommonResponse

    @GET("/bucketList")
    suspend fun loadMyState(): MyState

    @GET("/bucketList/{id}")
    suspend fun loadBucketDetail(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): BucketDetailResponse

    @GET("/feed/keyWord")
    suspend fun loadFeedKeyword(): List<FeedKeyWord>

    @GET("/feed")
    suspend fun loadFeedItems(): List<FeedInfo>

    @GET("/search/recommendList")
    suspend fun loadRecommendList(): RecommendList

    @GET("/report/comment")
    suspend fun loadReportItems(): ReportItems

    // 버킷리스트 등록
    @POST("/bucketlist")
    @Multipart
    suspend fun addNewBucketList(
        @Header("Authorization") token: String,
        @Part bucketType: MultipartBody.Part,
        @Part exposureStatus: MultipartBody.Part, // 공개여부
        @Part title: MultipartBody.Part,
        @Part memo: MultipartBody.Part?, // 메모
        @Part keywordIds: MultipartBody.Part?, // 키워드 (태그) 키워드 목록(최대 5) - ","로 구분
        @Part friendUserIds: List<MultipartBody.Part>?, // 함께할 친구 ID - 함께하기인 경우 최소 1명 필수(최대 5)
        @Part scrapYn: MultipartBody.Part, // 스크랩 여부
        @Part images: List<MultipartBody.Part>?, // 이미지 목록(최대 3)
        @Part targetDate: MultipartBody.Part?, // 목표완료일(yyyy-MM-dd)
        @Part goalCount: MultipartBody.Part, //  목표 횟수
        @Part categoryId: MultipartBody.Part // 카테고리 ID
    ): AddBucketListResponse

    @GET("/alarm")
    suspend fun loadAlarmList(@Header("Authorization") token: String): AlarmList

    @GET("/explore/keywords")
    suspend fun loadKeywords(@Header("Authorization") token: String): KeywordResponse

    @GET("/follow")
    suspend fun loadFollowList(@Header("Authorization") token: String): FollowResponse

    @POST("/follow/unfollow")
    suspend fun requestUnfollow(
        @Header("Authorization") token: String,
        @Query("followUserId") followUserId: String
    ): CommonResponse

    @POST("/follow")
    suspend fun requestFollow(
        @Header("Authorization") token: String,
        @Query("followUserId") followUserId: String
    ): CommonResponse
}
