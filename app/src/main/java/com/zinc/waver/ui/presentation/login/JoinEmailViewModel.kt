package com.zinc.waver.ui.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.common.models.CreateProfileRequest
import com.zinc.datastore.login.PreferenceDataStoreModule
import com.zinc.domain.models.GoogleEmailInfo
import com.zinc.domain.usecases.login.CreateProfile
import com.zinc.domain.usecases.login.LoginByEmail
import com.zinc.waver.ui.viewmodel.CommonViewModel
import com.zinc.waver.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinEmailViewModel @Inject constructor(
    private val loginByEmail: LoginByEmail,
    private val createProfile: CreateProfile,
    private val preferenceDataStoreModule: PreferenceDataStoreModule,
) : CommonViewModel() {

    private val _failEmailCheck = SingleLiveEvent<Boolean>()
    val failEmailCheck: LiveData<Boolean> get() = _failEmailCheck

    private val _goToMakeNickName = SingleLiveEvent<GoogleEmailInfo>()
    val goToMakeNickName: LiveData<GoogleEmailInfo> get() = _goToMakeNickName

    private val _isAlreadyUsedEmail = SingleLiveEvent<Boolean>()
    val isAlreadyUsedEmail: LiveData<Boolean> get() = _isAlreadyUsedEmail

    private val _goToLogin = MutableLiveData<Boolean>()
    val goToLogin: LiveData<Boolean> get() = _goToLogin

    fun goToLogin(emailInfo: GoogleEmailInfo) {
        viewModelScope.launch(ceh(_failEmailCheck, true)) {
            _failEmailCheck.value = false
            val res = loginByEmail(emailInfo.uid)
            if (res.success) {
                res.data.accessToken.let { token ->
                    preferenceDataStoreModule.setAccessToken("Bearer $token")
                }
                _isAlreadyUsedEmail.value = true
            } else {
                _goToMakeNickName.value = emailInfo
            }
        }
    }

    // TODO: 삭제된 이메일인지 확인
    private fun checkAlreadyDeleteEmail(emailInfo: GoogleEmailInfo) {
        viewModelScope.launch(ceh(_failEmailCheck, true)) {
            _failEmailCheck.value = false
            val res = createProfile(
                CreateProfileRequest(
                    uid = emailInfo.uid,
                    email = emailInfo.email,
                    name = "이메일계정확인",
                    bio = "빈값전달",
                    profileImage = null,
                )
            )
        }
    }

    fun savedLoginEmail(emailInfo: GoogleEmailInfo) {
        viewModelScope.launch {
            preferenceDataStoreModule.setLoginEmail(emailInfo.email)
            preferenceDataStoreModule.setLoginEmailUid(emailInfo.uid)
            _goToLogin.value = true
        }
    }
}