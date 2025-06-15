package com.zinc.waver.ui.presentation.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.common.models.CreateProfileRequest
import com.zinc.datastore.login.PreferenceDataStoreModule
import com.zinc.domain.usecases.login.CreateProfile
import com.zinc.domain.usecases.login.LoginByEmail
import com.zinc.domain.usecases.more.CheckAlreadyUsedNickname
import com.zinc.waver.ui.viewmodel.CommonViewModel
import com.zinc.waver.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private val _isAlreadyUsedNickName = MutableLiveData<Boolean>()
    val isAlreadyUsedNickName: LiveData<Boolean> get() = _isAlreadyUsedNickName

    private val _goToLogin = SingleLiveEvent<Boolean>()
    val goToLogin: LiveData<Boolean> get() = _goToLogin

    private val _failJoin = SingleLiveEvent<Boolean>()
    val failJoin: LiveData<Boolean> get() = _failJoin

    private val _failCheckNickname = MutableLiveData<Boolean>()
    val failCheckNickname: LiveData<Boolean> get() = _failCheckNickname

    fun join(
        email: String,
        nickName: String,
        bio: String? = null,
        image: File? = null
    ) {
        viewModelScope.launch(ceh(_failJoin, true)) {
            _failJoin.value = false
            runCatching {
                createNewProfile(email, nickName, bio, image)
            }
        }
    }

    fun checkIsAlreadyUsedName(name: String) {
        _isAlreadyUsedNickName.value = true

        viewModelScope.launch(ceh(_failCheckNickname, true)) {
            checkAlreadyUsedNickname.invoke(name).apply {
                _failCheckNickname.value = false
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
        email: String,
        nickName: String,
        bio: String? = null,
        image: File? = null
    ) {
        viewModelScope.launch(ceh(_failJoin, true)) {
            _failJoin.value = false
            _goToLogin.value = false
            runCatching {
                val res = createProfile(
                    CreateProfileRequest(
                        email = email,
                        name = nickName,
                        bio = bio,
                        profileImage = image
                    )
                )
                Log.e("ayhan", "createNewProfile success : $res")
                if (res.success) {
                    goToLogin(email)
                } else if (res.code == "3000") {
                    _isAlreadyUsedNickName.value = true
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
        viewModelScope.launch(ceh(_failJoin, true)) {
            runCatching {
                val res = loginByEmail(email)
                if (res.success) {
                    preferenceDataStoreModule.setLoginEmail(email)
                    res.data.accessToken.let { token ->
                        preferenceDataStoreModule.setAccessToken("Bearer $token")
                    }
                    _goToLogin.value = true
                } else {
                    _failJoin.value = true
                }
            }.getOrElse {
                _failJoin.value = true
            }
        }
    }
}