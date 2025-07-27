package com.zinc.waver.ui_other.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.common.models.OtherProfileHomeData
import com.zinc.domain.usecases.other.LoadOtherInfo
import com.zinc.domain.usecases.other.RequestFollowUser
import com.zinc.domain.usecases.other.RequestUnfollowUser
import com.zinc.waver.ui.viewmodel.CommonViewModel
import com.zinc.waver.ui_other.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OtherViewModel @Inject constructor(
    private val loadOtherInfo: LoadOtherInfo,
    private val followUser: RequestFollowUser,
    private val unfollowUser: RequestUnfollowUser
) : CommonViewModel() {

    private val _loadFail = MutableLiveData<Boolean>()
    val loadFail: LiveData<Boolean> get() = _loadFail

    private val _otherHomeData = MutableLiveData<OtherProfileHomeData>()
    val otherHomeData: LiveData<OtherProfileHomeData> get() = _otherHomeData

    fun loadOtherInfo(userId: String) {
        Log.e("ayhan", "loadOther : userId : $userId")
        _loadFail.value = false
        viewModelScope.launch(ceh(_loadFail, true)) {
            val result = loadOtherInfo.invoke(userId)
            Log.e("ayhan", "loadOtherInfo : $result")
            if (result.success) {
                _otherHomeData.value = result.data.toUiModel()
            } else {
                _loadFail.value = true
            }
        }
    }

    fun changeFollowStatus(userId: String, follow: Boolean) {
        viewModelScope.launch(ceh(_loadFail, true)) {
            Log.e("ayhan", "changeFollowStatus 1 result : $follow. $userId")

            if (follow) {
                val result = followUser.invoke(userId)

                Log.e("ayhan", "changeFollowStatus 2 result : $result")

                if (result.success) {
                    loadOtherInfo(userId)
                    _loadFail.value = false
                } else {
                    _loadFail.value = true
                }
            } else {
                val result = unfollowUser.invoke(userId)
                if (result.success) {
                    loadOtherInfo(userId)
                    _loadFail.value = false
                } else {
                    _loadFail.value = true
                }
            }
        }
    }
}