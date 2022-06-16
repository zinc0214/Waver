package com.zinc.berrybucket.ui.presentation.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zinc.common.models.ReportItems
import com.zinc.domain.usecases.report.LoadReportItems
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val loadReportItems: LoadReportItems
) : ViewModel() {

    private val _reportItemList = MutableLiveData<ReportItems>()
    val reportItemList: LiveData<ReportItems> get() = _reportItemList

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
}