package com.zinc.berrybucket.ui_my.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.berrybucket.CommonViewModel
import com.zinc.berrybucket.model.AllBucketList
import com.zinc.berrybucket.model.MyTabType
import com.zinc.berrybucket.model.MyTabType.ALL
import com.zinc.berrybucket.model.MyTabType.CATEGORY
import com.zinc.berrybucket.model.MyTabType.CHALLENGE
import com.zinc.berrybucket.model.MyTabType.DDAY
import com.zinc.berrybucket.model.parseToUI
import com.zinc.common.models.AllBucketListRequest
import com.zinc.common.models.AllBucketListSortType
import com.zinc.common.models.BucketInfoSimple
import com.zinc.common.models.BucketStatus
import com.zinc.common.models.BucketType
import com.zinc.common.models.CategoryInfo
import com.zinc.common.models.DdayBucketList
import com.zinc.common.models.ExposureStatus
import com.zinc.common.models.YesOrNo
import com.zinc.datastore.bucketListFilter.FilterPreferenceDataStoreModule
import com.zinc.datastore.login.LoginPreferenceDataStoreModule
import com.zinc.domain.models.TopProfile
import com.zinc.domain.usecases.my.LoadAllBucketList
import com.zinc.domain.usecases.my.LoadCategoryList
import com.zinc.domain.usecases.my.LoadDdayBucketList
import com.zinc.domain.usecases.my.LoadHomeProfileInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val loadHomeProfileInfo: LoadHomeProfileInfo,
    private val loadAllBucketList: LoadAllBucketList,
    private val loadCategoryList: LoadCategoryList,
    private val loadDdayBucketList: LoadDdayBucketList,
    private val filterPreferenceDataStoreModule: FilterPreferenceDataStoreModule,
    loginPreferenceDataStoreModule: LoginPreferenceDataStoreModule
) : CommonViewModel(loginPreferenceDataStoreModule) {

    private val _profileInfo = MutableLiveData<TopProfile>()
    val profileInfo: LiveData<TopProfile> get() = _profileInfo

    private val _searchBucketResult = MutableLiveData<Pair<MyTabType, List<*>>>()
    val searchResult: LiveData<Pair<MyTabType, List<*>>> get() = _searchBucketResult

    private val _allBucketItem = MutableLiveData<AllBucketList>()
    val allBucketItem: LiveData<AllBucketList> get() = _allBucketItem

    private val _categoryInfoItems = MutableLiveData<List<CategoryInfo>>()
    val categoryInfoItems: LiveData<List<CategoryInfo>> get() = _categoryInfoItems

    private val _ddayBucketList = MutableLiveData<DdayBucketList>()
    val ddayBucketList: LiveData<DdayBucketList> = _ddayBucketList

    private val _showProgress = MutableLiveData<Boolean>()
    val showProgress: LiveData<Boolean> get() = _showProgress

    private val _showSucceed = MutableLiveData<Boolean>()
    val showSucceed: LiveData<Boolean> get() = _showSucceed

    private val _orderType = MutableLiveData<Int>()
    val orderType: LiveData<Int> get() = _orderType

    private val _showDdayView = MutableLiveData<Boolean>()
    val showDdayView: LiveData<Boolean> get() = _showDdayView

    fun loadBucketFilter() {
        viewModelScope.launch {
            val job1: Deferred<Unit> = async { loadShowProgressDataStore() }
            val job2: Deferred<Unit> = async { loadShowSucceedDataStore() }
            val job3: Deferred<Unit> = async { loadOrderTypeDataStore() }
            val job4: Deferred<Unit> = async { loadShowDdayDataStore() }

            awaitAll(job1, job2, job3, job4)
        }
    }

    private suspend fun loadShowProgressDataStore() {
        filterPreferenceDataStoreModule.apply {
            loadIsProgress.collectLatest {
                _showProgress.value = it
            }
        }
    }

    private suspend fun loadShowSucceedDataStore() {
        filterPreferenceDataStoreModule.apply {
            loadIsSucceed.collectLatest {
                _showSucceed.value = it
            }
        }
    }

    private suspend fun loadOrderTypeDataStore() {
        filterPreferenceDataStoreModule.apply {
            loadOrderType.collectLatest {
                _orderType.value = it
            }
        }
    }

    private suspend fun loadShowDdayDataStore() {
        filterPreferenceDataStoreModule.apply {
            loadShowDday.collectLatest {
                _showDdayView.value = it
            }
        }
    }


    fun loadProfile() {
        viewModelScope.launch {
            runCatching {
                accessToken.value?.let { token ->
                    Log.e("ayhan", "token : $token")

                    val response = loadHomeProfileInfo.invoke(token)
                    Log.e("ayhan", "tokgen : $response")

                    if (response.success) {
                        val data = response.data
                        val topProfile = TopProfile(
                            name = data.name,
                            imgUrl = data.imgUrl,
                            badgeType = data.badgeUrl,
                            badgeTitle = data.badgeTitle,
                            bio = data.bio,
                            followingCount = data.followingCount,
                            followerCount = data.followerCount,
                            percent = data.bucketInfo.grade()
                        )
                        _profileInfo.value = topProfile
                    } else {
                        Log.e("ayhan", "Fail Load Profile not success")
                        loadDummyProfile()
                    }
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
            badgeType = "",
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
                accessToken.value?.let { token ->
                    loadAllBucketList.invoke(
                        token,
                        allBucketListRequest
                    ).apply {
                        if (this.success) {
                            val data = this.data
                            Log.e("ayhan", "allBucketList : $this")
                            val uiALlBucketType = AllBucketList(
                                processingCount = data.processingCount.toString(),
                                succeedCount = data.completedCount.toString(),
                                bucketList = data.bucketlist.parseToUI()
                            )
                            _allBucketItem.value = uiALlBucketType
                        }

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

    fun loadCategoryList() {
        viewModelScope.launch {
            kotlin.runCatching {
                loadCategoryList.invoke().apply {
                    _categoryInfoItems.value = this
                }
            }
        }
    }

    fun loadDdayBucketList() {
        viewModelScope.launch {
            kotlin.runCatching {
                loadDdayBucketList.invoke().apply {
                    _ddayBucketList.value = this
                }
            }.getOrElse {

            }
        }
    }

    fun searchList(type: MyTabType, searchWord: String) {
        when (type) {
            is ALL -> searchAllBucket(searchWord = searchWord)
            is DDAY -> searchDdayBucket(searchWord = searchWord)
            is CATEGORY -> searchCategoryItems(searchWord = searchWord)
            is CHALLENGE -> searchChallenge(searchWord = searchWord)
        }
    }

    fun updateBucketFilter(
        isProgress: Boolean?,
        isSucceed: Boolean?,
        orderType: Int?,
        showDday: Boolean?
    ) {
        viewModelScope.launch {
            filterPreferenceDataStoreModule.apply {
                isProgress?.let {
                    setProgress(isProgress)
                }
                isSucceed?.let {
                    setSucceed(isSucceed)
                }
                orderType?.let {
                    setOrderType(it)
                }
                showDday?.let {
                    setShowDday(it)
                }
            }
        }
    }

    private fun searchAllBucket(searchWord: String) {
        _searchBucketResult.value = Pair(
            ALL(),
            simpleTypeList
        )
    }

    private fun searchDdayBucket(searchWord: String) {
        _searchBucketResult.value = Pair(
            DDAY(),
            simpleTypeList
        )
    }

    private fun searchCategoryItems(searchWord: String) {
        _searchBucketResult.value = Pair(
            CATEGORY(),
            searchCategoryInfos
        )
    }

    private fun searchChallenge(searchWord: String) {
        _searchBucketResult.value = Pair(
            CHALLENGE(),
            simpleTypeList
        )
    }

    private val simpleTypeList = listOf(
        BucketInfoSimple(
            id = "1",
            title = "아이스크림을 먹을테야 얍얍압얍",
            userCount = 1,
            status = BucketStatus.PROGRESS,
            exposureStatus = ExposureStatus.PUBLIC,
            bucketType = BucketType.ORIGINAL

        ),
        BucketInfoSimple(
            id = "2",
            title = "아이스크림을 여행을 갈거란 말이야",
            userCount = 1,
            status = BucketStatus.PROGRESS,
            exposureStatus = ExposureStatus.PUBLIC,
            bucketType = BucketType.ORIGINAL
        ),
        BucketInfoSimple(
            id = "3",
            title = "Dday가 있는 애22233",
            userCount = 5,
            goalCount = 10,
            dDay = -10,
            status = BucketStatus.PROGRESS,
            exposureStatus = ExposureStatus.PUBLIC,
            bucketType = BucketType.ORIGINAL
        ),
        BucketInfoSimple(
            id = "3",
            title = "Dday가 있는 애22233",
            userCount = 5,
            goalCount = 10,
            dDay = -10,
            status = BucketStatus.PROGRESS,
            exposureStatus = ExposureStatus.PUBLIC,
            bucketType = BucketType.ORIGINAL
        ),
        BucketInfoSimple(
            id = "3",
            title = "Dday가 있는 애22233",
            userCount = 5,
            goalCount = 10,
            dDay = -10,
            status = BucketStatus.PROGRESS,
            exposureStatus = ExposureStatus.PUBLIC,
            bucketType = BucketType.ORIGINAL

        ),
        BucketInfoSimple(
            id = "3",
            title = "Dday가 있는 애22233",
            userCount = 5,
            goalCount = 10,
            dDay = -10,
            status = BucketStatus.PROGRESS,
            exposureStatus = ExposureStatus.PUBLIC,
            bucketType = BucketType.ORIGINAL
        )
    )

    private val searchCategoryInfos = listOf(
        CategoryInfo(
            id = 1,
            name = "여행",
            bucketlistCount = "20"
        ),
        CategoryInfo(
            id = 1,
            name = "아주아주 맛있는 것을 먹으러 다니는 거야 냠냠쩝쩝 하면서 룰루리랄라 크크루삥봉",
            bucketlistCount = "10"
        ),
        CategoryInfo(
            id = 1,
            name = "제주도여행을 갈거야",
            bucketlistCount = "3"
        ),
        CategoryInfo(
            id = 1,
            name = "제주도여행을 갈거야",
            bucketlistCount = "5"
        ),
        CategoryInfo(
            id = 1,
            name = "검색한 갈거야",
            bucketlistCount = "35"
        )
    )

}