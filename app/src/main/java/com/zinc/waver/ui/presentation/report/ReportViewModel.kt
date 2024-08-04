package com.zinc.waver.ui.presentation.report

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.common.models.ReportItems
import com.zinc.domain.usecases.report.LoadReportItems
import com.zinc.domain.usecases.report.ReportComment
import com.zinc.waver.ui.viewmodel.CommonViewModel
import com.zinc.waver.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val loadReportItems: LoadReportItems,
    private val requestReportComment: ReportComment
) : CommonViewModel() {

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

    fun requestReportComment(id: String, reason: String) {
        viewModelScope.launch(ceh(_requestFail, true)) {
            val result = requestReportComment.invoke(id, reason)
            Log.e("ayhan", "requestReportComment result : $reason\n $result")
            if (result.success) {
                _commentReportSucceed.value = true
            } else {
                _requestFail.value = true
            }
        }
    }
}