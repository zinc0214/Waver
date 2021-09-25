package com.zinc.mybury_2.presentation.my.viewModel

//@HiltViewModel
//class MyViewModel @Inject constructor(
//    handle: SavedStateHandle,
//    private val loadProfileInfo: LoadProfileInfo
//) : ViewModel() {
//
//    private val _profileInfo = MutableLiveData<TopProfile>()
//    val profileInfo: LiveData<TopProfile> get() = _profileInfo

//    fun loadProfile() {
//        val profileInfoFlow = flowOf(loadProfileInfo)
//        val profileStateFlow = flowOf(loadProfileState)
//        viewModelScope.launch {
//            profileInfoFlow.zip(profileStateFlow) { info, state ->
//                val profileInfo = info.invoke()
//                val stateInfo = state.invoke()
//                val topProfile = TopProfile(
////                    nickName = profileInfo.nickName,
////                    profileImg = profileInfo.profileImg,
////                    badgeType = profileInfo.badgeType,
////                    titlePosition = profileInfo.titlePosition,
////                    bio = profileInfo.bio,
////                    followingCount = stateInfo.followingCount,
////                    followerCount = stateInfo.followerCount
//
//                    nickName = "HANA",
//                    profileImg = "ddd",
//                    badgeType = BadgeType.TRIP1,
//                    titlePosition = "안녕 반가우이",
//                    bio = "나는 ESFP 한아라고 불러줘?",
//                    followingCount = "20",
//                    followerCount = "10"
//                )
//                _profileInfo.value = topProfile
//            }
//        }
//    }
//}