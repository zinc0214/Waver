package com.zinc.data.repository

import android.util.Log
import com.zinc.common.models.CategoryInfo
import com.zinc.common.models.CommonResponse
import com.zinc.data.api.BerryBucketApi
import com.zinc.data.model.AddNewCategoryRequest
import com.zinc.data.model.EditCategoryNameRequest
import com.zinc.data.model.ReorderedCategoryRequest
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

    override suspend fun editCategoryName(token: String, id: Int, name: String): CommonResponse {
        return berryBucketApi.editCategoryName(token, EditCategoryNameRequest(id, name))
    }

    override suspend fun removeCategory(token: String, id: Int): CommonResponse {
        return berryBucketApi.removeCategoryItem(token, id)
    }

    override suspend fun reorderCategory(token: String, orderedIds: List<String>): CommonResponse {
        return berryBucketApi.reorderedCategory(token, ReorderedCategoryRequest(orderedIds))
    }
}