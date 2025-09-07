package com.zinc.waver.ui_report

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.common.models.ReportItems
import com.zinc.domain.usecases.report.LoadReportItems
import com.zinc.domain.usecases.report.ReportBucket
import com.zinc.domain.usecases.report.ReportComment
import com.zinc.waver.ui.viewmodel.CommonViewModel
import com.zinc.waver.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val loadReportItems: LoadReportItems,
    private val requestReportComment: ReportComment,
    private val requestReportBucket: ReportBucket
) : CommonViewModel() {

    private val _reportItemList = MutableLiveData<ReportItems>()
    val reportItemList: LiveData<ReportItems> get() = _reportItemList

    private val _commentReportSucceed = SingleLiveEvent<Boolean>()
    val commentReportSucceed: LiveData<Boolean> get() = _commentReportSucceed

    private val _requestFail = SingleLiveEvent<Boolean>()
    val requestFail: LiveData<Boolean> get() = _requestFail

    fun loadReportItems() {
        viewModelScope.launch(ceh(_requestFail, true)) {
            _requestFail.value = false
            loadReportItems.invoke().apply {
                _reportItemList.value = this
            }
        }
    }

    fun requestReportComment(id: String, reason: String) {
        viewModelScope.launch(ceh(_requestFail, true)) {
            _requestFail.value = false
            val result = requestReportComment.invoke(id, reason)
            Log.e("ayhan", "requestReportComment result : $reason\n $result")
            if (result.success) {
                _commentReportSucceed.value = true
            } else {
                _requestFail.value = true
            }
        }
    }

    fun requestReportBucket(id: String, reason: String) {
        viewModelScope.launch(ceh(_requestFail, true)) {
            _requestFail.value = false
            val result = requestReportBucket.invoke(id, reason)
            Log.e("ayhan", "requestReportComment result : $reason\n $result")
            if (result.success) {
                _commentReportSucceed.value = true
            } else {
                _requestFail.value = true
            }
        }
    }
}