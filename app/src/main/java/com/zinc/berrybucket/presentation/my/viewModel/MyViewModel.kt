package com.zinc.berrybucket.presentation.my.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zinc.berrybucket.model.BucketInfoSimple
import com.zinc.berrybucket.model.TabType
import com.zinc.data.models.BadgeType
import com.zinc.data.models.Category
import com.zinc.domain.models.TopProfile
import com.zinc.domain.usecases.my.LoadProfileInfo
import com.zinc.domain.usecases.my.LoadProfileState
import dagger.assisted.AssistedFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val loadProfileInfo: LoadProfileInfo,
    private val loadProfileState: LoadProfileState
) : ViewModel() {

    private val _profileInfo = MutableLiveData<TopProfile>()
    val profileInfo: LiveData<TopProfile> get() = _profileInfo

    private val _searchBucketResult = MutableLiveData<Pair<TabType, List<*>>>()
    val searchResult: LiveData<Pair<TabType, List<*>>> get() = _searchBucketResult


    fun loadProfile1() {
        val profileInfoFlow = flowOf(loadProfileInfo)
        val profileStateFlow = flowOf(loadProfileState)
        viewModelScope.launch {
            profileInfoFlow.zip(profileStateFlow) { info, state ->
                val profileInfo = info.invoke()
                val stateInfo = state.invoke()
                val topProfile = TopProfile(
//                    nickName = profileInfo.nickName,
//                    profileImg = profileInfo.profileImg,
//                    badgeType = profileInfo.badgeType,
//                    titlePosition = profileInfo.titlePosition,
//                    bio = profileInfo.bio,
//                    followingCount = stateInfo.followingCount,
//                    followerCount = stateInfo.followerCount

                    nickName = "HANA",
                    profileImg = "ddd",
                    percent = 0.8f,
                    badgeType = BadgeType.TRIP1,
                    titlePosition = "안녕 반가우이",
                    bio = "나는 ESFP 한아라고 불러줘?",
                    followingCount = "20",
                    followerCount = "10"
                )
                _profileInfo.value = topProfile
            }
        }
    }

    fun loadProfile() {
        val topProfile = TopProfile(
            nickName = "HANA",
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

    fun searchList(type: TabType, searchWord: String) {
        when (type) {
            TabType.ALL -> searchAllBucket(searchWord = searchWord)
            TabType.D_DAY -> searchDdayBucket(searchWord = searchWord)
            TabType.CATEGORY -> searchCategoryItems(searchWord = searchWord)
            TabType.CHALLENGE -> searchChallenge(searchWord = searchWord)
        }
    }

    private fun searchAllBucket(searchWord: String) {
        _searchBucketResult.value = Pair(
            TabType.ALL,
            simpleTypeList
        )
    }

    private fun searchDdayBucket(searchWord: String) {
        _searchBucketResult.value = Pair(
            TabType.D_DAY,
            simpleTypeList
        )
    }

    private fun searchCategoryItems(searchWord: String) {
        _searchBucketResult.value = Pair(
            TabType.CATEGORY,
            searchCategory
        )
    }

    private fun searchChallenge(searchWord: String) {
        _searchBucketResult.value = Pair(
            TabType.CHALLENGE,
            simpleTypeList
        )
    }

    private val simpleTypeList = listOf(
        BucketInfoSimple(
            id = "1",
            title = "아이스크림을 먹을테야 얍얍압얍",
            currentCount = 1
        ),
        BucketInfoSimple(
            id = "2",
            title = "아이스크림을 여행을 갈거란 말이야",
            currentCount = 1
        ),
        BucketInfoSimple(
            id = "3",
            title = "Dday가 있는 애22233",
            currentCount = 5,
            goalCount = 10,
            dDay = -10
        ),
        BucketInfoSimple(
            id = "3",
            title = "Dday가 있는 애22233",
            currentCount = 5,
            goalCount = 10,
            dDay = -10
        ),
        BucketInfoSimple(
            id = "3",
            title = "Dday가 있는 애22233",
            currentCount = 5,
            goalCount = 10,
            dDay = -10
        ),
        BucketInfoSimple(
            id = "3",
            title = "Dday가 있는 애22233",
            currentCount = 5,
            goalCount = 10,
            dDay = -10
        )
    )

    private val searchCategory = listOf(
        Category(
            id = 1,
            name = "여행",
            count = "20"
        ),
        Category(
            id = 1,
            name = "아주아주 맛있는 것을 먹으러 다니는 거야 냠냠쩝쩝 하면서 룰루리랄라 크크루삥봉",
            count = "10"
        ),
        Category(
            id = 1,
            name = "제주도여행을 갈거야",
            count = "3"
        ),
        Category(
            id = 1,
            name = "제주도여행을 갈거야",
            count = "5"
        ),
        Category(
            id = 1,
            name = "검색한 갈거야",
            count = "35"
        )
    )

}