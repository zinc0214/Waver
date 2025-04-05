package com.zinc.waver.ui_more.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.common.models.MyWaveInfo
import com.zinc.domain.usecases.more.LoadMyWaveInfo
import com.zinc.waver.ui.viewmodel.CommonViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyWaveManageViewModel @Inject constructor(
    private val loadMyWaveInfo: LoadMyWaveInfo
) : CommonViewModel() {

    private val _myWaveInfo = MutableLiveData<MyWaveInfo>()
    val myWaveInfo: LiveData<MyWaveInfo> get() = _myWaveInfo

    private val _loadFail = MutableLiveData<Boolean>()
    val loadFail: LiveData<Boolean> get() = _loadFail

    fun loadMyWaveInfo() {
        viewModelScope.launch(ceh(_loadFail, true)) {
            _loadFail.value = false

            val result = loadMyWaveInfo.invoke()
            if (result.success) {
                _myWaveInfo.value = result.data
                Log.e("ayhan", "myWaveInfo  :${result.data}")
            } else {
                _loadFail.value = true
            }
        }
    }
}