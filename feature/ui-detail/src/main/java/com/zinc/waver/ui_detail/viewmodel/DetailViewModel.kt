package com.zinc.waver.ui_detail.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.common.models.AddBucketCommentRequest
import com.zinc.common.models.DetailInfo
import com.zinc.common.models.ProfileInfo
import com.zinc.domain.usecases.detail.AddBucketComment
import com.zinc.domain.usecases.detail.DeleteBucketComment
import com.zinc.domain.usecases.detail.GoalCountUpdate
import com.zinc.domain.usecases.detail.LoadBucketDetail
import com.zinc.domain.usecases.detail.LoadProfileInfo
import com.zinc.domain.usecases.my.AchieveMyBucket
import com.zinc.waver.model.BucketDetailUiInfo
import com.zinc.waver.model.CommentMentionInfo
import com.zinc.waver.model.DetailLoadFailStatus
import com.zinc.waver.ui.viewmodel.CommonViewModel
import com.zinc.waver.ui_detail.model.bucketDetailResponseToUiModel
import com.zinc.waver.util.SingleLiveEvent
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
    private val goalCountUpdate: GoalCountUpdate
) : CommonViewModel() {

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

    fun loadInitData(bucketId: String, writerId: String?, isMine: Boolean) {
        _loadFail.value = null

        viewModelScope.launch {

            val job1 = launch { getBucketDetail(bucketId, writerId, isMine) }
            val job2 = launch { getValidMentionList() }

            joinAll(job1, job2).runCatching {
                Log.e("ayhan", "runCatching")
            }.getOrElse {
                Log.e("ayhan", "getOrlElse, ${it.message}")
                _loadFail.value = DetailLoadFailStatus.LoadFail
            }
        }
    }

    fun achieveMyBucket() {
        _loadFail.value = null

        viewModelScope.launch(ceh(_loadFail, DetailLoadFailStatus.AchieveFail)) {
            val response = achieveMyBucket(bucketId!!)
            if (response.success) {
                getBucketDetail(bucketId!!, writerId!!, isMine!!)
            } else {
                _loadFail.value = DetailLoadFailStatus.AchieveFail
            }
        }
    }

    fun getBucketDetail(
        bucketId: String,
        writerId: String?,
        isMine: Boolean
    ) {
        _loadFail.value = null

        this.bucketId = bucketId
        this.writerId = writerId
        this.isMine = isMine

        viewModelScope.launch(ceh(_loadFail, DetailLoadFailStatus.LoadFail)) {
            // TODO : 다른사람 프로필 조회도 필요해!!!
            val job1 = launch { getBucketDetailData(bucketId, isMine) }
            val job2 = launch { getProfileInfo(isMine = isMine, writerId = writerId) }

            joinAll(job1, job2).runCatching {
                Log.e("ayhan", "runCatching")
            }.getOrElse {
                Log.e("ayhan", "getOrlElse, ${it.message}")
            }

            Log.e("ayhan", "getBucketDetail: $bucketDetailData , $profileInfo")

            _bucketBucketDetailUiInfo.value =
                bucketDetailResponseToUiModel(bucketDetailData, profileInfo, isMine)
        }
    }

    private suspend fun getBucketDetailData(id: String, isMine: Boolean) {
        bucketDetailData = loadBucketDetail(id, isMine).data
    }

    private suspend fun getProfileInfo(writerId: String?, isMine: Boolean) {
        profileInfo = loadProfileInfo(isMine, writerId).data
    }

    private fun getValidMentionList() {
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


    fun requestGoalCountUpdate(goalCount: Int) {
        _loadFail.value = null
        viewModelScope.launch(ceh(_loadFail, DetailLoadFailStatus.GoalCountUpdateFail)) {
            goalCountUpdate(bucketId!!, goalCount = goalCount)
            getBucketDetail(bucketId!!, writerId!!, true)
        }
    }

    fun requestAddBucketComment(request: AddBucketCommentRequest) {
        Log.e("ayhan", "comment request : $request")
        _loadFail.value = null
        viewModelScope.launch(ceh(_loadFail, DetailLoadFailStatus.AddCommentFail)) {
            val result = addBucketComment(request)
            Log.e("ayhan", "comment Result : $result")
            if (result.success) {
                getBucketDetail(bucketId!!, writerId!!, isMine!!)
            } else {
                _loadFail.value = DetailLoadFailStatus.AddCommentFail
            }
        }
    }

    fun requestDeleteBucketComment(id: String) {
        _loadFail.value = null
        viewModelScope.launch(ceh(_loadFail, DetailLoadFailStatus.DeleteCommentFail)) {
            val result = deleteBucketComment(id)
            Log.e("ayhan", "comment Result : $result")
            if (result.success) {
                getBucketDetail(bucketId!!, writerId!!, isMine!!)
            } else {
                _loadFail.value = DetailLoadFailStatus.DeleteCommentFail
            }
        }
    }
}