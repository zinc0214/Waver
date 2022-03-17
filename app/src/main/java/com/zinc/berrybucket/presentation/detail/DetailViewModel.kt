package com.zinc.berrybucket.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zinc.berrybucket.model.DetailDescType
import com.zinc.domain.usecases.detail.LoadBucketDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val loadBucketDetail: LoadBucketDetail
) : ViewModel() {

    private val _bucketDetailInfo = MutableLiveData<List<DetailDescType>>()
    val bucketDetailInfo: LiveData<List<DetailDescType>> get() = _bucketDetailInfo

    fun getBucketDetail(id: String) {
        viewModelScope.launch {
            loadBucketDetail(id)
        }
    }
}