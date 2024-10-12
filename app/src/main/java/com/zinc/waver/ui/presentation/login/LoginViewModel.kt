package com.zinc.waver.ui.presentation.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.datastore.login.LoginPreferenceDataStoreModule
import com.zinc.domain.usecases.login.LoginByEmail
import com.zinc.waver.ui.viewmodel.CommonViewModel
import com.zinc.waver.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginByEmail: LoginByEmail,
    private val loginPreferenceDataStoreModule: LoginPreferenceDataStoreModule,
) : CommonViewModel() {

    private val _loginFail = SingleLiveEvent<Boolean>()
    val loginFail: LiveData<Boolean> get() = _loginFail

    private val _needToStartJoin = SingleLiveEvent<Boolean>()
    val needToStartJoin: LiveData<Boolean> get() = _needToStartJoin

    private val _needToStartLoadToken = SingleLiveEvent<String>()
    val needToStartLoadToken: LiveData<String> get() = _needToStartLoadToken

    var isLoginChecked = false

    private val _goToMain = MutableLiveData<Boolean>()
    val goToMain: LiveData<Boolean> get() = _goToMain

    fun checkHasLoginEmail(retryEmail: String) {
        isLoginChecked = true
        _needToStartJoin.value = false
        viewModelScope.launch {
            loginPreferenceDataStoreModule.loadLoginedEmail.collectLatest {
                Log.e("ayhan", "checkHasLoginEmail : $it")
                if (it.isNotEmpty()) {
                    _needToStartLoadToken.value = it
                    _needToStartJoin.value = false
                } else if (retryEmail.isNotEmpty()) {
                    _needToStartLoadToken.value = it
                    _needToStartJoin.value = false
                } else {
                    _needToStartJoin.value = true
                }
            }
        }
    }

    fun loadLoginToken(email: String) {
        _loginFail.value = false
        viewModelScope.launch(ceh(_loginFail, true)) {
            val result = loginByEmail(email)
            Log.e("ayhan", "loadUserToken result : ${result.data}")
            if (result.success) {
                val data = result.data
                loginPreferenceDataStoreModule.setRefreshToken("Bearer ${data?.refreshToken}")
                loginPreferenceDataStoreModule.setAccessToken("Bearer ${data?.accessToken}")
                loginPreferenceDataStoreModule.setLoginEmail(email)
                _goToMain.value = true
            } else {
                _loginFail.value = true
            }
        }
    }
}
