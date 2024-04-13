package com.zinc.berrybucket.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zinc.berrybucket.util.SingleLiveEvent
import com.zinc.common.utils.TAG
import com.zinc.datastore.login.LoginPreferenceDataStoreModule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class CommonViewModel @Inject constructor(
    private val loginPreferenceDataStoreModule: LoginPreferenceDataStoreModule
) : ViewModel() {

    var accessToken = SingleLiveEvent<String>()
    var refreshToken = SingleLiveEvent<String>()

    private val _error = SingleLiveEvent<Nothing>()
    val error: LiveData<Nothing> get() = _error

    fun <T> CEH(event: MutableLiveData<T>, value: T?) =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.e(TAG, "loadMyProfile: $throwable")
            event.value = value
        }


    init {
        loadToken()
    }

    private fun loadToken() {
        viewModelScope.launch {
            val job1 = launch { loadAccessToken() }
            val job2 = launch { loadRefreshToken() }
            joinAll(job1, job2)
            //   "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjI1LCJpYXQiOjE2OTk3MDU1OTMsImV4cCI6MTcwMDA2NTU5M30.enBLkgc_xNWp-aAT4eDoF3pL6faciohGdn3f5HWXrMY"
            //  "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjQsImlhdCI6MTY3ODQ2MTgwMywiZXhwIjoxNzQ1NzI2MTgwM30.RG-TKPJR3UbLBXD-O9269gyNLv21G9KIBP1Q6SNaeCU"
        }
    }

    private suspend fun loadAccessToken() {
        loginPreferenceDataStoreModule.loadRefreshToken.collectLatest {
            refreshToken.value =
                it
            //"Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjQsImlhdCI6MTY3ODQ2MTgwMywiZXhwIjoxNzQ1NzI2MTgwM30.RG-TKPJR3UbLBXD-O9269gyNLv21G9KIBP1Q6SNaeCU"
        }
    }

    private suspend fun loadRefreshToken() {
        loginPreferenceDataStoreModule.loadAccessToken.collectLatest {
            accessToken.value =
                it
            //"Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjQsImlhdCI6MTY3ODQ2MTgwMywiZXhwIjoxNzQ1NzI2MTgwM30.RG-TKPJR3UbLBXD-O9269gyNLv21G9KIBP1Q6SNaeCU"
        }
    }
}