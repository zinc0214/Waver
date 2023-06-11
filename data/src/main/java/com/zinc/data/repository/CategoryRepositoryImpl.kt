package com.zinc.data.repository

import android.util.Log
import com.zinc.common.models.CategoryInfo
import com.zinc.common.models.CommonResponse
import com.zinc.data.api.BerryBucketApi
import com.zinc.data.model.AddNewCategoryRequest
import com.zinc.domain.repository.CategoryRepository
import javax.inject.Inject

internal class CategoryRepositoryImpl @Inject constructor(
    private val berryBucketApi: BerryBucketApi
) : CategoryRepository {
    override suspend fun loadCategoryList(token: String): List<CategoryInfo> {
        Log.e("ayhan", "loadCategoryList impl : ${berryBucketApi.loadCategoryList(token)}")
        return berryBucketApi.loadCategoryList(token).data
    }

    override suspend fun addNewCategory(token: String, name: String): CommonResponse {
        return berryBucketApi.addNewCategory(token, AddNewCategoryRequest(name))
    }
}