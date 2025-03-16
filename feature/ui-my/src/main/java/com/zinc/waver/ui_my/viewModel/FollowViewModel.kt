package com.zinc.waver.ui_my.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.common.models.OtherProfileInfo
import com.zinc.common.utils.cehCommonTitle
import com.zinc.domain.usecases.my.LoadFollowList
import com.zinc.domain.usecases.other.RequestBlockUser
import com.zinc.domain.usecases.other.RequestFollowUser
import com.zinc.domain.usecases.other.RequestUnfollowUser
import com.zinc.waver.ui.viewmodel.CommonViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowViewModel @Inject constructor(
    private val loadFollowList: LoadFollowList,
    private val requestUnfollowUser: RequestUnfollowUser,
    private val requestFollowUser: RequestFollowUser,
    private val requestBlockUser: RequestBlockUser
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
            loadFollowList.invoke().apply {
                Log.e("ayhan", "loadFollowList: $this")
                if (this.success) {
                    _followerList.value = this.data.followerUsers
                } else {
                    _loadFail.value = cehCommonTitle to null
                }
            }
        }
    }

    fun loadFollowingList() {
        viewModelScope.launch(CEH) {
            loadFollowList.invoke().apply {
                Log.e("ayhan", "loadFollowList: $this")
                if (this.success) {
                    _followingList.value = this.data.followingUsers
                } else {
                    _loadFail.value = cehCommonTitle to null
                }
            }
        }
    }

    fun requestUnfollow(unfollowUser: OtherProfileInfo) {
        viewModelScope.launch(CEH) {
            requestUnfollowUser.invoke(unfollowUser.id).apply {
                Log.e("ayhan", "response : $this")
                if (success) {
                    loadFollowingList()
                } else {
                    _loadFail.value = cehCommonTitle to null
                }
            }
        }
    }


    fun requestUserBlock(blockUser: OtherProfileInfo) {
        viewModelScope.launch(CEH) {
            requestBlockUser.invoke(blockUser.id).apply {
                Log.e("ayhan", "response : $this")
                if (success) {
                    loadFollowList()
                } else {
                    _loadFail.value = cehCommonTitle to null
                }
            }
        }
    }

    fun requestFollow(followUser: OtherProfileInfo) {
        viewModelScope.launch(CEH) {
            requestFollowUser.invoke(followUser.id).apply {
                if (success) {
                    loadFollowList()
                } else {
                    _loadFail.value = cehCommonTitle to null
                }
            }
        }
    }
}