package com.zinc.berrybucket.ui.presentation.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.berrybucket.ui.presentation.CommonViewModel
import com.zinc.common.models.JoinResponse
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

    private val _joinResponse = MutableLiveData<JoinResponse>()
    val joinResponse: LiveData<JoinResponse> get() = _joinResponse

    fun joinBerryBucket() {
        viewModelScope.launch {
            runCatching {
                joinBerryBucket.invoke("zinc1@gmail.com").apply {
                    _joinResponse.value = this
                    loginPreferenceDataStoreModule.setAccessToken(this.accessToken)
                    loginPreferenceDataStoreModule.setRefreshToken(this.refreshToken)
                }
            }.getOrElse {
                Log.e("ayhan", "Fail : $it")
            }
        }
    }
}