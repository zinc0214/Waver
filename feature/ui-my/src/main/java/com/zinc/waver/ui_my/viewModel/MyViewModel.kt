package com.zinc.waver.ui_my.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.common.models.AllBucketListRequest
import com.zinc.common.models.AllBucketListSortType
import com.zinc.common.models.BucketStatus
import com.zinc.common.models.DdaySortType
import com.zinc.common.models.TopProfile
import com.zinc.common.models.YesOrNo
import com.zinc.datastore.bucketListFilter.FilterPreferenceDataStoreModule
import com.zinc.domain.usecases.category.SearchCategoryList
import com.zinc.domain.usecases.my.AchieveMyBucket
import com.zinc.domain.usecases.my.LoadAllBucketList
import com.zinc.domain.usecases.my.LoadHomeProfileInfo
import com.zinc.domain.usecases.my.SearchAllBucketList
import com.zinc.domain.usecases.my.SearchDdayBucketList
import com.zinc.waver.model.AllBucketList
import com.zinc.waver.model.MyTabType
import com.zinc.waver.model.MyTabType.ALL
import com.zinc.waver.model.MyTabType.CATEGORY
import com.zinc.waver.model.MyTabType.CHALLENGE
import com.zinc.waver.model.MyTabType.DDAY
import com.zinc.waver.model.parseToUI
import com.zinc.waver.model.parseUI
import com.zinc.waver.ui.viewmodel.CommonViewModel
import com.zinc.waver.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val loadHomeProfileInfo: LoadHomeProfileInfo,
    private val loadAllBucketList: LoadAllBucketList,
    private val searchAllBucketList: SearchAllBucketList,
    private val searchCategoryList: SearchCategoryList,
    private val searchDdayBucketList: SearchDdayBucketList,
    private val achieveMyBucket: AchieveMyBucket,
    private val filterPreferenceDataStoreModule: FilterPreferenceDataStoreModule
) : CommonViewModel() {

    private val _profileInfo = MutableLiveData<TopProfile>()
    val profileInfo: LiveData<TopProfile> get() = _profileInfo

    private val _searchBucketResult = MutableLiveData<Pair<MyTabType, List<*>>>()
    val searchResult: LiveData<Pair<MyTabType, List<*>>> get() = _searchBucketResult

    private val _prevSearchedResult = MutableLiveData<Pair<MyTabType, String>>()
    val prevSearchedResult: LiveData<Pair<MyTabType, String>> get() = _prevSearchedResult

    private val _allBucketItem = MutableLiveData<AllBucketList>()
    val allBucketItem: LiveData<AllBucketList> get() = _allBucketItem

    private val _ddayBucketList = MutableLiveData<AllBucketList>()
    val ddayBucketList: LiveData<AllBucketList> = _ddayBucketList

    private val _showProgress = MutableLiveData<Boolean>()
    val showProgress: LiveData<Boolean> get() = _showProgress

    private val _showSucceed = MutableLiveData<Boolean>()
    val showSucceed: LiveData<Boolean> get() = _showSucceed

    private val _orderType = MutableLiveData<Int>()
    val orderType: LiveData<Int> get() = _orderType

    private val _showDdayView = MutableLiveData<Boolean>()
    val showDdayView: LiveData<Boolean> get() = _showDdayView

    private val _isNeedToUpdate = MutableLiveData<Boolean>()
    val isNeedToUpdate: LiveData<Boolean> get() = _isNeedToUpdate

    private val _isShowPlusDday = MutableLiveData<Boolean>()
    val isShowPlusDday: LiveData<Boolean> get() = _isShowPlusDday

    private val _isShownMinusDday = MutableLiveData<Boolean>()
    val isShownMinusDday: LiveData<Boolean> get() = _isShownMinusDday

    private val _dataLoadFailed = SingleLiveEvent<Nothing>()
    val dataLoadFailed: LiveData<Nothing> get() = _dataLoadFailed

    private val _searchFailed = SingleLiveEvent<Nothing>()
    val searchFailed: LiveData<Nothing> get() = _searchFailed

    private val _achieveBucketFail = SingleLiveEvent<Nothing>()
    val achieveBucketFail: LiveData<Nothing> get() = _achieveBucketFail

    private val _achieveSucceed = MutableLiveData<String>()
    val achieveSucceed: LiveData<String> get() = _achieveSucceed

    private val _allFilterLoadFinished = MutableLiveData<Boolean>()
    val allFilterLoadFinished: LiveData<Boolean> get() = _allFilterLoadFinished

    private val _ddayFilterLoadFinished = MutableLiveData<Boolean>()
    val ddayFilterLoadFinished: LiveData<Boolean> get() = _ddayFilterLoadFinished

    private val _allFilterSavedFinished = MutableLiveData<Boolean>()
    val allFilterSavedFinished: LiveData<Boolean> get() = _allFilterSavedFinished

    private val _ddayFilterSavedFinished = MutableLiveData<Boolean>()
    val ddayFilterSavedFinished: LiveData<Boolean> get() = _ddayFilterSavedFinished

    private var isPrefChanged = false

    private val searchCeh = CoroutineExceptionHandler { _, throwable ->
        Log.e("ayhan", "searchFail 1 : $throwable")
        _searchFailed.call()
    }

    private fun ceh(liveData: SingleLiveEvent<Nothing>) =
        CoroutineExceptionHandler { _, _ ->
            liveData.call()
        }

    fun loadAllBucketFilter() {
        viewModelScope.launch {

            Log.e(
                "ayhan",
                "status check : ${_showProgress.value} , ${_showSucceed.value}, ${_showDdayView.value}"
            )
            val job1 = launch { loadShowProgressDataStore() }
            val job2 = launch { loadShowSucceedDataStore() }
            val job3 = launch { loadOrderTypeDataStore() }
            val job4 = launch { loadShowDdayDataStore() }
            val job5 = launch {
                _allFilterLoadFinished.value = true
            }
            joinAll(job1, job2, job3, job4, job5)
        }
    }

    fun loadDdayBucketFilter() {
        viewModelScope.launch {
            val job1 = launch { loadFilerDdayMinusDataStore() }
            val job2 = launch { loadFilerDdayPlusDataStore() }
            val job3 = launch {
                _ddayFilterLoadFinished.value = true
            }

            joinAll(job1, job2, job3)
        }
    }

    private suspend fun loadShowProgressDataStore() {
        filterPreferenceDataStoreModule.apply {
            loadIsProgress.collectLatest {
                isPrefChanged = isPrefChanged || _showProgress.value != it
                _showProgress.value = it
                Log.e("ayhan", "loadShowProgressDataStore")
            }
        }
    }

    private suspend fun loadShowSucceedDataStore() {
        filterPreferenceDataStoreModule.apply {
            loadIsSucceed.collectLatest {
                isPrefChanged = isPrefChanged || _showSucceed.value != it
                _showSucceed.value = it
                Log.e("ayhan", "loadShowSucceedDataStore")
            }
        }
    }

    private suspend fun loadOrderTypeDataStore() {
        filterPreferenceDataStoreModule.apply {
            loadOrderType.collectLatest {
                isPrefChanged = isPrefChanged || _orderType.value != it
                _orderType.value = it
                Log.e("ayhan", "loadOrderTypeDataStore")
            }
        }
    }

    private suspend fun loadShowDdayDataStore() {
        filterPreferenceDataStoreModule.apply {
            loadShowDday.collectLatest {
                isPrefChanged = isPrefChanged || _showDdayView.value != it
                _showDdayView.value = it
                Log.e("ayhan", "loadShowDdayDataStore")
            }
        }
    }

    private suspend fun loadFilerDdayMinusDataStore() {
        filterPreferenceDataStoreModule.apply {
            loadIsDdayMinus.collectLatest {
                isPrefChanged = isPrefChanged || _isShownMinusDday.value != it
                _isShownMinusDday.value = it
            }
        }
    }

    private suspend fun loadFilerDdayPlusDataStore() {
        filterPreferenceDataStoreModule.apply {
            loadIsDdayPlus.collectLatest {
                isPrefChanged = isPrefChanged || _isShowPlusDday.value != it
                _isShowPlusDday.value = it
            }
        }
    }

    fun needToReload(isNeed: Boolean) {
        _isNeedToUpdate.value = isNeed
    }

    fun loadProfile() {
        viewModelScope.launch {
            runCatching {
                val response = loadHomeProfileInfo.invoke()
                Log.e("ayhan", "tokgen : $response")

                if (response.success) {
                    val data = response.data
                    val topProfile = TopProfile(
                        name = data.name,
                        imgUrl = data.imgUrl,
                        badgeImgUrl = data.badgeUrl,
                        badgeTitle = data.badgeTitle,
                        bio = data.bio,
                        followingCount = data.followingCount,
                        followerCount = data.followerCount,
                        percent = data.bucketInfo.grade()
                    )

                    Log.e("ayhan", "homeProfike : $topProfile")
                    _profileInfo.value = topProfile
                } else {
                    Log.e("ayhan", "Fail Load Profile not success")
                    loadDummyProfile()
                }
            }.getOrElse {
                Log.e("ayhan", "Fail Load Profile : $it")
                loadDummyProfile()
            }
        }
    }

    // TODO : 제거
    private fun loadDummyProfile() {
        val topProfile = TopProfile(
            name = "한아로해봐",
            imgUrl = "ddd",
            percent = 0.6f,
            badgeImgUrl = "",
            badgeTitle = "안녕 반가우이잇",
            bio = "나는 ESFP 한아라고 불러줘?",
            followingCount = "20",
            followerCount = "10"
        )
        _profileInfo.value = topProfile
    }

    fun loadAllBucketList() {
        val allBucketListRequest = AllBucketListRequest(
            dDayBucketOnly = YesOrNo.N.name,
            isPassed = null,
            status = loadStatusFilter(),
            sort = loadSortFilter()
        )

        Log.e("ayhan", "allBucketListRequest : ${allBucketListRequest}")

        viewModelScope.launch {
            runCatching {
                loadAllBucketList.invoke(
                    //token,
                    allBucketListRequest
                ).apply {
                    if (this.success) {
                        val data = this.data
                        Log.e("ayhan", "allBucketList : $this")
                        val uiALlBucketType = AllBucketList(
                            processingCount = data.progressCount.toString(),
                            succeedCount = data.completedCount.toString(),
                            bucketList = data.bucketlist.parseToUI()
                        )
                        _allBucketItem.value = uiALlBucketType
                        _allFilterLoadFinished.value = false
                    }

                }
            }.getOrElse {

            }
        }
    }

    private fun loadStatusFilter(): BucketStatus? =
        if (_showSucceed.value == true && _showProgress.value == true) null
        else if (_showSucceed.value == true) BucketStatus.COMPLETE
        else if (_showProgress.value == true) BucketStatus.PROGRESS
        else BucketStatus.PROGRESS

    private fun loadSortFilter() =
        if (_orderType.value == 1) AllBucketListSortType.CREATED else AllBucketListSortType.UPDATED

    fun loadDdayBucketList() {
        val allBucketListRequest = AllBucketListRequest(
            dDayBucketOnly = YesOrNo.Y.name,
            isPassed = null,
            status = null,
            sort = loadSortFilter()
        )

        viewModelScope.launch {
            runCatching {
                loadAllBucketList.invoke(allBucketListRequest).apply {
                    if (this.success) {
                        val data = this.data
                        val uiAllBucketList = AllBucketList(
                            processingCount = data.progressCount.toString(),
                            succeedCount = data.completedCount.toString(),
                            bucketList = data.bucketlist.parseToUI()
                        )

                        val filteredList =
                            if (_isShownMinusDday.value == true && _isShowPlusDday.value == true) {
                                uiAllBucketList.bucketList
                            } else if (_isShowPlusDday.value == true) {
                                uiAllBucketList.bucketList.filter { it.getDdayType() == DdaySortType.PLUS }
                            } else if (_isShownMinusDday.value == true) {
                                uiAllBucketList.bucketList.filterNot { it.getDdayType() == DdaySortType.PLUS }
                            } else {
                                uiAllBucketList.bucketList
                            }

                        _ddayBucketList.value = uiAllBucketList.copy(bucketList = filteredList)
                        _ddayFilterLoadFinished.value = false
                        Log.e("ayhan", "filteredList : $filteredList")
                    }
                }
            }.getOrElse {

            }
        }
    }

    fun searchList(type: MyTabType, searchWord: String) {
        _prevSearchedResult.value = type to searchWord
        when (type) {
            is ALL -> searchAllBucket(searchWord = searchWord)
            is DDAY -> searchDdayBucket(searchWord = searchWord)
            is CATEGORY -> searchCategoryItems(searchWord = searchWord)
            is CHALLENGE -> searchChallenge(searchWord = searchWord)
        }
    }

    fun updateAllBucketFilter(
        isProgress: Boolean?,
        isSucceed: Boolean?,
        orderType: Int?,
        showDday: Boolean?
    ) {
        viewModelScope.launch {
            filterPreferenceDataStoreModule.apply {
                isProgress?.let {
                    setProgress(isProgress)
                    _showProgress.value = it
                }
                isSucceed?.let {
                    setSucceed(isSucceed)
                    _showSucceed.value = it
                }
                orderType?.let {
                    setOrderType(it)
                    _orderType.value = it
                }
                showDday?.let {
                    setShowDday(it)
                    _showDdayView.value = it
                }
            }

            Log.e(
                "ayhan",
                "status check2 : ${_showProgress.value} , ${_showSucceed.value}, ${_showDdayView.value}"
            )
            _allFilterSavedFinished.value = true
            _isNeedToUpdate.value = true
        }
    }

    fun updateDdayBucketFilter(
        isMinusShow: Boolean?,
        isPlusShow: Boolean?
    ) {
        viewModelScope.launch {
            filterPreferenceDataStoreModule.apply {
                isMinusShow?.let {
                    Log.e("ayhan", "minus :  $it")
                    setShowDdayMinus(isMinusShow)
                    _isShownMinusDday.value = it
                }
                isPlusShow?.let {
                    Log.e("ayhan", "plus :  $it")
                    setShowDdayPlus(isPlusShow)
                    _isShowPlusDday.value = it
                }
            }
            _ddayFilterSavedFinished.value = true
            _isNeedToUpdate.value = true
        }
    }

    fun clearFilterSavedStatus() {
        _allFilterSavedFinished.value = false
        _ddayFilterSavedFinished.value = false
    }

    private fun searchAllBucket(searchWord: String) {
        viewModelScope.launch(searchCeh) {
            runCatching {
                searchAllBucketList.invoke(searchWord).apply {
                    if (this.success) {
                        _searchBucketResult.value = Pair(
                            ALL,
                            this.data.bucketlist
                        )
                    } else {
                        Log.e("ayhan", "searchFail 3 : ${this.message}")
                        _searchFailed.call()
                    }

                }
            }.getOrElse {
                Log.e("ayhan", "searchFail 2 : ${it.message}")
                _searchFailed.call()
            }
        }
    }

    private fun searchDdayBucket(searchWord: String) {
        viewModelScope.launch(searchCeh) {
            searchDdayBucketList.invoke(searchWord).apply {
                if (this.success) {
                    _searchBucketResult.value = Pair(
                        DDAY,
                        this.data.bucketlist
                    )
                } else {
                    Log.e("ayhan", "searchFail 3 : ${this.message}")
                    _searchFailed.call()
                }
            }
        }
    }

    private fun searchCategoryItems(searchWord: String) {
        viewModelScope.launch(searchCeh) {
            runCatching {
                searchCategoryList.invoke(searchWord).apply {
                    Log.e("ayhan", "category : $this")
                    if (this.success) {
                        val parseData = data.parseUI()
                        _searchBucketResult.value = Pair(
                            CATEGORY,
                            parseData
                        )
                    } else {
                        _searchFailed.call()
                        Log.e("ayhan", "searchCategoryItems Fail3 : $message")
                    }
                }
            }.getOrElse {
                Log.e("ayhan", "searchCategoryItems Fail2 : ${it.message}")
                _searchFailed.call()
            }
        }
    }

    private fun searchChallenge(searchWord: String) {
        // TODO
    }

    fun achieveBucket(id: String, type: MyTabType) {
        viewModelScope.launch(ceh(_achieveBucketFail)) {
            val response = achieveMyBucket(id)
            Log.e("ayhan", "Achieve Response : $response")
            if (response.success) {
                when (type) {
                    is ALL -> loadAllBucketList()
                    is DDAY -> loadDdayBucketList()
                    else -> {
                        // Do Nothing
                    }
                }
            } else {
                _achieveBucketFail.call()
            }
        }
    }
}