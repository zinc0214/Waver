package com.zinc.berrybucket.ui_more.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.berrybucket.CommonViewModel
import com.zinc.berrybucket.ui_more.models.UIMoreMyProfileInfo
import com.zinc.berrybucket.ui_more.models.toUi
import com.zinc.berrybucket.util.SingleLiveEvent
import com.zinc.common.utils.TAG
import com.zinc.datastore.login.LoginPreferenceDataStoreModule
import com.zinc.domain.usecases.more.LoadProfileInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoreViewModel @Inject constructor(
    private val loadProfileInfo: LoadProfileInfo,
    loginPreferenceDataStoreModule: LoginPreferenceDataStoreModule
) : CommonViewModel(loginPreferenceDataStoreModule) {

    private val CEH = CoroutineExceptionHandler { coroutineContext, throwable ->
        _profileLoadFail.call()
    }

    private val _profileInfo = MutableLiveData<UIMoreMyProfileInfo>()
    val profileInfo: LiveData<UIMoreMyProfileInfo> get() = _profileInfo

    private val _profileLoadFail = SingleLiveEvent<Nothing>()
    val profileLoadFail: LiveData<Nothing> get() = _profileLoadFail

    fun loadMyProfile() {
        Log.e(TAG, "loadMyProfile: $1111")

        viewModelScope.launch(CEH) {
            runCatching {
                accessToken.value?.let { token ->
                    loadProfileInfo.invoke(token).apply {
                        Log.e(TAG, "loadMyProfile: $this")
                        if (success) {
                            _profileInfo.value = data.toUi()
                        } else {
                            _profileLoadFail.call()
                        }
                    }
                }
            }.getOrElse {
                _profileLoadFail.call()
            }
        }
    }
}