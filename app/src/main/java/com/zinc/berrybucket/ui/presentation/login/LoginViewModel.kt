package com.zinc.berrybucket.ui.presentation.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.berrybucket.ui.viewmodel.CommonViewModel
import com.zinc.berrybucket.util.SingleLiveEvent
import com.zinc.datastore.common.CommonDataStoreModule
import com.zinc.datastore.login.LoginPreferenceDataStoreModule
import com.zinc.domain.usecases.login.CheckEmail
import com.zinc.domain.usecases.login.RefreshUserToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val checkEmail: CheckEmail,
    private val refreshUserToken: RefreshUserToken,
    private val loginPreferenceDataStoreModule: LoginPreferenceDataStoreModule,
    private val commonDataStoreModule: CommonDataStoreModule
) : CommonViewModel(loginPreferenceDataStoreModule) {

    private val _loginFail = SingleLiveEvent<Boolean>()
    val loginFail: LiveData<Boolean> get() = _loginFail

    private val _needToStartJoin = SingleLiveEvent<Boolean>()
    val needToStartJoin: LiveData<Boolean> get() = _needToStartJoin

    private val _needToStartLoadToken = SingleLiveEvent<String>()
    val needToStartLoadToken: LiveData<String> get() = _needToStartLoadToken

    var isLoginChecked = false

//    private val _emailIsAlreadyUsed = SingleLiveEvent<Nothing>()
//    val emailIsAlreadyUsed: LiveData<Nothing> get() = _emailIsAlreadyUsed

//    private val _goToCreateUser = SingleLiveEvent<String>()
//    val goToCreateUser: LiveData<String> get() = _goToCreateUser

    private val _goToMain = MutableLiveData<Boolean>()
    val goToMain: LiveData<Boolean> get() = _goToMain

    fun checkHasLoginEmail() {
        isLoginChecked = true
        viewModelScope.launch {
            loginPreferenceDataStoreModule.loadLoginedEmail.collectLatest {
                if (it.isEmpty()) {
                    _needToStartJoin.value = true
                } else {
                    _needToStartLoadToken.value = it
                }
            }
        }
    }

    fun checkIsLoginedEmail(email: String) {
        _loginFail.value = false
        viewModelScope.launch(CEH(_loginFail, true)) {
            val result = checkEmail(email)
            Log.e("ayhan", "checkIsLoginedEmail result : $result")
            if (result.success) {
                loadUserToken()
            } else {
                _needToStartJoin.value = true
            }
        }
    }

    private fun loadUserToken() {
        _loginFail.value = false
        viewModelScope.launch(CEH(_loginFail, true)) {
            Log.e("ayhan", "loadUserToken result : ${refreshToken.value} , ${accessToken.value}")

            accessToken.value?.let { token ->
                val result = refreshUserToken(token)
                Log.e("ayhan", "loadUserToken result : ${result.data}")
                if (result.success) {
                    val data = result.data
                    accessToken.value = "Bearer ${data?.accessToken}"
                    refreshToken.value = "Bearer ${data?.refreshToken}"
                    loginPreferenceDataStoreModule.setRefreshToken("Bearer ${data?.refreshToken}")
                    loginPreferenceDataStoreModule.setAccessToken("Bearer ${data?.accessToken}")
                    _goToMain.value = true
                } else {
                    _loginFail.value = true
                }
            }
        }
    }

//    fun joinBerryBucket(email: String) {
//        _failJoin.value = false
//        viewModelScope.launch(CEH(_failJoin, true)) {
//            val response = checkIsAvailableEmail(email)
//            if (response.code == "1004") {
//                // 중복된 이메일
//                _emailIsAlreadyUsed.call()
//            } else if (response.success) {
//                // 프로필 생성
//                _goToCreateUser.value = email
//            } else {
//                _failJoin.value = true
//            }
//        }
//
//    }

//    fun joinBerryBucket() {
//        if (accessToken.value.isNullOrEmpty()) {
//            viewModelScope.launch {
//                runCatching {
//                    // TODO : 구글로그인 붙이기
//                    val res = checkIsAvailableEmail.invoke("zinc4@gmail.com")
//                    Log.e("ayhan", "token : ${res}")
//
//                    if (res.success) {
//                        val data = res.data
//                        loginPreferenceDataStoreModule.setAccessToken("Bearer ${data?.accessToken}")
//                        loginPreferenceDataStoreModule.setRefreshToken("Bearer ${data?.refreshToken}")
//                        _joinResponse.value = data?.accessToken
//                    } else {
//                        _failJoin.call()
//                    }
//                }.getOrElse {
//                    Log.e("ayhan", "Fail : $it")
//                    _failJoin.call()
//                }
//            }
//        } else {
//            _canGoToLogin.call()
//        }
//    }

    // TODO : Splash 등으로 옮겨야댐
    fun updateAppVersion(appVersion: String) {
        viewModelScope.launch {
            commonDataStoreModule.setAppVersion(appVersion)
        }
    }
}
