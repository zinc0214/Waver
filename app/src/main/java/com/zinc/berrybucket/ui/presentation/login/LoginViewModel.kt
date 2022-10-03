package com.zinc.berrybucket.ui.presentation.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.berrybucket.CommonViewModel
import com.zinc.datastore.login.LoginPreferenceDataStoreModule
import com.zinc.domain.usecases.lgin.JoinBerryBucket
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val joinBerryBucket: JoinBerryBucket,
    private val loginPreferenceDataStoreModule: LoginPreferenceDataStoreModule
) : CommonViewModel(loginPreferenceDataStoreModule) {

    private val _joinResponse = MutableLiveData<String>()
    val joinResponse: LiveData<String> get() = _joinResponse

    // TODO : go to Nothing & SingleLiveEvent
    private val _failJoin = MutableLiveData<Boolean>()
    val failJoin: LiveData<Boolean> get() = _failJoin

    fun joinBerryBucket() {
        if (accessToken.value.isNullOrEmpty()) {
            viewModelScope.launch {
                runCatching {
                    // TODO : 구글로그인 붙이기
                    val res = joinBerryBucket.invoke("zinc0@gmail.com")

                    _joinResponse.value = res.accessToken
                    loginPreferenceDataStoreModule.setAccessToken(res.accessToken)
                    loginPreferenceDataStoreModule.setRefreshToken(res.refreshToken)

                }.getOrElse {
                    Log.e("ayhan", "Fail : $it")
                    _failJoin.value = true
                }
            }
        } else {
            _failJoin.value = true
        }
    }
}