package com.zinc.waver.ui.presentation.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.common.models.CreateProfileRequest
import com.zinc.datastore.login.PreferenceDataStoreModule
import com.zinc.domain.models.GoogleEmailInfo
import com.zinc.domain.usecases.login.CreateProfile
import com.zinc.domain.usecases.login.LoginByEmail
import com.zinc.domain.usecases.more.CheckAlreadyUsedNickname
import com.zinc.waver.ui.viewmodel.CommonViewModel
import com.zinc.waver.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class JoinNickNameViewModel @Inject constructor(
    private val createProfile: CreateProfile,
    private val loginByEmail: LoginByEmail,
    private val checkAlreadyUsedNickname: CheckAlreadyUsedNickname,
    private val preferenceDataStoreModule: PreferenceDataStoreModule,
) : CommonViewModel() {

    private val _isAlreadyUsedNickName = MutableLiveData<Boolean?>()
    val isAlreadyUsedNickName: LiveData<Boolean?> get() = _isAlreadyUsedNickName

    private val _goToLogin = SingleLiveEvent<Boolean>()
    val goToLogin: LiveData<Boolean> get() = _goToLogin

    private val _failJoin = SingleLiveEvent<Boolean>()
    val failJoin: LiveData<Boolean> get() = _failJoin

    private val _failCheckNickname = MutableLiveData<Boolean>()
    val failCheckNickname: LiveData<Boolean> get() = _failCheckNickname

    fun join(
        emailInfo: GoogleEmailInfo,
        nickName: String,
        bio: String? = null,
        image: File? = null
    ) {
        viewModelScope.launch(ceh(_failJoin, true)) {
            _failJoin.value = false
            createNewProfile(emailInfo, nickName, bio, image)
        }
    }

    fun checkIsAlreadyUsedName(name: String) {
        viewModelScope.launch(ceh(_failCheckNickname, true)) {
            checkAlreadyUsedNickname.invoke(name).apply {
                _isAlreadyUsedNickName.value = null
                delay(100)
                Log.e("ayhan", "check Alreay $this")
                if (success) {
                    _isAlreadyUsedNickName.value = false
                } else if (code == "6001") {
                    _isAlreadyUsedNickName.value = true
                } else {
                    _failCheckNickname.value = true
                }

            }
        }
    }

    private fun createNewProfile(
        emailInfo: GoogleEmailInfo,
        nickName: String,
        bio: String? = null,
        image: File? = null
    ) {
        Log.e("ayhan", "createNewProfile called :$emailInfo")
        viewModelScope.launch(ceh(_failJoin, true)) {
            _failJoin.value = false
            _goToLogin.value = false

            val res = createProfile(
                CreateProfileRequest(
                    email = emailInfo.email,
                    uid = emailInfo.uid,
                    name = nickName,
                    bio = bio,
                    profileImage = image
                )
            )
            Log.e("ayhan", "createNewProfile success : $res")
            if (res.code == "6001") {
                _isAlreadyUsedNickName.value = true
            } else if (res.success) {
                goToLogin(emailInfo)
            } else {
                Log.e("ayhan", "createNewProfile fail : $res")
                _failJoin.value = true
            }
        }
    }

    private fun goToLogin(emailInfo: GoogleEmailInfo) {
        viewModelScope.launch(ceh(_failJoin, true)) {
            val res = loginByEmail(emailInfo.uid)
            if (res.success) {
                preferenceDataStoreModule.setLoginEmail(emailInfo.email)
                preferenceDataStoreModule.setLoginEmailUid(emailInfo.uid)
                res.data.accessToken.let { token ->
                    preferenceDataStoreModule.setAccessToken("Bearer $token")
                }
                _goToLogin.value = true
            } else {
                _failJoin.value = true
            }
        }
    }
}