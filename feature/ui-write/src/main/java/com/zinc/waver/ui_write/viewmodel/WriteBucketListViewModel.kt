package com.zinc.waver.ui_write.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.common.models.AddBucketListRequest
import com.zinc.common.models.DetailInfo
import com.zinc.common.models.YesOrNo
import com.zinc.datastore.login.PreferenceDataStoreModule
import com.zinc.domain.usecases.category.LoadCategoryList
import com.zinc.domain.usecases.detail.LoadBucketDetail
import com.zinc.domain.usecases.detail.LoadFriends
import com.zinc.domain.usecases.keyword.LoadKeyWord
import com.zinc.domain.usecases.write.AddNewBucketList
import com.zinc.waver.model.UIAddBucketListInfo
import com.zinc.waver.model.WriteFriend
import com.zinc.waver.model.WriteKeyWord
import com.zinc.waver.model.WriteTotalInfo
import com.zinc.waver.model.toUiModel
import com.zinc.waver.ui.viewmodel.CommonViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WriteBucketListViewModel @Inject constructor(
    private val addNewBucketList: AddNewBucketList,
    private val loadBucketDetail: LoadBucketDetail,
    private val loadKeyWord: LoadKeyWord,
    private val loadFriends: LoadFriends,
    private val loadCategoryList: LoadCategoryList,
    private val preferenceDataStoreModule: PreferenceDataStoreModule,
) : CommonViewModel() {

    private val _savedWriteData = MutableLiveData<WriteTotalInfo>()
    val savedWriteData: LiveData<WriteTotalInfo> get() = _savedWriteData

    private val _prevWriteDataForUpdate = MutableLiveData<DetailInfo>()
    val prevWriteDataForUpdate: LiveData<DetailInfo> get() = _prevWriteDataForUpdate

    private val _searchFriendsResult = MutableLiveData<List<WriteFriend>>()
    val searchFriendsResult: LiveData<List<WriteFriend>> get() = _searchFriendsResult

    private val _addNewBucketListResult = MutableLiveData<Boolean>()
    val addNewBucketListResult: LiveData<Boolean> get() = _addNewBucketListResult

    private val _keywordList = MutableLiveData<List<WriteKeyWord>>()
    val keywordList: LiveData<List<WriteKeyWord>> get() = _keywordList

    private val _loadFail = MutableLiveData<Pair<String, String>?>()
    val loadFail: LiveData<Pair<String, String>?> get() = _loadFail

    private val _hasWaverPlus = MutableLiveData<Boolean>()
    val hasWaverPlus: LiveData<Boolean> get() = _hasWaverPlus

    private val _defaultCategoryId = MutableLiveData<Int>()

    fun clearData() {
        _loadFail.value = null
    }

    fun loadCategory() {
        viewModelScope.launch(ceh(_loadFail, "카테고리 로드 실패" to "다시 시도해주세요")) {
            val result = loadCategoryList.invoke()
            _defaultCategoryId.value = result.data.firstOrNull { it.defaultYn == YesOrNo.Y }?.id
            Log.e("ayhan", "category : $result")
        }
    }

    fun addNewBucketList(writeInfo: UIAddBucketListInfo, isForUpdate: Boolean) {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            _loadFail.value = "버킷리스트 생성 실패" to "다시 시도해주세요"
            Log.e("ayhan", "addBucketResult fail1 : ${throwable.cause}")
            Log.e("ayhan", "Imagrs : ${writeInfo.images}")
        }) {
            val categoryId = if (writeInfo.categoryId == -1) _defaultCategoryId.value
                ?: -1 else writeInfo.categoryId
            runCatching {
                _loadFail.value = null
                val result = addNewBucketList.invoke(
                    addBucketListRequest = AddBucketListRequest(
                        bucketId = writeInfo.bucketId,
                        bucketType = writeInfo.bucketType,
                        exposureStatus = writeInfo.exposureStatus,
                        title = writeInfo.title,
                        memo = writeInfo.memo,
                        keywords = writeInfo.keywords,
                        friendUserIds = writeInfo.friendUserIds?.joinToString { "," },
                        scrapYn = writeInfo.scrapYn,
                        images = writeInfo.images,
                        targetDate = writeInfo.targetDate,
                        goalCount = writeInfo.goalCount,
                        categoryId = categoryId
                    ),
                    isForUpdate = isForUpdate
                )
                Log.e("ayhan", "addBucketResult : $result")
                if (result.success) {
                    _addNewBucketListResult.value = true
                } else {
                    _loadFail.value = "버킷리스트 생성 실패" to result.message
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

    fun loadFriends() {
        viewModelScope.launch(ceh(_loadFail, "친구 로드 실패" to "로드 실패!")) {
            runCatching {
                _loadFail.value = null
                val res = loadFriends.invoke()
                Log.e("ayhan", "freinds : $res")
                if (res.success) {
//                    _searchFriendsResult.value = buildList {
//                        repeat(10) {
//                            add(WriteFriend(id = "$it", imageUrl = "", nickname = "카카$it"))
//                        }
//                    }
                    _searchFriendsResult.value = res.data.filter { it.mutualFollow }.map {
                        WriteFriend(it.id, it.imgUrl, it.name)
                    }
                } else {
                    _loadFail.value = "친구 로드 실패" to res.message
                }
            }.getOrElse {
                _loadFail.value = "친구 로드 실패" to "로드 실패!"
            }
        }
    }

    fun searchFriends(searchName: String) {
        _searchFriendsResult.value = searchFriendsResult.value?.filter {
            it.nickname == searchName
        }.orEmpty()
    }

    fun loadKeyword() {
        _loadFail.value = null
        viewModelScope.launch(CoroutineExceptionHandler { coroutineContext, throwable ->
            _loadFail.value = "키워드 로딩 실패" to "데이터 로드에 실패했습니다."
        }) {
            runCatching {
                val result = loadKeyWord.invoke()
                Log.e("ayhan", "laodKeywrod : $result")
                if (result.success) {
                    _keywordList.value = result.data?.toUiModel()
                } else {
                    _loadFail.value = "키워드 로딩 실패" to result.message
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
        _loadFail.value = null
        if (bucketId.isBlank().not() && bucketId != "NoId") {
            viewModelScope.launch(ceh(_loadFail, "버킷리스트 로드 실패" to "다시 시도해주세요")) {
                runCatching {
                    val result = loadBucketDetail(bucketId, true)
                    Log.e("ayhan", "loadBucketDetai; $result")
                    if (result.success) {
                        _prevWriteDataForUpdate.value = result.data
                    } else {
                        _loadFail.value = "버킷리스트 로드 실패" to "데이터 로드에 실패했습니다."
                    }
                }.getOrElse {
                    _loadFail.value = "버킷리스트 로드 실패" to "데이터 로드에 실패했습니다."
                }
            }
        } else {
            _savedWriteData.value = WriteTotalInfo()
        }
    }

    fun checkHasWaverPlus() {
        viewModelScope.launch {
            preferenceDataStoreModule.loadHasWaverPlus.collectLatest { value ->
                _hasWaverPlus.value = value
            }
        }
    }
}