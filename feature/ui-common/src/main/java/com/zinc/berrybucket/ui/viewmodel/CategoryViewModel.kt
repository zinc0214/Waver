package com.zinc.berrybucket.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.berrybucket.model.UIBucketInfoSimple
import com.zinc.berrybucket.model.UICategoryInfo
import com.zinc.berrybucket.model.parseToUI
import com.zinc.berrybucket.model.parseUI
import com.zinc.common.models.AllBucketListSortType
import com.zinc.datastore.bucketListFilter.FilterPreferenceDataStoreModule
import com.zinc.datastore.login.LoginPreferenceDataStoreModule
import com.zinc.domain.usecases.category.AddNewCategory
import com.zinc.domain.usecases.category.EditCategoryName
import com.zinc.domain.usecases.category.LoadCategoryBucketList
import com.zinc.domain.usecases.category.LoadCategoryList
import com.zinc.domain.usecases.category.RemoveCategoryItem
import com.zinc.domain.usecases.category.ReorderCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val loadCategoryList: LoadCategoryList,
    private val addNewCategory: AddNewCategory,
    private val editCategoryName: EditCategoryName,
    private val removeCategoryItem: RemoveCategoryItem,
    private val reorderCategory: ReorderCategory,
    private val loadCategoryBucketList: LoadCategoryBucketList,
    private val filterPreferenceDataStoreModule: FilterPreferenceDataStoreModule,
    loginPreferenceDataStoreModule: LoginPreferenceDataStoreModule,
) : CommonViewModel(loginPreferenceDataStoreModule) {

    private val _categoryInfoList = MutableLiveData<List<UICategoryInfo>>()
    val categoryInfoList: LiveData<List<UICategoryInfo>> get() = _categoryInfoList

    private val _apiFailed = MutableLiveData<Pair<String, String>>()
    val apiFailed: LiveData<Pair<String, String>> get() = _apiFailed

    private val _categoryBucketList = MutableLiveData<List<UIBucketInfoSimple>>()
    val categoryBucketList: LiveData<List<UIBucketInfoSimple>> get() = _categoryBucketList

    private val _apiFailed2 = MutableLiveData<Boolean>()
    val apiFailed2: LiveData<Boolean> get() = _apiFailed2

    private val _orderType = MutableLiveData<Int>()
    val orderType: LiveData<Int> get() = _orderType

    private fun ceh(liveData: MutableLiveData<Boolean>, data: Boolean) =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            liveData.value = data
        }

    init {
        viewModelScope.launch {
            loadOrderTypeDataStore()
        }
    }

    fun loadCategoryList() {
        runCatching {
            accessToken.value?.let { token ->
                viewModelScope.launch(CoroutineExceptionHandler { coroutineContext, throwable ->
                    Log.e("ayhan", "load Category Fail 2 $throwable")
                    _apiFailed.value = "카테고리 로드 실패" to ""
                }) {
                    loadCategoryList.invoke(token).apply {
                        if (this.success) {
                            _categoryInfoList.value = this.data.parseUI()
                        } else {
                            _apiFailed.value = "카테고리 로드 실패" to this.message
                        }
                    }
                }
            }
        }.getOrElse {
            _apiFailed.value = "카테고리 로드 실패" to ""
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
        }
    }

    fun editCategory(categoryInfo: UICategoryInfo) {
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
        }
    }

    fun removeCategory(categoryId: Int) {
        accessToken.value?.let { token ->
            viewModelScope.launch(CoroutineExceptionHandler { coroutineContext, throwable ->
            }) {
                _apiFailed.value = null
                val response = removeCategoryItem(token, categoryId)
                if (response.success) {
                    loadCategoryList()
                } else {
                    _apiFailed.value = "카테고리 삭제 실패" to response.message
                }
            }
        }
    }

    fun reorderCategory() {
        val updatedList = _categoryInfoList.value.orEmpty()
        Log.d("ayhan", "reorderCategory: ${updatedList}")
        accessToken.value?.let { token ->
            viewModelScope.launch(CoroutineExceptionHandler { coroutineContext, throwable ->
            }) {
                _apiFailed.value = null
                val response = reorderCategory(token, updatedList.map { it.id.toString() })
                Log.d("ayhan", "reorderCategoryresponse: ${response}")

                if (response.success) {
                    loadCategoryList()
                } else {
                    _apiFailed.value = "카테고리 순서 편집 실패" to response.message
                }
            }
        }
    }

    fun updateCategoryOrder(list: List<UICategoryInfo>) {
        _categoryInfoList.value = list
    }

    fun loadCategoryBucketList(categoryId: Int) {
        accessToken.value?.let { token ->
            runCatching {
                viewModelScope.launch(ceh(_apiFailed2, true)) {
                    val response = loadCategoryBucketList.invoke(
                        token,
                        categoryId.toString(),
                        loadSortFilter()
                    )
                    Log.e("ayhan", "loadCategoryBucketList response : ${response}")
                    if (response.success) {
                        _apiFailed2.value = false
                        _categoryBucketList.value = response.data.bucketlist.parseToUI()
                    } else {
                        _apiFailed2.value = true
                    }
                }
            }.getOrElse {
                _apiFailed2.value = true
            }
        }
    }

    private suspend fun loadOrderTypeDataStore() {
        filterPreferenceDataStoreModule.apply {
            loadOrderType.collectLatest {
                _orderType.value = it
                Log.e("ayhan", "loadOrderTypeDataStore")
            }
        }
    }

    private fun loadSortFilter() =
        if (_orderType.value == 1) AllBucketListSortType.CREATED else AllBucketListSortType.UPDATED
}