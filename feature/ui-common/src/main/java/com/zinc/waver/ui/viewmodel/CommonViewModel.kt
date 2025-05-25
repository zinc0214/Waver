package com.zinc.waver.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zinc.common.utils.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
open class CommonViewModel @Inject constructor() : ViewModel() {
    private lateinit var currentLocale: Locale

    fun <T> ceh(event: MutableLiveData<T>, value: T?) =
        CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG, "common Error: $throwable")
            event.value = value
        }

    fun updateLocale(locale: Locale) {
        currentLocale = locale
    }

    fun getLocale() = currentLocale
}