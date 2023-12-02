package com.zinc.berrybucket.ui_write.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.berrybucket.model.UIAddBucketListInfo
import com.zinc.berrybucket.model.WriteFriend
import com.zinc.berrybucket.model.WriteKeyWord
import com.zinc.berrybucket.model.WriteTotalInfo
import com.zinc.berrybucket.model.toUiModel
import com.zinc.berrybucket.ui.viewmodel.CommonViewModel
import com.zinc.common.models.AddBucketListRequest
import com.zinc.common.models.DetailInfo
import com.zinc.datastore.login.LoginPreferenceDataStoreModule
import com.zinc.domain.usecases.detail.LoadBucketDetail
import com.zinc.domain.usecases.keyword.LoadKeyWord
import com.zinc.domain.usecases.write.AddNewBucketList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WriteViewModel @Inject constructor(
    private val loginPreferenceDataStoreModule: LoginPreferenceDataStoreModule,
    private val addNewBucketList: AddNewBucketList,
    private val loadBucketDetail: LoadBucketDetail,
    private val loadKeyWord: LoadKeyWord
) : CommonViewModel(loginPreferenceDataStoreModule) {

    private val _savedWriteData = MutableLiveData<WriteTotalInfo>()
    val savedWriteData: LiveData<WriteTotalInfo> get() = _savedWriteData

    private val _prevWriteDataForUpdate = MutableLiveData<DetailInfo>()
    val prevWriteDataForUpdate: LiveData<DetailInfo> get() = _prevWriteDataForUpdate

    private val _searchFriendsResult = MutableLiveData<List<WriteFriend>>()
    val searchFriendsResult: LiveData<List<WriteFriend>> get() = _searchFriendsResult

    private val _addNewBucketListResult = MutableLiveData<Boolean>()
    val addNewBucketListResult: LiveData<Boolean> get() = _addNewBucketListResult

    private val _updateBucketListResult = MutableLiveData<Boolean>()
    val updateBucketListResult: LiveData<Boolean> get() = _updateBucketListResult

    private val _keywordList = MutableLiveData<List<WriteKeyWord>>()
    val keywordList: LiveData<List<WriteKeyWord>> get() = _keywordList

    private val _loadFail = MutableLiveData<Pair<String, String>>()
    val loadFail: LiveData<Pair<String, String>> get() = _loadFail


    fun addNewBucketList(writeInfo: UIAddBucketListInfo, isForUpdate: Boolean) {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            _loadFail.value = "버킷리스트 생성 실패" to "다시 시도해주세요"
            Log.e("ayhan", "addBucketResult fail1 : ${throwable.cause}")
            Log.e("ayhan", "Imagrs : ${writeInfo.images}")
        }) {
            runCatching {
                accessToken.value?.let { accessToken ->
                    val result = addNewBucketList.invoke(
                        accessToken,
                        addBucketListRequest = AddBucketListRequest(
                            bucketId = writeInfo.bucketId,
                            bucketType = writeInfo.bucketType,
                            exposureStatus = writeInfo.exposureStatus,
                            title = writeInfo.title,
                            memo = writeInfo.memo,
                            tags = writeInfo.tags,
                            friendUserIds = writeInfo.friendUserIds,
                            scrapYn = writeInfo.scrapYn,
                            images = writeInfo.images,
                            targetDate = writeInfo.targetDate,
                            goalCount = writeInfo.goalCount,
                            categoryId = writeInfo.categoryId
                        ),
                        isForUpdate = isForUpdate
                    )
                    Log.e("ayhan", "addBucketResult : $result")
                    if (result.success) {
                        _addNewBucketListResult.value = true
                    } else {
                        _loadFail.value = "버킷리스트 생성 실패" to result.message
                    }
                }
            }.getOrElse {
                _loadFail.value = "버킷리스트 생성 실패" to it.message.toString()
                Log.e("ayhan", "addBucketResult : fail2 ${it.message}")
            }
        }
    }

    fun clearFriendsData() {
        _searchFriendsResult.value = emptyList()
    }

    fun searchFriends(searchName: String) {
        _searchFriendsResult.value = buildList {
            add(WriteFriend("1", "ㅁㅁㅁ", "유저 닉네임 11"))
            add(WriteFriend("3", "ㅁㅁㅁ", "유저 닉네임 12"))
            add(WriteFriend("2", "ㅁㅁㅁ", "유저 닉네임 133"))
            add(WriteFriend("4", "ㅁㅁㅁ", "유저 닉네임 01"))
            add(WriteFriend("5", "ㅁㅁㅁ", "유저 1"))
            add(WriteFriend("6", "ㅁㅁㅁ", "유1"))
            add(WriteFriend("7", "ㅁㅁㅁ", "유5541"))
            add(WriteFriend("8", "ㅁㅁㅁ", "유55dd541"))
            add(WriteFriend("9", "ㅁㅁㅁ", "유5541"))
            add(WriteFriend("10", "ㅁㅁㅁ", "유d임 55541"))
        }
    }

    fun loadKeyword() {
        viewModelScope.launch(CoroutineExceptionHandler { coroutineContext, throwable ->
            _loadFail.value = "키워드 로딩 실패" to "데이터 로드에 실패했습니다."
        }) {
            runCatching {
                accessToken.value?.let { token ->
                    val result = loadKeyWord.invoke(token)
                    if (result.success) {
                        _keywordList.value = result.data?.toUiModel()
                    } else {
                        _loadFail.value = "키워드 로딩 실패" to result.message
                    }
                }
            }.getOrElse {
                _loadFail.value = "키워드 로딩 실패" to "데이터 로드에 실패했습니다."
            }
        }
    }

    fun savedWriteData(data: WriteTotalInfo?) {
        _savedWriteData.value = data ?: WriteTotalInfo()
    }

    fun getBucketDetailData(bucketId: String) {
        if (bucketId.isBlank().not() && bucketId != "NoId") {
            viewModelScope.launch(CEH(_loadFail, "버킷리스트 로드 실패" to "다시 시도해주세요")) {
                runCatching {
                    accessToken.value?.let { token ->
                        val result = loadBucketDetail(token, bucketId, true)

                        Log.e("ayhan", "loadBucketDetai; $result")
                        if (result.success) {
                            _prevWriteDataForUpdate.value = result.data
                        } else {
                            _loadFail.value = "버킷리스트 로드 실패" to "데이터 로드에 실패했습니다."
                        }
                    }
                }.getOrElse {
                    _loadFail.value = "버킷리스트 로드 실패" to "데이터 로드에 실패했습니다."
                }

            }
        } else {
            _savedWriteData.value = WriteTotalInfo()
        }

    }
}