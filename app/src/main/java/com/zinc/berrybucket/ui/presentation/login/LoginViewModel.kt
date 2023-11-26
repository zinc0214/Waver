package com.zinc.berrybucket.ui.presentation.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.berrybucket.ui.viewmodel.CommonViewModel
import com.zinc.berrybucket.util.SingleLiveEvent
import com.zinc.common.models.CreateProfileRequest
import com.zinc.datastore.common.CommonDataStoreModule
import com.zinc.datastore.login.LoginPreferenceDataStoreModule
import com.zinc.domain.usecases.login.CreateProfile
import com.zinc.domain.usecases.login.JoinBerryBucket
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val joinBerryBucket: JoinBerryBucket,
    private val createProfile: CreateProfile,
    private val loginPreferenceDataStoreModule: LoginPreferenceDataStoreModule,
    private val commonDataStoreModule: CommonDataStoreModule
) : CommonViewModel(loginPreferenceDataStoreModule) {

    private val _joinResponse = MutableLiveData<String>()
    val joinResponse: LiveData<String> get() = _joinResponse

    // TODO : go to Nothing & SingleLiveEvent
    private val _failJoin = MutableLiveData<Boolean>()
    val failJoin: LiveData<Boolean> get() = _failJoin

    private val _canGoToLogin = SingleLiveEvent<Nothing>()
    val canGoToLogin: LiveData<Nothing> get() = _canGoToLogin

    private val _needToEmail = SingleLiveEvent<Nothing>()
    val needToEmail: LiveData<Nothing> get() = _needToEmail

    fun joinBerryBucket() {
        if (accessToken.value.isNullOrEmpty()) {
            viewModelScope.launch {
                runCatching {
                    // TODO : 구글로그인 붙이기
                    val res = joinBerryBucket.invoke("zinc4@gmail.com")
                    Log.e("ayhan", "token : ${res}")

                    if (res.success) {
                        val data = res.data
                        loginPreferenceDataStoreModule.setAccessToken("Bearer ${data?.accessToken}")
                        loginPreferenceDataStoreModule.setRefreshToken("Bearer ${data?.refreshToken}")
                        _joinResponse.value = data?.accessToken
                    } else {
                        _failJoin.value = true
                    }
                }.getOrElse {
                    Log.e("ayhan", "Fail : $it")
                    _failJoin.value = true
                }
            }
        } else {
            _canGoToLogin.call()
        }
    }

    fun createNewProfile(nickName: String, bio: String? = null, image: File? = null) {
        viewModelScope.launch(CEH(_failJoin, true)) {
            accessToken.value?.let { token ->
                runCatching {
                    val res = createProfile(
                        token,
                        CreateProfileRequest(name = nickName, bio = bio, profileImage = image)
                    )
                    if (res.success) {
                        _canGoToLogin.call()
                    } else {
                        _failJoin.value = true
                    }
                }.getOrElse {
                    _failJoin.value = true
                }
            }
        }
    }

    fun loginBerryBucket() {
        // TODO : 이전의 저장된 이메일 값이 있는 경우 (없으면 이메일 등록 하러 가기)
        if (accessToken.value.isNullOrEmpty()) {
            _needToEmail.call()
        } else {

        }

    }

    // TODO : Splash 등으로 옮겨야댐
    fun updateAppVersion(appVersion: String) {
        viewModelScope.launch {
            commonDataStoreModule.setAppVersion(appVersion)
        }
    }
}
