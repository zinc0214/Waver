package com.zinc.berrybucket.ui_more.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.berrybucket.CommonViewModel
import com.zinc.berrybucket.ui_more.models.UIMoreMyProfileInfo
import com.zinc.berrybucket.ui_more.models.toUi
import com.zinc.berrybucket.util.SingleLiveEvent
import com.zinc.common.utils.TAG
import com.zinc.datastore.login.LoginPreferenceDataStoreModule
import com.zinc.domain.models.UpdateProfileRequest
import com.zinc.domain.usecases.more.CheckAlreadyUsedNickname
import com.zinc.domain.usecases.more.LoadProfileInfo
import com.zinc.domain.usecases.more.UpdateProfileInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MoreViewModel @Inject constructor(
    private val loadProfileInfo: LoadProfileInfo,
    private val updateProfileInfo: UpdateProfileInfo,
    private val checkAlreadyUsedNickname: CheckAlreadyUsedNickname,
    loginPreferenceDataStoreModule: LoginPreferenceDataStoreModule
) : CommonViewModel(loginPreferenceDataStoreModule) {

    private fun CEH(event: SingleLiveEvent<Boolean>, value: Boolean) =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.e(TAG, "loadMyProfile: $throwable")
            event.value = value
        }

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
        viewModelScope.launch(CEH(_profileLoadFail, true)) {
            runCatching {
                accessToken.value?.let { token ->
                    Log.e(TAG, "loadMyProfile1: $token")
                    loadProfileInfo.invoke(token).apply {
                        Log.e(TAG, "loadMyProfile: $this")
                        if (success) {
                            _profileInfo.value = data.toUi()
                        } else {
                            _profileLoadFail.value = true
                        }
                    }
                }
            }.getOrElse {
                Log.e(TAG, "loadMyProfile2: ${it.message}")
                _profileLoadFail.value = true
            }
        }
    }

    fun updateMyProfile(name: String, bio: String, profileImage: File? = null) {
        viewModelScope.launch(CEH(_profileUpdateFail, false)) {
            runCatching {
                accessToken.value?.let { token ->
                    val request = UpdateProfileRequest(
                        name = name,
                        bio = bio,
                        image = profileImage
                    )
                    updateProfileInfo.invoke(token, request).apply {
                        Log.e(TAG, "loadMyProfile: $this")
                        if (success) {
                            _profileUpdateSucceed.value = true
                        } else {
                            _profileUpdateFail.value = false
                        }
                    }
                }
            }.getOrElse {
                Log.e(TAG, "loadMyProfile2: ${it.message}")
                _profileUpdateFail.value = false
            }
        }
    }

    fun checkIsAlreadyUsedName(name: String) {
        viewModelScope.launch(CEH(_isAlreadyUsedNickName, true)) {
            runCatching {
                accessToken.value?.let { token ->
                    checkAlreadyUsedNickname.invoke(token, name).apply {
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
            }.getOrElse {
                Log.e(TAG, "loadMyProfile2: ${it.message}")
                _isAlreadyUsedNickName.value = true
            }
        }
    }
}