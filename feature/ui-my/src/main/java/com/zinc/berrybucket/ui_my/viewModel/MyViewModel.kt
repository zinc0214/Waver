package com.zinc.berrybucket.ui_my.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.berrybucket.CommonViewModel
import com.zinc.berrybucket.model.AllBucketList
import com.zinc.berrybucket.model.MyTabType
import com.zinc.berrybucket.model.parseToUI
import com.zinc.common.models.AllBucketListRequest
import com.zinc.common.models.BadgeType
import com.zinc.common.models.BucketInfoSimple
import com.zinc.common.models.BucketStatus
import com.zinc.common.models.CategoryInfo
import com.zinc.common.models.DdayBucketList
import com.zinc.datastore.login.LoginPreferenceDataStoreModule
import com.zinc.domain.models.TopProfile
import com.zinc.domain.usecases.my.LoadAllBucketList
import com.zinc.domain.usecases.my.LoadCategoryList
import com.zinc.domain.usecases.my.LoadDdayBucketList
import com.zinc.domain.usecases.my.LoadProfileInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val loadProfileInfo: LoadProfileInfo,
    private val loadAllBucketList: LoadAllBucketList,
    private val loadCategoryList: LoadCategoryList,
    private val loadDdayBucketList: LoadDdayBucketList,
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


    fun loadProfile() {
        viewModelScope.launch {
            runCatching {
                accessToken.value?.let { token ->
                    Log.e("ayhan", "token : $token")

                    val profileInfo = loadProfileInfo.invoke(token)
                    Log.e("ayhan", "tokgen : $profileInfo")

                    val topProfile = TopProfile(
                        nickName = profileInfo.name,
                        profileImg = profileInfo.imgUrl,
                        badgeType = profileInfo.badgeType,
                        titlePosition = profileInfo.badgeTitle,
                        bio = profileInfo.bio,
                        followingCount = profileInfo.followingCount,
                        followerCount = profileInfo.followerCount,
                        percent = profileInfo.bucketInfo.grade()
                    )
                    _profileInfo.value = topProfile
                }
            }.getOrElse {
                Log.e("ayhan", "Fail Load Profile : $it")
                loadDummyProfile()
            }
        }
    }

    private fun loadDummyProfile() {
        val topProfile = TopProfile(
            nickName = "한아로해봐",
            profileImg = "ddd",
            percent = 0.6f,
            badgeType = BadgeType.TRIP1,
            titlePosition = "안녕 반가우이잇",
            bio = "나는 ESFP 한아라고 불러줘?",
            followingCount = "20",
            followerCount = "10"
        )
        _profileInfo.value = topProfile
    }

    fun loadAllBucketList(allBucketListRequest: AllBucketListRequest) {
        viewModelScope.launch {
            runCatching {
                accessToken.value?.let { token ->
                    loadAllBucketList.invoke(
                        token,
                        allBucketListRequest
                    ).apply {
                        Log.e("ayhan", "allBucketList : $this")
                        val uiALlBucketType = AllBucketList(
                            processingCount = this.processingCount,
                            succeedCount = this.succeedCount,
                            bucketList = this.bucketList.parseToUI()
                        )
                        _allBucketItem.value = uiALlBucketType
                    }
                }
            }.getOrElse {

            }
        }
    }

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
            MyTabType.ALL -> searchAllBucket(searchWord = searchWord)
            MyTabType.DDAY -> searchDdayBucket(searchWord = searchWord)
            MyTabType.CATEGORY -> searchCategoryItems(searchWord = searchWord)
            MyTabType.CHALLENGE -> searchChallenge(searchWord = searchWord)
        }
    }

    private fun searchAllBucket(searchWord: String) {
        _searchBucketResult.value = Pair(
            MyTabType.ALL,
            simpleTypeList
        )
    }

    private fun searchDdayBucket(searchWord: String) {
        _searchBucketResult.value = Pair(
            MyTabType.DDAY,
            simpleTypeList
        )
    }

    private fun searchCategoryItems(searchWord: String) {
        _searchBucketResult.value = Pair(
            MyTabType.CATEGORY,
            searchCategoryInfos
        )
    }

    private fun searchChallenge(searchWord: String) {
        _searchBucketResult.value = Pair(
            MyTabType.CHALLENGE,
            simpleTypeList
        )
    }

    private val simpleTypeList = listOf(
        BucketInfoSimple(
            id = "1",
            title = "아이스크림을 먹을테야 얍얍압얍",
            currentCount = 1,
            status = BucketStatus.PROGRESS
        ),
        BucketInfoSimple(
            id = "2",
            title = "아이스크림을 여행을 갈거란 말이야",
            currentCount = 1,
            status = BucketStatus.PROGRESS
        ),
        BucketInfoSimple(
            id = "3",
            title = "Dday가 있는 애22233",
            currentCount = 5,
            goalCount = 10,
            dDay = -10,
            status = BucketStatus.PROGRESS
        ),
        BucketInfoSimple(
            id = "3",
            title = "Dday가 있는 애22233",
            currentCount = 5,
            goalCount = 10,
            dDay = -10,
            status = BucketStatus.PROGRESS
        ),
        BucketInfoSimple(
            id = "3",
            title = "Dday가 있는 애22233",
            currentCount = 5,
            goalCount = 10,
            dDay = -10,
            status = BucketStatus.PROGRESS

        ),
        BucketInfoSimple(
            id = "3",
            title = "Dday가 있는 애22233",
            currentCount = 5,
            goalCount = 10,
            dDay = -10,
            status = BucketStatus.PROGRESS
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