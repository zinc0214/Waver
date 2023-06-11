package com.zinc.berrybucket.ui_my.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.berrybucket.CommonViewModel
import com.zinc.common.models.CategoryInfo
import com.zinc.datastore.login.LoginPreferenceDataStoreModule
import com.zinc.domain.usecases.category.AddNewCategory
import com.zinc.domain.usecases.category.EditCategoryName
import com.zinc.domain.usecases.category.LoadCategoryList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val loadCategoryList: LoadCategoryList,
    private val addNewCategory: AddNewCategory,
    private val editCategoryName: EditCategoryName,
    private val loginPreferenceDataStoreModule: LoginPreferenceDataStoreModule
) : CommonViewModel(loginPreferenceDataStoreModule) {

    private val _categoryInfoList = MutableLiveData<List<CategoryInfo>>()
    val categoryInfoList: LiveData<List<CategoryInfo>> get() = _categoryInfoList

    private val _apiFailed = MutableLiveData<Pair<String, String>>()
    val apiFailed: LiveData<Pair<String, String>> get() = _apiFailed

    fun loadCategoryList() {
        accessToken.value?.let { token ->
            viewModelScope.launch(CoroutineExceptionHandler { coroutineContext, throwable ->
                Log.e("ayhan", "load Category Fail 2 $throwable")
            }) {
                _categoryInfoList.value = loadCategoryList.invoke(token)
            }
        }.runCatching {

        }

    }

    fun addNewCategory(name: String) {
        accessToken.value?.let { token ->
            viewModelScope.launch(CoroutineExceptionHandler { coroutineContext, throwable ->
                Log.e("ayhan", "load addNewCategory Fail 2 $throwable")
            }) {
                _apiFailed.value = null
                val response = addNewCategory(token, name)
                if (response.success) {
                    loadCategoryList()
                } else {
                    _apiFailed.value = "카테고리 추가 실패" to response.message
                }
            }
        }.runCatching {

        }
    }

    fun editCategory(categoryInfo: CategoryInfo) {
        accessToken.value?.let { token ->
            viewModelScope.launch(CoroutineExceptionHandler { coroutineContext, throwable ->
            }) {
                _apiFailed.value = null
                val response = editCategoryName(token = token, categoryInfo.id, categoryInfo.name)
                if (response.success) {
                    loadCategoryList()
                } else {
                    _apiFailed.value = "카테고리 수정 실패" to response.message
                }
            }
        }.runCatching {

        }
    }

    fun removeCategory(categoryInfo: CategoryInfo) {

    }

    fun reorderCategory(list: List<CategoryInfo>) {

    }
}