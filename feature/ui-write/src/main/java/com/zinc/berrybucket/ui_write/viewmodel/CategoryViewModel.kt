package com.zinc.berrybucket.ui_write.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zinc.berrybucket.CommonViewModel
import com.zinc.common.models.CategoryInfo
import com.zinc.datastore.login.LoginPreferenceDataStoreModule
import com.zinc.domain.usecases.category.LoadCategoryList
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WriteCategoryViewModel @Inject constructor(
    private val loadCategoryList: LoadCategoryList,
    private val loginPreferenceDataStoreModule: LoginPreferenceDataStoreModule
) : CommonViewModel(loginPreferenceDataStoreModule) {

    private val _categoryInfoList = MutableLiveData<List<CategoryInfo>>()
    val categoryInfoList: LiveData<List<CategoryInfo>> get() = _categoryInfoList

    fun loadCategoryList() {

        _categoryInfoList.value = listOf(
            CategoryInfo(
                id = 0,
                name = "여행",
                bucketlistCount = "10"
            ),
            CategoryInfo(
                id = 1,
                name = "음식",
                bucketlistCount = "10"
            ),
            CategoryInfo(
                id = 2,
                name = "쇼미",
                bucketlistCount = "10"
            )
        )
//        accessToken.value?.let { token ->
//            viewModelScope.launch {
//                _categoryInfoList.value = loadCategoryList.invoke(token)
//            }
//        }.runCatching {
//
//        }

    }
}