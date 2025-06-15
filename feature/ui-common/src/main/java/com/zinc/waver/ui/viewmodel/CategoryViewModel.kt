package com.zinc.waver.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.common.models.AllBucketListSortType
import com.zinc.datastore.bucketListFilter.FilterPreferenceDataStoreModule
import com.zinc.domain.usecases.category.AddNewCategory
import com.zinc.domain.usecases.category.EditCategoryName
import com.zinc.domain.usecases.category.LoadCategoryBucketList
import com.zinc.domain.usecases.category.LoadCategoryList
import com.zinc.domain.usecases.category.RemoveCategoryItem
import com.zinc.domain.usecases.category.ReorderCategory
import com.zinc.waver.model.CategoryLoadFailStatus
import com.zinc.waver.model.UIBucketInfoSimple
import com.zinc.waver.model.UICategoryInfo
import com.zinc.waver.model.parseToUI
import com.zinc.waver.model.parseUI
import com.zinc.waver.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
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
) : CommonViewModel() {

    private val _categoryInfoList = MutableLiveData<List<UICategoryInfo>>()
    val categoryInfoList: LiveData<List<UICategoryInfo>> get() = _categoryInfoList

    private val _categoryBucketList = MutableLiveData<List<UIBucketInfoSimple>>()
    val categoryBucketList: LiveData<List<UIBucketInfoSimple>> get() = _categoryBucketList

    private val _loadFail = SingleLiveEvent<CategoryLoadFailStatus?>()
    val loadFail: LiveData<CategoryLoadFailStatus?> get() = _loadFail

    private var orderType = AllBucketListSortType.CREATED

    init {
        viewModelScope.launch {
            loadOrderTypeDataStore()
        }
    }

    fun loadCategoryList() {
        _loadFail.value = null
        viewModelScope.launch(ceh(_loadFail, CategoryLoadFailStatus.LoadFail)) {
            runCatching {
                val result = loadCategoryList.invoke()
                Log.e("ayhan", "loadCategoryList : $result")
                if (result.success) {
                    _categoryInfoList.value = result.data.parseUI()
                } else {
                    _loadFail.value = CategoryLoadFailStatus.LoadFail
                }
            }.getOrElse {
                _loadFail.value = CategoryLoadFailStatus.LoadFail
            }
        }
    }

    fun requestAddNewCategory(name: String) {
        _loadFail.value = null
        viewModelScope.launch(ceh(_loadFail, CategoryLoadFailStatus.AddFail)) {
            val response = addNewCategory(name)
            if (response.success) {
                loadCategoryList()
            } else {
                _loadFail.value = CategoryLoadFailStatus.AddFail
            }
        }
    }

    fun editCategory(categoryInfo: UICategoryInfo) {
        _loadFail.value = null
        viewModelScope.launch(ceh(_loadFail, CategoryLoadFailStatus.EditFail)) {
            val response = editCategoryName(categoryInfo.id, categoryInfo.name)
            if (response.success) {
                loadCategoryList()
            } else {
                _loadFail.value = CategoryLoadFailStatus.EditFail
            }
        }
    }

    fun removeCategory(categoryId: Int) {
        _loadFail.value = null
        viewModelScope.launch(ceh(_loadFail, CategoryLoadFailStatus.DeleteFail)) {
            val response = removeCategoryItem.invoke(categoryId)
            if (response.success) {
                loadCategoryList()
            } else {
                _loadFail.value = CategoryLoadFailStatus.DeleteFail
            }
        }
    }

    fun reorderCategory() {
        _loadFail.value = null
        val updatedList = _categoryInfoList.value.orEmpty()
        viewModelScope.launch(ceh(_loadFail, CategoryLoadFailStatus.ReorderFail)) {
            val response = reorderCategory(updatedList.map { it.id.toString() })
            if (response.success) {
                loadCategoryList()
            } else {
                _loadFail.value = CategoryLoadFailStatus.ReorderFail
            }
        }
    }

    fun updateCategoryOrder(list: List<UICategoryInfo>) {
        _categoryInfoList.value = list
    }

    fun loadCategoryBucketList(categoryId: Int) {
        _loadFail.value = null
        runCatching {
            viewModelScope.launch(ceh(_loadFail, CategoryLoadFailStatus.BucketLoadFail)) {
                val response = loadCategoryBucketList.invoke(categoryId.toString(), orderType)
                if (response.success) {
                    _categoryBucketList.value = response.data.bucketlist.parseToUI()
                } else {
                    _loadFail.value = CategoryLoadFailStatus.BucketLoadFail
                }
            }
        }.getOrElse {
            _loadFail.value = CategoryLoadFailStatus.BucketLoadFail
        }
    }

    private suspend fun loadOrderTypeDataStore() {
        filterPreferenceDataStoreModule.apply {
            loadOrderType.collectLatest {
                orderType = loadSortFilter(it)
            }
        }
    }

    private fun loadSortFilter(orderIntType: Int) =
        if (orderIntType == 1) AllBucketListSortType.CREATED else AllBucketListSortType.UPDATED
}