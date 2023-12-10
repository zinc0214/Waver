package com.zinc.data.api

import com.zinc.common.models.AddBucketCommentRequest
import com.zinc.common.models.AddNewCategoryRequest
import com.zinc.common.models.AlarmList
import com.zinc.common.models.AllBucketListResponse
import com.zinc.common.models.AllBucketListSortType
import com.zinc.common.models.BucketDetailResponse
import com.zinc.common.models.BucketStatus
import com.zinc.common.models.CommonResponse
import com.zinc.common.models.EditCategoryNameRequest
import com.zinc.common.models.FollowResponse
import com.zinc.common.models.HomeProfileResponse
import com.zinc.common.models.JoinEmailCheck
import com.zinc.common.models.JoinResponse
import com.zinc.common.models.KeywordResponse
import com.zinc.common.models.LoadCategoryResponse
import com.zinc.common.models.MyProfileResponse
import com.zinc.common.models.MyState
import com.zinc.common.models.RefreshTokenResponse
import com.zinc.common.models.ReorderedCategoryRequest
import com.zinc.common.models.ReportItems
import com.zinc.common.models.YesOrNo
import com.zinc.domain.models.CheckEmailIsLogined
import com.zinc.domain.models.FeedListResponse
import com.zinc.domain.models.RequestGoalCountUpdate
import com.zinc.domain.models.SavedKeywordItemsRequest
import com.zinc.domain.models.SearchRecommendResponse
import com.zinc.domain.models.SearchResultResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
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

    // 이메일 확인
    @POST("/user/check")
    suspend fun checkEmailIsLogined(@Body checkEmailIsLogined: CheckEmailIsLogined): CommonResponse

    // 회원가입
    @POST("/user/join")
    suspend fun joinBerryBucket(@Body joinEmailCheck: JoinEmailCheck): JoinResponse

    // 토큰재발급
    @GET("/token/refresh")
    suspend fun refreshToken(@Header("Authorization") token: String): RefreshTokenResponse

    // 프로필생성
    @POST("/user/profile")
    @Multipart
    suspend fun crateProfile(
        @Header("Authorization") token: String,
        @Part accountType: MultipartBody.Part, // 기기 유형
        @Part name: MultipartBody.Part,
        @Part bio: MultipartBody.Part?,
        @Part profileImage: MultipartBody.Part?
    ): JoinResponse

    @GET("/my")
    suspend fun loadMyProfileInfo(@Header("Authorization") token: String): HomeProfileResponse

    @GET("/bucketList")
    suspend fun loadAllBucketList(
        @Header("Authorization") token: String,
        @Query("dDayBucketOnly") dDayBucketOnly: String? = null,
        @Query("isPassed") isPassed: String? = null,
        @Query("status") status: BucketStatus? = null,
        @Query("sort") sort: AllBucketListSortType? = AllBucketListSortType.ORDERED,
        @Query("categoryId") categoryId: String? = null,
    ): AllBucketListResponse

    @GET("/category")
    suspend fun loadCategoryList(@Header("Authorization") token: String): LoadCategoryResponse

    @GET("/category")
    suspend fun searchCategoryList(
        @Header("Authorization") token: String,
        @Query("query") query: String
    ): LoadCategoryResponse

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

    @POST("/feeds/keyword")
    suspend fun savedFeedKeywords(
        @Header("Authorization") token: String,
        @Body request: SavedKeywordItemsRequest
    ): CommonResponse

    @GET("/feeds")
    suspend fun loadFeedItems(@Header("Authorization") token: String): FeedListResponse

//    @GET("/search/recommendList")
//    suspend fun loadRecommendList(): RecommendList

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
    ): CommonResponse

    // 버킷리스트 등록
    @POST("/bucketlist/{id}")
    @Multipart
    suspend fun updateBucketList(
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
        @Part categoryId: MultipartBody.Part, // 카테고리 ID
        @Path("id") id: String
    ): CommonResponse

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

    @GET("/user/profile")
    suspend fun loadMyProfile(
        @Header("Authorization") token: String,
    ): MyProfileResponse

    @PATCH("/user/profile")
    @Multipart
    suspend fun updateMyProfile(
        @Header("Authorization") token: String,
        @Part name: MultipartBody.Part,
        @Part bio: MultipartBody.Part,
        @Part profileImage: MultipartBody.Part?
    ): CommonResponse

    @GET("/user/profile/name")
    suspend fun checkAlreadyUsedNickname(
        @Header("Authorization") token: String,
        @Query("name") name: String
    ): CommonResponse

    @GET("/bucketList")
    suspend fun searchAllBucketList(
        @Header("Authorization") token: String,
        @Query("query") query: String
    ): AllBucketListResponse

    @GET("/bucketList")
    suspend fun searchDdayBucketList(
        @Header("Authorization") token: String,
        @Query("dDayBucketOnly") dDayBucketOnly: String? = YesOrNo.Y.name
    ): AllBucketListResponse

    @GET("/bucketList/{id}/achieve")
    suspend fun achieveBucket(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): CommonResponse

    @GET("/explore")
    suspend fun loadSearchResult(
        @Header("Authorization") token: String,
        @Query("query") query: String
    ): SearchResultResponse

    @GET("/explore/searchOptions")
    suspend fun loadSearchRecommend(
        @Header("Authorization") token: String,
    ): SearchRecommendResponse

    @DELETE("/explore/recentSearch/{keyword}")
    suspend fun deleteSearchRecentWord(
        @Header("Authorization") token: String,
        @Path("keyword") keyword: String
    ): CommonResponse

    @POST("/feeds/{id}/like")
    suspend fun saveBucketLike(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): CommonResponse

    @GET("/feeds/{id}")
    suspend fun loadOtherBucketDetail(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): BucketDetailResponse

    @POST("/comment")
    suspend fun addBucketComment(
        @Header("Authorization") token: String,
        @Body request: AddBucketCommentRequest
    ): CommonResponse

    @PATCH("/bucketlist/{id}/goalCount")
    suspend fun requestGoalCountUpdate(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body goalCount: RequestGoalCountUpdate
    ): CommonResponse


}
