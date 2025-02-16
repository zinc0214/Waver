package com.zinc.waver.ui.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.zinc.datastore.login.PreferenceDataStoreModule
import com.zinc.waver.ui.viewmodel.CommonViewModel
import com.zinc.waver.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val preferenceDataStoreModule: PreferenceDataStoreModule
) : CommonViewModel() {
    private val _logoutSucceed = SingleLiveEvent<Boolean>()
    val logoutSucceed: LiveData<Boolean> get() = _logoutSucceed

    fun logout() {
        viewModelScope.launch {
            preferenceDataStoreModule.clearLoginEmail()
            _logoutSucceed.value = true
        }
    }

    fun updateWaverPlus(purchased: Boolean) {
        viewModelScope.launch {
            preferenceDataStoreModule.setHasWaverPlus(purchased)
        }
    }
}
