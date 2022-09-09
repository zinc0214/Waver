package com.zinc.berrybucket.ui.presentation.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.berrybucket.ui.presentation.CommonViewModel
import com.zinc.common.models.Category
import com.zinc.datastore.login.LoginPreferenceDataStoreModule
import com.zinc.domain.category.LoadCategoryList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val loadCategoryList: LoadCategoryList,
    private val loginPreferenceDataStoreModule: LoginPreferenceDataStoreModule
) : CommonViewModel(loginPreferenceDataStoreModule) {

    private val _categoryList = MutableLiveData<List<Category>>()
    val categoryList: LiveData<List<Category>> get() = _categoryList

    fun loadCategoryList() {
        viewModelScope.launch {
            _categoryList.value = loadCategoryList.invoke()
        }
    }
}