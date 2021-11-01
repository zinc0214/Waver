package com.zinc.berrybucket.presentation.my.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zinc.data.models.BadgeType
import com.zinc.domain.models.TopProfile
import com.zinc.domain.usecases.my.LoadProfileInfo
import com.zinc.domain.usecases.my.LoadProfileState
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
            badgeType = BadgeType.TRIP1,
            titlePosition = "안녕 반가우이",
            bio = "나는 ESFP 한아라고 불러줘?",
            followingCount = "20",
            followerCount = "10"
        )
        _profileInfo.value = topProfile
    }

}