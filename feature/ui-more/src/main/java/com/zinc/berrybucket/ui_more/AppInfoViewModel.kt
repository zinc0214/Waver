package com.zinc.berrybucket.ui_more

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zinc.datastore.common.CommonDataStoreModule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppInfoViewModel @Inject constructor(
    private val commonDataStoreModule: CommonDataStoreModule
) : ViewModel() {

    private val _appVersion = MutableLiveData<String>()
    val appVersion: LiveData<String> get() = _appVersion

    fun loadAppVersion() {
        viewModelScope.launch {
            commonDataStoreModule.loadAppVersion.collectLatest {
                _appVersion.value = it
            }
        }
    }
}