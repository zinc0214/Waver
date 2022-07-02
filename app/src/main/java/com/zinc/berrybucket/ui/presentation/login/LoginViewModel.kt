package com.zinc.berrybucket.ui.presentation.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zinc.common.models.JoinResponse
import com.zinc.domain.usecases.lgin.JoinBerryBucket
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val joinBerryBucket: JoinBerryBucket
) : ViewModel() {

    private val _joinResponse = MutableLiveData<JoinResponse>()
    val joinResponse: LiveData<JoinResponse> get() = _joinResponse

    fun joinBerryBucket() {
        viewModelScope.launch {
            runCatching {
                joinBerryBucket.invoke("zinc0214@gmail.com").apply {
                    _joinResponse.value = this
                }
            }.getOrElse {
                Log.e("ayhan", "Fail : $it")
            }
        }
    }
}