package com.zinc.data.repository

import com.zinc.common.models.AddNewCategoryRequest
import com.zinc.common.models.AllBucketListResponse
import com.zinc.common.models.AllBucketListSortType
import com.zinc.common.models.CommonResponse
import com.zinc.common.models.EditCategoryNameRequest
import com.zinc.common.models.LoadCategoryResponse
import com.zinc.common.models.ReorderedCategoryRequest
import com.zinc.data.api.BerryBucketApi
import com.zinc.domain.repository.CategoryRepository
import javax.inject.Inject

internal class CategoryRepositoryImpl @Inject constructor(
    private val berryBucketApi: BerryBucketApi
) : CategoryRepository {
    override suspend fun loadCategoryList(): LoadCategoryResponse {
        return berryBucketApi.loadCategoryList()
    }

    override suspend fun addNewCategory(name: String): CommonResponse {
        return berryBucketApi.addNewCategory(AddNewCategoryRequest(name))
    }

    override suspend fun editCategoryName(id: Int, name: String): CommonResponse {
        return berryBucketApi.editCategoryName(EditCategoryNameRequest(id, name))
    }

    override suspend fun removeCategory(id: Int): CommonResponse {
        return berryBucketApi.removeCategoryItem(id)
    }

    override suspend fun reorderCategory(orderedIds: List<String>): CommonResponse {
        return berryBucketApi.reorderedCategory(ReorderedCategoryRequest(orderedIds))
    }

    override suspend fun searchCategoryList(query: String): LoadCategoryResponse {
        return berryBucketApi.searchCategoryList(query)
    }

    override suspend fun loadCategoryBucketList(
        categoryId: String,
        sort: AllBucketListSortType
    ): AllBucketListResponse {
        return berryBucketApi.loadAllBucketList(
            categoryId = categoryId,
            sort = sort
        )
    }
}