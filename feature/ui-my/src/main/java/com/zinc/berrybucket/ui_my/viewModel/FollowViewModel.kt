package com.zinc.berrybucket.ui_my.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.berrybucket.CommonViewModel
import com.zinc.common.models.OtherProfileInfo
import com.zinc.common.models.YesOrNo
import com.zinc.common.utils.cehCommonTitle
import com.zinc.datastore.login.LoginPreferenceDataStoreModule
import com.zinc.domain.usecases.my.LoadFollowList
import com.zinc.domain.usecases.my.RequestFollowUser
import com.zinc.domain.usecases.my.RequestUnfollowUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowViewModel @Inject constructor(
    private val loadFollowList: LoadFollowList,
    private val requestUnfollowUser: RequestUnfollowUser,
    private val requestFollowUser: RequestFollowUser,
    loginPreferenceDataStoreModule: LoginPreferenceDataStoreModule
) : CommonViewModel(loginPreferenceDataStoreModule) {

    private val _followingList = MutableLiveData<List<OtherProfileInfo>>()
    val followingList: LiveData<List<OtherProfileInfo>> get() = _followingList

    private val _followerList = MutableLiveData<List<OtherProfileInfo>>()
    val followerList: LiveData<List<OtherProfileInfo>> get() = _followerList

    private val _loadFail = MutableLiveData<Pair<String?, String?>>()
    val loadFail: LiveData<Pair<String?, String?>> get() = _loadFail

    private val CEH = CoroutineExceptionHandler { coroutineContext, throwable ->
        _loadFail.value = cehCommonTitle to null
    }

    fun loadFollowList() {
        viewModelScope.launch(CEH) {
            accessToken.value?.let { token ->
                runCatching {
                    loadFollowList.invoke(token).apply {
                        Log.e("ayhan", "loadFollowList: $this")
                        _followerList.value =
                            buildList {
                                repeat(40) {
                                    add(
                                        OtherProfileInfo(
                                            id = it.toString(),
                                            imgUrl = null,
                                            name = "$it+가나다라마바사아자차가타파아azbdfdkop+$it",
                                            mutualFollow = if (it < 10) YesOrNo.Y else YesOrNo.N
                                        )
                                    )
                                }
                            }

                        //this.data.followerUsers
                        _followingList.value =

//TODO : 코드제거
//                            buildList {
//                                repeat(40) {
//                                    add(
//                                        OtherProfileInfo(
//                                            id = it.toString(),
//                                            imgUrl = null,
//                                            name = "$it+가나다라마바사아자차가타파아azbdfdkop+$it"
//                                        )
//                                    )
//                                }
//                            }

                            this.data.followingUsers


                    }
                }.getOrElse {
                    _loadFail.value = cehCommonTitle to null
                }
            }
        }
    }

    fun requestUnfollow(unfollowUser: OtherProfileInfo) {
        viewModelScope.launch(CEH) {
            accessToken.value?.let { token ->
                runCatching {
                    requestUnfollowUser.invoke(token, unfollowUser.id).apply {
                        Log.e("ayhan", "response : $this")
                        if (success) {
                            val removeIndex =
                                _followingList.value?.indexOfFirst { it == unfollowUser } ?: -1
                            if (removeIndex > -1) {
                                val list = _followingList.value?.toMutableList()
                                list?.removeAt(removeIndex)
                                _followingList.value = list
                            }
                        }
                    }
                }.getOrElse {
                    _loadFail.value = cehCommonTitle to null
                }
            }
        }
    }

    fun requestFollow(followUser: OtherProfileInfo) {
        viewModelScope.launch(CEH) {
            accessToken.value?.let { token ->
                runCatching {
                    requestFollowUser.invoke(token, followUser.id).apply {
                        if (success) {
                            val updateList = _followerList.value?.toMutableList()
                            val updateUser = followUser.copy(mutualFollow = YesOrNo.Y)
                            updateList?.replaceAll {
                                if (it.id == updateUser.id) {
                                    updateUser
                                } else {
                                    it
                                }
                            }
                            _followerList.value = updateList
                        }
                    }
                }.getOrElse {
                    _loadFail.value = "팔로우 목록 로드 실패" to "다시 시도해주세요"
                }
            }
        }
    }
}