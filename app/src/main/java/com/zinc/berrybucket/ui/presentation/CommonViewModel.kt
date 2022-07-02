package com.zinc.berrybucket.ui.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zinc.berrybucket.util.SingleLiveEvent
import com.zinc.datastore.login.LoginPreferenceDataStoreModule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class CommonViewModel @Inject constructor(
    private val loginPreferenceDataStoreModule: LoginPreferenceDataStoreModule
) : ViewModel() {

    var accessToken = SingleLiveEvent<String>()
    var refreshToken = SingleLiveEvent<String>()

    init {
        loadToken()
    }

    fun loadToken() {
        viewModelScope.launch {
            loginPreferenceDataStoreModule.loadAccessToken.collectLatest {
                accessToken.value = it
            }

            loginPreferenceDataStoreModule.loadRefreshToken.collectLatest {
                refreshToken.value = it
            }
        }
    }
}