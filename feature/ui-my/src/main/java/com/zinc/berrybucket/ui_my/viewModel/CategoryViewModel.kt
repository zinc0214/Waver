package com.zinc.berrybucket.ui_my.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.berrybucket.CommonViewModel
import com.zinc.berrybucket.model.UIBucketInfoSimple
import com.zinc.berrybucket.model.UICategoryInfo
import com.zinc.berrybucket.model.parseToUI
import com.zinc.berrybucket.model.parseUI
import com.zinc.common.models.BucketInfoSimple
import com.zinc.common.models.BucketStatus
import com.zinc.common.models.BucketType
import com.zinc.common.models.ExposureStatus
import com.zinc.datastore.login.LoginPreferenceDataStoreModule
import com.zinc.domain.usecases.category.AddNewCategory
import com.zinc.domain.usecases.category.EditCategoryName
import com.zinc.domain.usecases.category.LoadCategoryList
import com.zinc.domain.usecases.category.RemoveCategoryItem
import com.zinc.domain.usecases.category.ReorderCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val loadCategoryList: LoadCategoryList,
    private val addNewCategory: AddNewCategory,
    private val editCategoryName: EditCategoryName,
    private val removeCategoryItem: RemoveCategoryItem,
    private val reorderCategory: ReorderCategory,
    private val loginPreferenceDataStoreModule: LoginPreferenceDataStoreModule
) : CommonViewModel(loginPreferenceDataStoreModule) {

    private val _categoryInfoList = MutableLiveData<List<UICategoryInfo>>()
    val categoryInfoList: LiveData<List<UICategoryInfo>> get() = _categoryInfoList

    private val _apiFailed = MutableLiveData<Pair<String, String>>()
    val apiFailed: LiveData<Pair<String, String>> get() = _apiFailed

    private val _categoryBucketList = MutableLiveData<List<UIBucketInfoSimple>>()
    val categoryBucketList: LiveData<List<UIBucketInfoSimple>> get() = _categoryBucketList

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
            viewModelScope.launch(CoroutineExceptionHandler { coroutineContext, throwable ->
            }) {
                _categoryBucketList.value = simpleTypeList.parseToUI()

            }
        }
    }


    private val simpleTypeList = listOf(
        BucketInfoSimple(
            id = "1",
            title = "아이스크림을 먹을테야 얍얍압얍",
            userCount = 1,
            status = BucketStatus.PROGRESS,
            exposureStatus = ExposureStatus.PUBLIC,
            bucketType = BucketType.ORIGINAL

        ),
        BucketInfoSimple(
            id = "2",
            title = "아이스크림을 여행을 갈거란 말이야",
            userCount = 1,
            status = BucketStatus.PROGRESS,
            exposureStatus = ExposureStatus.PUBLIC,
            bucketType = BucketType.ORIGINAL
        ),
        BucketInfoSimple(
            id = "3",
            title = "Dday가 있는 애22233",
            userCount = 5,
            goalCount = 10,
            dDay = -10,
            status = BucketStatus.PROGRESS,
            exposureStatus = ExposureStatus.PUBLIC,
            bucketType = BucketType.ORIGINAL
        ),
        BucketInfoSimple(
            id = "4",
            title = "Dday가 있는 애22233",
            userCount = 5,
            goalCount = 10,
            dDay = -10,
            status = BucketStatus.PROGRESS,
            exposureStatus = ExposureStatus.PUBLIC,
            bucketType = BucketType.ORIGINAL
        ),
        BucketInfoSimple(
            id = "5",
            title = "Dday가 있는 애22233",
            userCount = 5,
            goalCount = 10,
            dDay = -10,
            status = BucketStatus.PROGRESS,
            exposureStatus = ExposureStatus.PUBLIC,
            bucketType = BucketType.ORIGINAL

        ),
        BucketInfoSimple(
            id = "6",
            title = "Dday가 있는 애22233",
            userCount = 5,
            goalCount = 10,
            dDay = -10,
            status = BucketStatus.PROGRESS,
            exposureStatus = ExposureStatus.PUBLIC,
            bucketType = BucketType.ORIGINAL
        ),
        BucketInfoSimple(
            id = "61",
            title = "Dday가 있는 애22233",
            userCount = 5,
            goalCount = 10,
            dDay = -10,
            status = BucketStatus.PROGRESS,
            exposureStatus = ExposureStatus.PUBLIC,
            bucketType = BucketType.ORIGINAL
        ),
        BucketInfoSimple(
            id = "16",
            title = "Dday가 있는 애22233",
            userCount = 5,
            goalCount = 10,
            dDay = -10,
            status = BucketStatus.PROGRESS,
            exposureStatus = ExposureStatus.PUBLIC,
            bucketType = BucketType.ORIGINAL
        ),
        BucketInfoSimple(
            id = "26",
            title = "Dday가 있는 애22233",
            userCount = 5,
            goalCount = 10,
            dDay = -10,
            status = BucketStatus.PROGRESS,
            exposureStatus = ExposureStatus.PUBLIC,
            bucketType = BucketType.ORIGINAL
        ),
        BucketInfoSimple(
            id = "36",
            title = "Dday가 있는 애22233",
            userCount = 5,
            goalCount = 10,
            dDay = -10,
            status = BucketStatus.PROGRESS,
            exposureStatus = ExposureStatus.PUBLIC,
            bucketType = BucketType.ORIGINAL
        ),
        BucketInfoSimple(
            id = "46",
            title = "Dday가 있는 애22233",
            userCount = 5,
            goalCount = 10,
            dDay = -10,
            status = BucketStatus.PROGRESS,
            exposureStatus = ExposureStatus.PUBLIC,
            bucketType = BucketType.ORIGINAL
        )
    )
}