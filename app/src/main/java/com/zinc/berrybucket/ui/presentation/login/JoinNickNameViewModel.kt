package com.zinc.berrybucket.ui.presentation.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.zinc.berrybucket.ui.presentation.login.model.LoginPrevData
import com.zinc.berrybucket.ui.viewmodel.CommonViewModel
import com.zinc.berrybucket.util.SingleLiveEvent
import com.zinc.common.models.CreateProfileRequest
import com.zinc.common.utils.TAG
import com.zinc.datastore.login.LoginPreferenceDataStoreModule
import com.zinc.domain.usecases.login.CreateProfile
import com.zinc.domain.usecases.login.JoinByEmail
import com.zinc.domain.usecases.more.CheckAlreadyUsedNickname
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class JoinNickNameViewModel @Inject constructor(
    private val joinByEmail: JoinByEmail,
    private val checkAlreadyUsedNickname: CheckAlreadyUsedNickname,
    private val createProfile: CreateProfile,
    private val loginPreferenceDataStoreModule: LoginPreferenceDataStoreModule,
) : CommonViewModel(loginPreferenceDataStoreModule) {

    private val _isAlreadyUsedNickName = SingleLiveEvent<Boolean>()
    val isAlreadyUsedNickName: LiveData<Boolean> get() = _isAlreadyUsedNickName

    private val _goToLogin = SingleLiveEvent<Boolean>()
    val goToLogin: LiveData<Boolean> get() = _goToLogin

    private val _goToCreateProfile = SingleLiveEvent<Boolean>()
    val goToCreateProfile: LiveData<Boolean> get() = _goToCreateProfile

    private val _failJoin = SingleLiveEvent<Boolean>()
    val failJoin: LiveData<Boolean> get() = _failJoin

    fun createNewProfile(
        email: String,
        nickName: String,
        bio: String? = null,
        image: File? = null
    ) {
        viewModelScope.launch(CEH(_failJoin, true)) {
            accessToken.value?.let { token ->
                _failJoin.value = false
                runCatching {
                    val res = createProfile(
                        token,
                        CreateProfileRequest(name = nickName, bio = bio, profileImage = image)
                    )
                    if (res.success) {
                        _goToLogin.value = true
                    } else {
                        _failJoin.value = true
                    }
                }.getOrElse {
                    _failJoin.value = true
                }
            }
        }
    }

    fun checkIsAlreadyUsedName(name: String) {
        viewModelScope.launch(CEH(_failJoin, true)) {
            runCatching {
                accessToken.value?.let { token ->
                    _failJoin.value = false
                    checkAlreadyUsedNickname.invoke(token, name).apply {
                        Log.e("ayhan", "check Alreay $this")
                        if (success) {
                            _isAlreadyUsedNickName.value = false
                        } else if (code == "3000") {
                            _isAlreadyUsedNickName.value = true
                        } else {
                            _failJoin.value = true
                        }
                    }
                }
            }.getOrElse {
                Log.e(TAG, "loadMyProfile2: ${it.message}")
                _failJoin.value = true
            }
        }
    }

    fun createUserToken(data: LoginPrevData) {
        viewModelScope.launch {
            loginPreferenceDataStoreModule.setLoaginedEmail(data.email)
            loginPreferenceDataStoreModule.setAccessToken("Bearer ${data.accessToken}")
            loginPreferenceDataStoreModule.setRefreshToken("Bearer ${data.refreshToken}")
            accessToken.value = "Bearer ${data.accessToken}"
        }
    }
}