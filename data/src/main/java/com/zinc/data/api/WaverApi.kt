package com.zinc.data.api

import com.zinc.common.models.AddBucketCommentRequest
import com.zinc.common.models.AddNewCategoryRequest
import com.zinc.common.models.AlarmList
import com.zinc.common.models.AllBucketListResponse
import com.zinc.common.models.AllBucketListSortType
import com.zinc.common.models.BlockedUserResponse
import com.zinc.common.models.BucketDetailResponse
import com.zinc.common.models.BucketStatus
import com.zinc.common.models.CommonResponse
import com.zinc.common.models.EditCategoryNameRequest
import com.zinc.common.models.FollowResponse
import com.zinc.common.models.HomeProfileResponse
import com.zinc.common.models.JoinResponse
import com.zinc.common.models.KeywordResponse
import com.zinc.common.models.LoadCategoryResponse
import com.zinc.common.models.LoadTokenByEmailRequest
import com.zinc.common.models.LoadTokenByEmailResponse
import com.zinc.common.models.LoadWriteSelectableFriendsResponse
import com.zinc.common.models.ProfileResponse
import com.zinc.common.models.RefreshTokenResponse
import com.zinc.common.models.ReorderedCategoryRequest
import com.zinc.common.models.YesOrNo
import com.zinc.domain.models.FeedListResponse
import com.zinc.domain.models.OtherBucketListResponse
import com.zinc.domain.models.OtherFollowDataResponse
import com.zinc.domain.models.RequestGoalCountUpdate
import com.zinc.domain.models.SavedKeywordItemsRequest
import com.zinc.domain.models.SearchRecommendResponse
import com.zinc.domain.models.SearchResultResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface WaverApi {

    // 토큰재발급
    @GET("/token/refresh")
    suspend fun refreshToken(): RefreshTokenResponse

    // 프로필생성
    @POST("/user/join")
    @Multipart
    suspend fun crateProfile(
        @Part accountType: MultipartBody.Part, // 기기 유형
        @Part email: MultipartBody.Part,
        @Part name: MultipartBody.Part,
        @Part bio: MultipartBody.Part?,
        @Part profileImage: MultipartBody.Part?
    ): JoinResponse

    // 로그인
    @POST("/login")
    suspend fun requestLogin(@Body loadTokenByEmail: LoadTokenByEmailRequest): LoadTokenByEmailResponse

    // 홈 > 프로필 조회
    @GET("/my")
    suspend fun loadMyProfileInfo(): HomeProfileResponse

    // 전체버킷리스트 조회
    @GET("/bucketList")
    suspend fun loadAllBucketList(
        @Query("dDayBucketOnly") dDayBucketOnly: String? = null,
        @Query("isPassed") isPassed: String? = null,
        @Query("status") status: BucketStatus? = null,
        @Query("sort") sort: AllBucketListSortType? = AllBucketListSortType.ORDERED,
        @Query("categoryId") categoryId: String? = null,
    ): AllBucketListResponse

    // 카테고리 전체 로드
    @GET("/category")
    suspend fun loadCategoryList(): LoadCategoryResponse

    // 카테고리 별 로드
    @GET("/category")
    suspend fun searchCategoryList(
        @Query("query") query: String
    ): LoadCategoryResponse

    // 카테고리 추가
    @POST("/category")
    suspend fun addNewCategory(
        @Body request: AddNewCategoryRequest
    ): CommonResponse

    // 카테고리 수정
    @PATCH("/category")
    suspend fun editCategoryName(
        @Body request: EditCategoryNameRequest
    ): CommonResponse

    // 카테고리 삭제
    @HTTP(method = "DELETE", path = "/category/{id}")
    suspend fun removeCategoryItem(
        @Path("id") id: Int
    ): CommonResponse

    // 카테고리 순서 변경
    @PATCH("/category/seq")
    suspend fun reorderedCategory(
        @Body request: ReorderedCategoryRequest
    ): CommonResponse

    // 버킷리스트 상세
    @GET("/bucketList/{id}")
    suspend fun loadBucketDetail(
        @Path("id") id: String
    ): BucketDetailResponse

    // 피드 > 키워드 선택 > 저장
    @POST("/feeds/keyword")
    suspend fun savedFeedKeywords(
        @Body request: SavedKeywordItemsRequest
    ): CommonResponse

    // 피드 조회
    @GET("/feeds")
    suspend fun loadFeedItems(): FeedListResponse

    // 버킷리스트 등록
    @POST("/bucketlist")
    @Multipart
    suspend fun addNewBucketList(
        @Part bucketType: MultipartBody.Part,
        @Part exposureStatus: MultipartBody.Part, // 공개여부
        @Part title: MultipartBody.Part,
        @Part memo: MultipartBody.Part?, // 메모
        @Part keywordIds: MultipartBody.Part?, // 키워드 (태그) 키워드 목록(최대 5) - ","로 구분
        @Part friendUserIds: MultipartBody.Part?, // 함께할 친구 ID - 함께하기인 경우 최소 1명 필수(최대 5)
        @Part scrapYn: MultipartBody.Part, // 스크랩 여부
        @Part images: List<MultipartBody.Part>?, // 이미지 목록(최대 3)
        @Part targetDate: MultipartBody.Part?, // 목표완료일(yyyy-MM-dd)
        @Part goalCount: MultipartBody.Part, //  목표 횟수
        @Part categoryId: MultipartBody.Part // 카테고리 ID
    ): CommonResponse

    // 버킷리스트 수정
    @POST("/bucketlist/{id}")
    @Multipart
    suspend fun updateBucketList(
        @Part bucketType: MultipartBody.Part,
        @Part exposureStatus: MultipartBody.Part, // 공개여부
        @Part title: MultipartBody.Part,
        @Part memo: MultipartBody.Part?, // 메모
        @Part keywordIds: MultipartBody.Part?, // 키워드 (태그) 키워드 목록(최대 5) - ","로 구분
        @Part friendUserIds: MultipartBody.Part?, // 함께할 친구 ID - 함께하기인 경우 최소 1명 필수(최대 5)
        @Part scrapYn: MultipartBody.Part, // 스크랩 여부
        @Part images: List<MultipartBody.Part>?, // 이미지 목록(최대 3)
        @Part targetDate: MultipartBody.Part?, // 목표완료일(yyyy-MM-dd)
        @Part goalCount: MultipartBody.Part, //  목표 횟수
        @Part categoryId: MultipartBody.Part, // 카테고리 ID
        @Path("id") id: String
    ): CommonResponse

    //TODO : 실데이터 연결 필요
    @GET("/alarm")
    suspend fun loadAlarmList(): AlarmList

    // 피드, 버킷리스트 생성(수정) 시 키워드 아이템 로드
    @GET("/explore/keywords")
    suspend fun loadKeywords(): KeywordResponse

    // 팔로우 목록
    @GET("/follow")
    suspend fun loadFollowList(): FollowResponse

    // 펄로오 > 언팔하기
    @POST("/follow/unfollow")
    suspend fun requestUnfollow(
        @Query("followUserId") followUserId: String
    ): CommonResponse

    // 팔로우 하기
    @POST("/follow")
    suspend fun requestFollow(
        @Query("followUserId") followUserId: String
    ): CommonResponse

    // 프로필편집 > 프로필조회
    @GET("/user/profile")
    suspend fun loadMyProfile(): ProfileResponse

    // 프로필편집 > 프로필 수정
    @PATCH("/user/profile")
    @Multipart
    suspend fun updateMyProfile(
        @Part name: MultipartBody.Part,
        @Part bio: MultipartBody.Part,
        @Part profileImage: MultipartBody.Part?
    ): CommonResponse

    // 닉네임 중복 확인
    @GET("/user/profile/name")
    suspend fun checkAlreadyUsedNickname(
        @Query("name") name: String
    ): CommonResponse

    // 버킷리스트 전체 검색
    @GET("/bucketList")
    suspend fun searchAllBucketList(
        @Query("query") query: String
    ): AllBucketListResponse

    // 디데이 버킷리스트 검색
    @GET("/bucketList")
    suspend fun searchDdayBucketList(
        @Query("dDayBucketOnly") dDayBucketOnly: String? = YesOrNo.Y.name
    ): AllBucketListResponse

    // 버킷리스트 달성
    @GET("/bucketList/{id}/achieve")
    suspend fun achieveBucket(@Path("id") id: String): CommonResponse

    // 검색 > 버킷리스트 검색
    @GET("/explore")
    suspend fun loadSearchResult(@Query("query") query: String): SearchResultResponse

    // 검색 > 추천검색어, 최근검색어 조회
    @GET("/explore/searchOptions")
    suspend fun loadSearchRecommend(): SearchRecommendResponse

    // 검색 > 최근검색어 > 삭제
    @DELETE("/explore/recentSearch/{keyword}")
    suspend fun deleteSearchRecentWord(
        @Path("keyword") keyword: String
    ): CommonResponse

    // 피드 > 버킷리스트 좋아요
    @POST("/feeds/{id}/like")
    suspend fun saveBucketLike(@Path("id") id: String): CommonResponse

    // 피드 > 다른사람 버킷리스트 상세
    @GET("/bucketlist/{id}")
    suspend fun loadOtherBucketDetail(@Path("id") id: String): BucketDetailResponse

    // 버킷리스트 상세 > 댓글 달기
    @POST("/comment")
    suspend fun addBucketComment(@Body request: AddBucketCommentRequest): CommonResponse

    // 내 버킷리스트 > 달성횟수 변경
    @PATCH("/bucketlist/{id}/goalCount")
    suspend fun requestGoalCountUpdate(
        @Path("id") id: String,
        @Body goalCount: RequestGoalCountUpdate
    ): CommonResponse

    @POST("/comment/{id}/report")
    suspend fun requestCommentReport(
        @Path("id") id: String,
        @Query("reason") reason: String
    ): CommonResponse

    @GET("/follow/mutual")
    suspend fun loadWriteSelectableFriends(): LoadWriteSelectableFriendsResponse

    @GET("/other/profile/{otherUserId}")
    suspend fun loadOtherProfileInfo(
        @Path("otherUserId") id: String,
    ): ProfileResponse

    @GET("/other/bucketlist/{otherUserId}")
    suspend fun loadOtherBucketList(
        @Path("otherUserId") id: String,
    ): OtherBucketListResponse

    @GET("/other/follow/count/{otherUserId}")
    suspend fun loadOtherFollowData(@Path("otherUserId") id: String): OtherFollowDataResponse

    // 댓글 삭제
    @DELETE("/comment/{id}")
    suspend fun deleteComment(@Path("id") id: String): CommonResponse

    // 더보기 > 차단된 유저 목록
    @GET("/follow/blockUsers")
    suspend fun loadBlockedUsers(): BlockedUserResponse

    // 버킷리스트 신고
    @POST("/feed/{id}/report")
    suspend fun requestBucketReport(
        @Path("id") id: String,
        @Query("reason") reason: String
    ): CommonResponse

    // 버킷 복사
    @POST("/feeds/{id}/scrap")
    suspend fun copyOtherBucket(
        @Path("id") id: String
    ): CommonResponse

    @POST("/follow/block")
    suspend fun requestUserBlock(
        @Query("blockUserId ") blockUserId: String
    ): CommonResponse
}
