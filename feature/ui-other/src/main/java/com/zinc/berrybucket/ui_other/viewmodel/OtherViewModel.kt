package com.zinc.berrybucket.ui_other.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.berrybucket.ui.viewmodel.CommonViewModel
import com.zinc.common.models.OtherProfileHomeData
import com.zinc.datastore.login.LoginPreferenceDataStoreModule
import com.zinc.domain.usecases.other.LoadOtherInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OtherViewModel @Inject constructor(
    private val loadOtherInfo: LoadOtherInfo,
    loginPreferenceDataStoreModule: LoginPreferenceDataStoreModule
) : CommonViewModel(loginPreferenceDataStoreModule) {

    private val _loadFail = MutableLiveData<Boolean>()
    val loadFail: LiveData<Boolean> get() = _loadFail

    private val _otherHomeData = MutableLiveData<OtherProfileHomeData>()
    val otherHomeData: LiveData<OtherProfileHomeData> get() = _otherHomeData

    fun loadOtherInfo(userId: String) {
        _loadFail.value = false
        viewModelScope.launch(CEH(_loadFail, true)) {
            runCatching {
                accessToken.value?.let { token ->
                    val result = loadOtherInfo(token, userId)
                    if (result == null || !result.isSuccess) {
                        _loadFail.value = true
                    } else {
                        _otherHomeData.value = result.data
                        Log.e("ayhan", "loadOtherInfo : ${result.data}")
                    }
                }
            }.getOrElse {
                _loadFail.value = true
            }
        }
    }
}