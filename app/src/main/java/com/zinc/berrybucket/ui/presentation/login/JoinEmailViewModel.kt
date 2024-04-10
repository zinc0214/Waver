package com.zinc.berrybucket.ui.presentation.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.zinc.berrybucket.ui.viewmodel.CommonViewModel
import com.zinc.berrybucket.util.SingleLiveEvent
import com.zinc.datastore.login.LoginPreferenceDataStoreModule
import com.zinc.domain.usecases.login.JoinByEmail
import com.zinc.domain.usecases.login.LoginByEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinEmailViewModel @Inject constructor(
    private val joinByEmail: JoinByEmail,
    private val loginByEmail: LoginByEmail,
    private val loginPreferenceDataStoreModule: LoginPreferenceDataStoreModule,
) : CommonViewModel(loginPreferenceDataStoreModule) {

    private val _failEmailCheck = SingleLiveEvent<Boolean>()
    val failEmailCheck: LiveData<Boolean> get() = _failEmailCheck

    private val _goToMakeNickName = SingleLiveEvent<Pair<String, String>>()
    val goToMakeNickName: LiveData<Pair<String, String>> get() = _goToMakeNickName

    private val _isAlreadyUsedEmail = SingleLiveEvent<Boolean>()
    val isAlreadyUsedEmail: LiveData<Boolean> get() = _isAlreadyUsedEmail

    fun checkEmailValid(
        email: String
    ) {
        viewModelScope.launch(CEH(_failEmailCheck, true)) {
            runCatching {
                val res = joinByEmail(email)
                Log.e("ayhan", "checkEmailValid  :$res")
                if (res.code == "1004") {
                    // 존재하는 이메일
                    _isAlreadyUsedEmail.value = true
                    _failEmailCheck.value = false
                } else if (res.success && res.code == "0000") {
                    // 존재하지 않는 이메일
                    _goToMakeNickName.value =
                        res.data?.accessToken.orEmpty() to res.data?.refreshToken.orEmpty()
                    _failEmailCheck.value = false
                } else {
                    // api 실패
                    _failEmailCheck.value = true
                }
            }.getOrElse {
                _failEmailCheck.value = true
            }
        }
    }

    fun goToLogin(email: String) {
        viewModelScope.launch(CEH(_failEmailCheck, true)) {
            runCatching {
                val res = loginByEmail(email)
                if (res.success) {
                    loginPreferenceDataStoreModule.setLoaginedEmail(email)
                    res.data?.let { token ->
                        loginPreferenceDataStoreModule.setAccessToken("Bearer ${token.accessToken}")
                        loginPreferenceDataStoreModule.setRefreshToken("Bearer ${token.refreshToken}")
                        accessToken.value = "Bearer ${token.accessToken}"
                    }
                    _failEmailCheck.value = false
                } else {
                    _failEmailCheck.value = true
                }
            }.getOrElse {
                _failEmailCheck.value = true
            }
        }
    }
}