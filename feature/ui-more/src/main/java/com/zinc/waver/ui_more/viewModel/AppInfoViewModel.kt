package com.zinc.waver.ui_more.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.datastore.common.CommonDataStoreModule
import com.zinc.datastore.login.PreferenceDataStoreModule
import com.zinc.domain.usecases.more.RequestWithdraw
import com.zinc.waver.ui.viewmodel.CommonViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppInfoViewModel @Inject constructor(
    private val requestWithdraw: RequestWithdraw,
    private val commonDataStoreModule: CommonDataStoreModule,
    private val preferenceDataStoreModule: PreferenceDataStoreModule,
) : CommonViewModel() {

    private val _appVersion = MutableLiveData<String>()
    val appVersion: LiveData<String> get() = _appVersion

    private val _requestFail = MutableLiveData<Boolean>()
    val requestFail: LiveData<Boolean> get() = _requestFail

    private val _withdrawSucceed = MutableLiveData<Boolean>()
    val withdrawSucceed: LiveData<Boolean> get() = _withdrawSucceed

    fun loadAppVersion() {
        viewModelScope.launch {
            commonDataStoreModule.loadAppVersion.collectLatest {
                _appVersion.value = it
            }
        }
    }

    fun deleteAccount() {
        viewModelScope.launch(ceh(_requestFail, true)) {
            _requestFail.value = false
            _withdrawSucceed.value = false

            val result = requestWithdraw()
            if (result.success) {
                preferenceDataStoreModule.clearLoginEmail()
                _withdrawSucceed.value = true
            }
        }
    }
}