package com.zinc.waver.ui.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.zinc.datastore.login.LoginPreferenceDataStoreModule
import com.zinc.waver.ui.viewmodel.CommonViewModel
import com.zinc.waver.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val loginPreferenceDataStoreModule: LoginPreferenceDataStoreModule
) : CommonViewModel() {
    private val _logoutSucceed = SingleLiveEvent<Boolean>()
    val logoutSucceed: LiveData<Boolean> get() = _logoutSucceed

    fun logout() {
        viewModelScope.launch {
            loginPreferenceDataStoreModule.clearLoginEmail()
            _logoutSucceed.value = true
        }
    }
}
