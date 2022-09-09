package com.zinc.data.repository

import com.zinc.common.models.Category
import com.zinc.data.api.BerryBucketApi
import com.zinc.domain.repository.CategoryRepository
import javax.inject.Inject

internal class CategoryRepositoryImpl @Inject constructor(
    private val berryBucketApi: BerryBucketApi
) : CategoryRepository {
    override suspend fun loadCategoryList(): List<Category> {
        return buildList {
            add(Category(id = 0, name = "여행", bucketlistCount = "3"))
        }
        //   return berryBucketApi.loadCategoryList()
    }
}