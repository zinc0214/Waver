package com.zinc.berrybucket.ui.presentation.report

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.berrybucket.ui.viewmodel.CommonViewModel
import com.zinc.berrybucket.util.SingleLiveEvent
import com.zinc.common.models.ReportItems
import com.zinc.datastore.login.LoginPreferenceDataStoreModule
import com.zinc.domain.usecases.report.LoadReportItems
import com.zinc.domain.usecases.report.ReportComment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val loadReportItems: LoadReportItems,
    private val requestReportComment: ReportComment,
    loginPreferenceDataStoreModule: LoginPreferenceDataStoreModule
) : CommonViewModel(loginPreferenceDataStoreModule) {

    private val _reportItemList = MutableLiveData<ReportItems>()
    val reportItemList: LiveData<ReportItems> get() = _reportItemList

    private val _commentReportSucceed = SingleLiveEvent<Boolean>()
    val commentReportSucceed: LiveData<Boolean> get() = _commentReportSucceed

    private val _requestFail = SingleLiveEvent<Boolean>()
    val requestFail: LiveData<Boolean> get() = _requestFail

    fun loadReportItmes() {
        viewModelScope.launch {
            runCatching {
                loadReportItems.invoke().apply {
                    _reportItemList.value = this
                }
            }.getOrElse {

            }
        }
    }

    fun requestReportComment(id: Int, reason: String) {
        viewModelScope.launch {
            accessToken.value?.let { token ->
                val result = requestReportComment.invoke(token, id, reason)
                Log.e("ayhan", "requestReportComment result : $reason\n $result")
                if (result.success) {
                    _commentReportSucceed.value = true
                } else {
                    _requestFail.value = true
                }
            }
        }
    }
}