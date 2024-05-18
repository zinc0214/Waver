package com.zinc.berrybucket.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.berrybucket.model.CategoryLoadFailStatus
import com.zinc.berrybucket.model.UIBucketInfoSimple
import com.zinc.berrybucket.model.UICategoryInfo
import com.zinc.berrybucket.model.parseToUI
import com.zinc.berrybucket.model.parseUI
import com.zinc.berrybucket.util.SingleLiveEvent
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

    private val _categoryBucketList = MutableLiveData<List<UIBucketInfoSimple>>()
    val categoryBucketList: LiveData<List<UIBucketInfoSimple>> get() = _categoryBucketList

    private val _loadFail = SingleLiveEvent<CategoryLoadFailStatus>()
    val loadFail: LiveData<CategoryLoadFailStatus> get() = _loadFail

    private var orderType = AllBucketListSortType.CREATED

    init {
        viewModelScope.launch {
            loadOrderTypeDataStore()
        }
    }

    fun loadCategoryList() {
        _loadFail.value = null
        accessToken.value?.let { token ->
            viewModelScope.launch(CEH(_loadFail, CategoryLoadFailStatus.LoadFail)) {
                runCatching {
                    val result = loadCategoryList.invoke(token)
                    if (result.success) {
                        _categoryInfoList.value = result.data.parseUI()
                    } else {
                        _loadFail.value = CategoryLoadFailStatus.LoadFail
                    }
                }.getOrElse {
                    _loadFail.value = CategoryLoadFailStatus.LoadFail
                }
            }
        } ?: run {
            _loadFail.value = CategoryLoadFailStatus.LoadFail
        }
    }

    fun addNewCategory(name: String) {
        _loadFail.value = null

        accessToken.value?.let { token ->
            viewModelScope.launch(CEH(_loadFail, CategoryLoadFailStatus.AddFail)) {
                val response = addNewCategory(token, name)
                if (response.success) {
                    loadCategoryList()
                } else {
                    _loadFail.value = CategoryLoadFailStatus.AddFail
                }
            }
        } ?: run {
            _loadFail.value = CategoryLoadFailStatus.AddFail
        }
    }

    fun editCategory(categoryInfo: UICategoryInfo) {
        _loadFail.value = null
        accessToken.value?.let { token ->
            viewModelScope.launch(CEH(_loadFail, CategoryLoadFailStatus.EditFail)) {
                val response = editCategoryName(token = token, categoryInfo.id, categoryInfo.name)
                if (response.success) {
                    loadCategoryList()
                } else {
                    _loadFail.value = CategoryLoadFailStatus.EditFail
                }
            }
        } ?: run {
            _loadFail.value = CategoryLoadFailStatus.EditFail
        }
    }

    fun removeCategory(categoryId: Int) {
        _loadFail.value = null

        accessToken.value?.let { token ->
            viewModelScope.launch(CEH(_loadFail, CategoryLoadFailStatus.DeleteFail)) {
                val response = removeCategoryItem(token, categoryId)
                if (response.success) {
                    loadCategoryList()
                } else {
                    _loadFail.value = CategoryLoadFailStatus.DeleteFail
                }
            }
        } ?: run {
            _loadFail.value = CategoryLoadFailStatus.DeleteFail
        }
    }

    fun reorderCategory() {
        _loadFail.value = null
        val updatedList = _categoryInfoList.value.orEmpty()
        accessToken.value?.let { token ->
            viewModelScope.launch(CEH(_loadFail, CategoryLoadFailStatus.ReorderFail)) {
                val response = reorderCategory(token, updatedList.map { it.id.toString() })
                if (response.success) {
                    loadCategoryList()
                } else {
                    _loadFail.value = CategoryLoadFailStatus.ReorderFail
                }
            }
        } ?: run {
            _loadFail.value = CategoryLoadFailStatus.ReorderFail
        }
    }

    fun updateCategoryOrder(list: List<UICategoryInfo>) {
        _categoryInfoList.value = list
    }

    fun loadCategoryBucketList(categoryId: Int) {
        _loadFail.value = null
        accessToken.value?.let { token ->
            runCatching {
                viewModelScope.launch(CEH(_loadFail, CategoryLoadFailStatus.BucketLoadFail)) {
                    val response = loadCategoryBucketList.invoke(
                        token,
                        categoryId.toString(),
                        orderType
                    )
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