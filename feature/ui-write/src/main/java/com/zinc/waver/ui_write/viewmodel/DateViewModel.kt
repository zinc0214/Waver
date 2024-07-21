package com.zinc.waver.ui_write.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class DateViewModel @Inject constructor() : ViewModel() {
    private val _validDateRange = MutableLiveData<Int>()
    val validDateRange: LiveData<Int> get() = _validDateRange

    fun updateYearAndMonth(year: Int, month: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, 1)
        _validDateRange.value = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    }
}
