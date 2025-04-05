package com.zinc.waver.ui_more.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.common.utils.TAG
import com.zinc.domain.models.UpdateProfileRequest
import com.zinc.domain.usecases.more.CheckAlreadyUsedNickname
import com.zinc.domain.usecases.more.LoadProfileInfo
import com.zinc.domain.usecases.more.UpdateProfileInfo
import com.zinc.waver.ui.viewmodel.CommonViewModel
import com.zinc.waver.ui_more.models.UIMoreMyProfileInfo
import com.zinc.waver.ui_more.models.toUi
import com.zinc.waver.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MoreViewModel @Inject constructor(
    private val loadProfileInfo: LoadProfileInfo,
    private val updateProfileInfo: UpdateProfileInfo,
    private val checkAlreadyUsedNickname: CheckAlreadyUsedNickname
) : CommonViewModel() {

    private val _profileInfo = MutableLiveData<UIMoreMyProfileInfo>()
    val profileInfo: LiveData<UIMoreMyProfileInfo> get() = _profileInfo

    private val _profileLoadFail = SingleLiveEvent<Boolean>()
    val profileLoadFail: LiveData<Boolean> get() = _profileLoadFail

    private val _profileUpdateFail = SingleLiveEvent<Boolean>()
    val profileUpdateFail: LiveData<Boolean> get() = _profileUpdateFail

    private val _profileUpdateSucceed = SingleLiveEvent<Boolean>()
    val profileUpdateSucceed: LiveData<Boolean> get() = _profileUpdateSucceed

    private val _isAlreadyUsedNickName = SingleLiveEvent<Boolean>()
    val isAlreadyUsedNickName: LiveData<Boolean> get() = _isAlreadyUsedNickName

    fun loadMyProfile() {
        viewModelScope.launch(ceh(_profileLoadFail, true)) {
            loadProfileInfo.invoke().apply {
                Log.e(TAG, "loadMyProfile: $this")
                if (success) {
                    _profileInfo.value = data.toUi()
                } else {
                    _profileLoadFail.value = true
                }
            }
        }
    }

    fun updateMyProfile(name: String, bio: String, profileImage: File? = null) {
        viewModelScope.launch(ceh(_profileUpdateFail, false)) {
            val request = UpdateProfileRequest(
                name = name,
                bio = bio,
                image = profileImage
            )
            updateProfileInfo.invoke(request).apply {
                Log.e(TAG, "loadMyProfile: $this")
                if (success) {
                    _profileUpdateSucceed.value = true
                } else {
                    _profileUpdateFail.value = false
                }
            }
        }
    }

    fun checkIsAlreadyUsedName(name: String) {
        viewModelScope.launch(ceh(_isAlreadyUsedNickName, true)) {
            checkAlreadyUsedNickname.invoke(name).apply {
                Log.e("ayhan", "check Alreay $this")
                if (success) {
                    _isAlreadyUsedNickName.value = false
                } else if (code == "3000") {
                    _isAlreadyUsedNickName.value = true
                } else {
                    _profileUpdateFail.value = false
                }
            }
        }
    }
}