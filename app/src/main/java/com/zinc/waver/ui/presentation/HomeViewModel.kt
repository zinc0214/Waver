package com.zinc.waver.ui.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.zinc.common.models.ProfileInfo
import com.zinc.datastore.login.PreferenceDataStoreModule
import com.zinc.domain.usecases.detail.LoadProfileInfo
import com.zinc.waver.ui.viewmodel.CommonViewModel
import com.zinc.waver.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val preferenceDataStoreModule: PreferenceDataStoreModule,
    private val loadProfileInfo: LoadProfileInfo
) : CommonViewModel() {
    private val _logoutSucceed = SingleLiveEvent<Boolean>()
    val logoutSucceed: LiveData<Boolean> get() = _logoutSucceed

    private val _doNothing = SingleLiveEvent<Nothing>()

    fun logout() {
        viewModelScope.launch {
            preferenceDataStoreModule.clearLoginEmail()
            _logoutSucceed.value = true
        }
    }

    fun loadProfileInfo() {
        viewModelScope.launch(ceh(_doNothing, null)) {
            val response = loadProfileInfo.invoke(true, null)
            if (response.success) {
                val isWaverUser = response.data.premiumStatus == ProfileInfo.PremiumStatus.ACTIVE
                updateWaverPlus(isWaverUser)
            }
        }
    }

    fun updateWaverPlus(purchased: Boolean) {
        viewModelScope.launch {
            preferenceDataStoreModule.setHasWaverPlus(purchased)
        }
    }
}
