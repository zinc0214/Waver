package com.zinc.berrybucket.ui.presentation.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.berrybucket.ui.viewmodel.CommonViewModel
import com.zinc.berrybucket.util.SingleLiveEvent
import com.zinc.common.models.CreateProfileRequest
import com.zinc.common.utils.TAG
import com.zinc.datastore.login.LoginPreferenceDataStoreModule
import com.zinc.domain.usecases.login.CreateProfile
import com.zinc.domain.usecases.login.LoginByEmail
import com.zinc.domain.usecases.more.CheckAlreadyUsedNickname
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class JoinNickNameViewModel @Inject constructor(
    private val checkAlreadyUsedNickname: CheckAlreadyUsedNickname,
    private val createProfile: CreateProfile,
    private val loginByEmail: LoginByEmail,
    private val loginPreferenceDataStoreModule: LoginPreferenceDataStoreModule,
) : CommonViewModel(loginPreferenceDataStoreModule) {

    private val _isAlreadyUsedNickName = MutableLiveData<Boolean>()
    val isAlreadyUsedNickName: LiveData<Boolean> get() = _isAlreadyUsedNickName

    private val _goToLogin = SingleLiveEvent<Boolean>()
    val goToLogin: LiveData<Boolean> get() = _goToLogin

    private val _failJoin = SingleLiveEvent<Boolean>()
    val failJoin: LiveData<Boolean> get() = _failJoin

    private val _failLogin = SingleLiveEvent<Boolean>()
    val failLogin: LiveData<Boolean> get() = _failLogin

    fun join(
        token: String,
        email: String,
        nickName: String,
        bio: String? = null,
        image: File? = null
    ) {
        viewModelScope.launch(CEH(_failJoin, true)) {
            _failJoin.value = false
            runCatching {
                checkIsAlreadyUsedName(token, email, nickName, bio, image)
            }
        }
    }

    private fun checkIsAlreadyUsedName(
        token: String,
        email: String,
        nickName: String,
        bio: String? = null,
        image: File? = null
    ) {
        viewModelScope.launch(CEH(_failJoin, true)) {
            runCatching {
                _failJoin.value = false
                _isAlreadyUsedNickName.value = false
                checkAlreadyUsedNickname.invoke(token, nickName).apply {
                    Log.e("ayhan", "check Alreay $this")
                    if (success) {
                        createNewProfile(token, email, nickName, bio, image)
                    } else if (code == "3000") {
                        _isAlreadyUsedNickName.value = true
                    } else {
                        _failJoin.value = true
                    }
                }
            }.getOrElse {
                Log.e(TAG, "loadMyProfile2: ${it.message}")
                _failJoin.value = true
            }
        }
    }


    private fun createNewProfile(
        token: String,
        email: String,
        nickName: String,
        bio: String? = null,
        image: File? = null
    ) {
        viewModelScope.launch(CEH(_failJoin, true)) {
            _failJoin.value = false
            _goToLogin.value = false
            runCatching {
                val res = createProfile(
                    "Bearer $token",
                    CreateProfileRequest(name = nickName, bio = bio, profileImage = image)
                )
                Log.e("ayhan", "createNewProfile success : $res /// $token")
                if (res.success) {
                    goToLogin(email)
                } else {
                    _failJoin.value = true
                }
            }.getOrElse {
                Log.e("ayhan", "createNewProfile fatil : $it")
                _failJoin.value = true
            }
        }
    }

    private fun goToLogin(email: String) {
        viewModelScope.launch(CEH(_failLogin, true)) {
            runCatching {
                val res = loginByEmail(email)
                if (res.success) {
                    loginPreferenceDataStoreModule.setLoaginedEmail(email)
                    res.data?.let { token ->
                        loginPreferenceDataStoreModule.setAccessToken("Bearer ${token.accessToken}")
                        loginPreferenceDataStoreModule.setRefreshToken("Bearer ${token.refreshToken}")
                        accessToken.value = "Bearer ${token.accessToken}"
                    }
                    _goToLogin.value = true
                } else {
                    _failLogin.value = true
                }
            }.getOrElse {
                _failLogin.value = true
            }
        }
    }
}