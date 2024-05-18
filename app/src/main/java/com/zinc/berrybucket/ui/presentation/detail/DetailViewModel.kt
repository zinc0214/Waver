package com.zinc.berrybucket.ui.presentation.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.berrybucket.model.BucketDetailUiInfo
import com.zinc.berrybucket.model.CommentMentionInfo
import com.zinc.berrybucket.model.DetailLoadFailStatus
import com.zinc.berrybucket.ui.presentation.detail.model.bucketDetailResponseToUiModel
import com.zinc.berrybucket.ui.viewmodel.CommonViewModel
import com.zinc.berrybucket.util.SingleLiveEvent
import com.zinc.common.models.AddBucketCommentRequest
import com.zinc.common.models.DetailInfo
import com.zinc.common.models.ProfileInfo
import com.zinc.datastore.login.LoginPreferenceDataStoreModule
import com.zinc.domain.usecases.detail.AddBucketComment
import com.zinc.domain.usecases.detail.DeleteBucketComment
import com.zinc.domain.usecases.detail.GoalCountUpdate
import com.zinc.domain.usecases.detail.LoadBucketDetail
import com.zinc.domain.usecases.detail.LoadProfileInfo
import com.zinc.domain.usecases.my.AchieveMyBucket
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val loadBucketDetail: LoadBucketDetail,
    private val loadProfileInfo: LoadProfileInfo,
    private val achieveMyBucket: AchieveMyBucket,
    private val addBucketComment: AddBucketComment,
    private val deleteBucketComment: DeleteBucketComment,
    private val goalCountUpdate: GoalCountUpdate,
    loginPreferenceDataStoreModule: LoginPreferenceDataStoreModule
) : CommonViewModel(loginPreferenceDataStoreModule) {

    private val _bucketBucketDetailUiInfo = MutableLiveData<BucketDetailUiInfo>()
    val bucketBucketDetailUiInfo: LiveData<BucketDetailUiInfo> get() = _bucketBucketDetailUiInfo

    private val _validMentionList = MutableLiveData<List<CommentMentionInfo>>()
    val validMentionList: LiveData<List<CommentMentionInfo>> = _validMentionList

    private val _loadFail = SingleLiveEvent<DetailLoadFailStatus?>()
    val loadFail: LiveData<DetailLoadFailStatus?> get() = _loadFail

    private lateinit var bucketDetailData: DetailInfo
    private lateinit var profileInfo: ProfileInfo

    private var bucketId: String? = null
    private var writerId: String? = null
    private var isMine: Boolean? = true

    fun getBucketDetail(bucketId: String, writerId: String?, isMine: Boolean) {
        _loadFail.value = null

        this.bucketId = bucketId
        this.writerId = writerId
        this.isMine = isMine

        viewModelScope.launch(ceh(_loadFail, DetailLoadFailStatus.LoadFail)) {
            accessToken.value?.let { token ->

                // TODO : 다른사람 프로필 조회도 필요해!!!
                val job1 = launch { getBucketDetailData(token, bucketId, isMine) }
                val job2 = launch { getProfileInfo(token, isMine = isMine, writerId = writerId) }

                joinAll(job1, job2).runCatching {
                    Log.e("ayhan", "runCatching")
                }.getOrElse {
                    Log.e("ayhan", "getOrlElse, ${it.message}")
                }

                Log.e("ayhan", "getBucketDetail: $bucketDetailData , $profileInfo")

                _bucketBucketDetailUiInfo.value =
                    bucketDetailResponseToUiModel(bucketDetailData, profileInfo, isMine)
            } ?: run {
                Log.e("ayhan", "loadfail acceess")
                _loadFail.value = DetailLoadFailStatus.LoadFail
            }
        }
    }

    fun achieveMyBucket() {
        _loadFail.value = null

        viewModelScope.launch(ceh(_loadFail, DetailLoadFailStatus.AchieveFail)) {
            accessToken.value?.let { token ->
                val response = achieveMyBucket(token, bucketId!!)
                if (response.success) {
                    getBucketDetail(bucketId!!, writerId!!, isMine!!)
                } else {
                    _loadFail.value = DetailLoadFailStatus.AchieveFail
                }
            } ?: run {
                _loadFail.value = DetailLoadFailStatus.AchieveFail
            }
        }
    }

    private suspend fun getBucketDetailData(token: String, id: String, isMine: Boolean) {
        bucketDetailData = loadBucketDetail(token, id, isMine).data
    }

    private suspend fun getProfileInfo(token: String, writerId: String?, isMine: Boolean) {
        profileInfo = loadProfileInfo(token, isMine, writerId).data
    }

    fun getValidMentionList() {
        val validMentionList = mutableListOf<CommentMentionInfo>()

        repeat(10) {
            validMentionList.add(
                CommentMentionInfo(
                    userId = "$it",
                    profileImage = "",
                    nickName = "가나다$it",
                    isFriend = false,
                    isSelected = false
                )
            )
        }

        validMentionList.add(
            CommentMentionInfo(
                userId = "a",
                profileImage = "",
                nickName = "나는우주야",
                isFriend = false,
                isSelected = false
            )
        )
        validMentionList.add(
            CommentMentionInfo(
                userId = "b",
                profileImage = "",
                nickName = "한아",
                isFriend = false,
                isSelected = false
            )
        )

        _validMentionList.value = validMentionList
    }


    fun goalCountUpdate(goalCount: Int) {
        _loadFail.value = null
        viewModelScope.launch(ceh(_loadFail, DetailLoadFailStatus.GoalCountUpdateFail)) {
            accessToken.value?.let { token ->
                goalCountUpdate(token, bucketId!!, goalCount = goalCount)
                getBucketDetail(bucketId!!, writerId!!, true)
            } ?: run {
                _loadFail.value = DetailLoadFailStatus.GoalCountUpdateFail
            }
        }
    }

    fun addBucketComment(request: AddBucketCommentRequest) {
        Log.e("ayhan", "comment request : $request")
        _loadFail.value = null
        viewModelScope.launch(ceh(_loadFail, DetailLoadFailStatus.AddCommentFail)) {
            accessToken.value?.let { token ->
                val result = addBucketComment(token, request)
                Log.e("ayhan", "comment Result : $result")
                if (result.success) {
                    getBucketDetail(bucketId!!, writerId!!, isMine!!)
                } else {
                    _loadFail.value = DetailLoadFailStatus.AddCommentFail
                }
            } ?: run {
                _loadFail.value = DetailLoadFailStatus.AddCommentFail
            }
        }
    }

    fun deleteBucketComment(id: String) {
        _loadFail.value = null
        viewModelScope.launch(ceh(_loadFail, DetailLoadFailStatus.DeleteCommentFail)) {
            accessToken.value?.let { token ->
                val result = deleteBucketComment(token, id)
                Log.e("ayhan", "comment Result : $result")
                if (result.success) {
                    getBucketDetail(bucketId!!, writerId!!, isMine!!)
                } else {
                    _loadFail.value = DetailLoadFailStatus.DeleteCommentFail
                }
            } ?: run {
                _loadFail.value = DetailLoadFailStatus.DeleteCommentFail
            }
        }
    }
}