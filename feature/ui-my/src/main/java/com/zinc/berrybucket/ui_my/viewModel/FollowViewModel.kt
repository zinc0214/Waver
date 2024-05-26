package com.zinc.berrybucket.ui_my.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.berrybucket.ui.viewmodel.CommonViewModel
import com.zinc.common.models.OtherProfileInfo
import com.zinc.common.utils.cehCommonTitle
import com.zinc.domain.usecases.my.LoadFollowList
import com.zinc.domain.usecases.other.RequestFollowUser
import com.zinc.domain.usecases.other.RequestUnfollowUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowViewModel @Inject constructor(
    private val loadFollowList: LoadFollowList,
    private val requestUnfollowUser: RequestUnfollowUser,
    private val requestFollowUser: RequestFollowUser
) : CommonViewModel() {

    private val _followingList = MutableLiveData<List<OtherProfileInfo>>()
    val followingList: LiveData<List<OtherProfileInfo>> get() = _followingList

    private val _followerList = MutableLiveData<List<OtherProfileInfo>>()
    val followerList: LiveData<List<OtherProfileInfo>> get() = _followerList

    private val _loadFail = MutableLiveData<Pair<String?, String?>>()
    val loadFail: LiveData<Pair<String?, String?>> get() = _loadFail

    private val CEH = CoroutineExceptionHandler { _, throwable ->
        Log.e("ayhan", "loadFollowList fail: $throwable")
        _loadFail.value = cehCommonTitle to null
    }

    fun loadFollowList() {
        viewModelScope.launch(CEH) {
            runCatching {
                loadFollowList.invoke().apply {
                    Log.e("ayhan", "loadFollowList: $this")
                    _followerList.value = this.data.followerUsers
                }
            }.getOrElse {
                Log.e("ayhan", "loadFollowList fail2: ${it.message}")
                _loadFail.value = cehCommonTitle to null
            }
        }
    }

    fun loadFollowingList() {
        viewModelScope.launch(CEH) {
            runCatching {
                loadFollowList.invoke().apply {
                    Log.e("ayhan", "loadFollowList: $this")
                    _followingList.value = this.data.followingUsers
                }
            }.getOrElse {
                Log.e("ayhan", "loadFollowList fail2: ${it.message}")
                _loadFail.value = cehCommonTitle to null
            }
        }
    }

    fun requestUnfollow(unfollowUser: OtherProfileInfo) {
        viewModelScope.launch(CEH) {
            runCatching {
                requestUnfollowUser.invoke(unfollowUser.id).apply {
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

    fun requestFollow(followUser: OtherProfileInfo) {
        viewModelScope.launch(CEH) {
            requestFollowUser.invoke(followUser.id).apply {
                if (success) {
                    val updateList = _followerList.value?.toMutableList()
                    val updateUser = followUser.copy(mutualFollow = true)
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
        }
    }
}