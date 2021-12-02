package com.zinc.berrybucket.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zinc.data.models.DetailInfo
import com.zinc.domain.usecases.detail.LoadBucketDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val loadBucketDetail: LoadBucketDetail
) : ViewModel() {

    private val _bucketDetailInfo = MutableLiveData<DetailInfo>()
    val bucketDetailInfo: LiveData<DetailInfo> get() = _bucketDetailInfo

    fun loadBucketDetail(id: String) {
        viewModelScope.launch {
            loadBucketDetail(id)
        }
    }
}