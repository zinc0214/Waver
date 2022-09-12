package com.zinc.berrybucket.ui.presentation.write

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DateViewModel @Inject constructor() : ViewModel() {
    private val _validDateRange = MutableLiveData<Int>()
    val validDateRange: LiveData<Int> get() = _validDateRange

    private val _selectedLocalDate = MutableLiveData<LocalDate>()
    val selectedLocalDate: LiveData<LocalDate> get() = _selectedLocalDate

    fun updateYearAndMonth(year: Int, month: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, 1)
        _validDateRange.value = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    fun updateSelectedLocalDate(year: Int, month: Int, date: Int) {
        _selectedLocalDate.value = LocalDate.of(year, month, date)
    }

    fun updateSelectedLocalDate(epochMilli: Long) {
        val date = Instant.ofEpochMilli(epochMilli).atZone(ZoneId.systemDefault()).toLocalDate()
        _selectedLocalDate.value = LocalDate.of(date.year, date.monthValue, date.dayOfMonth)
    }
}
