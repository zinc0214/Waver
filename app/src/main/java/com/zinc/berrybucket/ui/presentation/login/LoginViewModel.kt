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
import com.zinc.domain.usecases.login.LoginByEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val checkEmail: CheckEmail,
    private val loginByEmail: LoginByEmail,
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

    private val _goToMain = MutableLiveData<Boolean>()
    val goToMain: LiveData<Boolean> get() = _goToMain

    fun checkHasLoginEmail() {
        isLoginChecked = true
        viewModelScope.launch {
            loginPreferenceDataStoreModule.loadLoginedEmail.collectLatest {
                Log.e("ayhan", "checkHasLoginEmail : $it")
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
                loadLoginToken(email)
                loginPreferenceDataStoreModule.setUserIdKey(result.data.userId.toString())
            } else {
                _needToStartJoin.value = true
            }
        }
    }

    private fun loadLoginToken(email: String) {
        _loginFail.value = false
        viewModelScope.launch(CEH(_loginFail, true)) {
            Log.e("ayhan", "loadUserToken result : ${refreshToken.value} , ${accessToken.value}")

            val result = loginByEmail(email)
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

    // TODO : Splash 등으로 옮겨야댐
    fun updateAppVersion(appVersion: String) {
        viewModelScope.launch {
            commonDataStoreModule.setAppVersion(appVersion)
        }
    }
}
