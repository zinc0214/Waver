package com.zinc.data.repository

import com.zinc.common.models.*
import com.zinc.data.api.BerryBucketApi
import com.zinc.domain.repository.MyRepository
import javax.inject.Inject

internal class MyRepositoryImpl @Inject constructor(
    private val berryBucketApi: BerryBucketApi
) : MyRepository {
    override suspend fun loadMyProfileInfo(): MyProfileInfo {
        return berryBucketApi.loadMyProfileInfo()
    }

    override suspend fun loadMyDdayBucketList(): DdayBucketList {
        //return berryBucketApi.loadMyDdayBucketList()
        return DdayBucketList(
            bucketList = listOf(
                BucketInfoSimple(
                    type = "D_DAY",
                    id = "3",
                    title = "Dday가 있는 애 1, 아이스크림 냠냠 후루룹쨥짭",
                    currentCount = 1,
                    goalCount = 10,
                    dDay = 0,
                    detailType = "MY_CLOSE"
                ),
                BucketInfoSimple(
                    type = "D_PLUS",
                    id = "3",
                    title = "Dday가 있는 애222",
                    currentCount = 2,
                    goalCount = 10,
                    dDay = 20,
                    detailType = "MY_CLOSE"

                ),
                BucketInfoSimple(
                    type = "D_MINUS",
                    id = "3",
                    title = "Dday가 있는 애22233",
                    currentCount = 5,
                    goalCount = 10,
                    dDay = -10,
                    detailType = "MY_CLOSE"
                )
            )
        )
    }

    override suspend fun loadAllBucketList(): AllBucketList {
        // return berryBucketApi.loadAllBucketList()
        return AllBucketList(
            processingCount = "15",
            succeedCount = "20",
            bucketList = listOf(
                BucketInfoSimple(
                    type = "BASIC",
                    id = "1",
                    title = "아이스크림을 먹을테야 히힛",
                    currentCount = 1,
                    detailType = "MY_OPEN"
                ),
                BucketInfoSimple(
                    type = "BASIC",
                    id = "2",
                    title = "제주도 여행을 갈거란 말이야",
                    currentCount = 1,
                    detailType = "MY_CLOSE"
                ),
                BucketInfoSimple(
                    type = "BASIC",
                    id = "1",
                    title = "아이스크림을 먹을테야",
                    currentCount = 1,
                    goalCount = 5,
                    detailType = "MY_OPEN"
                ),
                BucketInfoSimple(
                    type = "BASIC",
                    id = "3",
                    title = "Dday가 있는 애222",
                    currentCount = 5,
                    goalCount = 10,
                    dDay = 20,
                    detailType = "MY_CLOSE"
                ),
                BucketInfoSimple(
                    type = "BASIC",
                    id = "3",
                    title = "Dday가 있는 애22233",
                    currentCount = 5,
                    goalCount = 10,
                    dDay = -10,
                    detailType = "MY_OPEN"
                ),
                BucketInfoSimple(
                    type = "BASIC",
                    id = "3",
                    title = "Dday가 있는 애22233",
                    currentCount = 5,
                    goalCount = 10,
                    dDay = -10,
                    detailType = "MY_OPEN"
                ),
                BucketInfoSimple(
                    type = "BASIC",
                    id = "3",
                    title = "Dday가 있는 애22233",
                    currentCount = 5,
                    goalCount = 10,
                    dDay = -10,
                    detailType = "MY_OPEN"
                ),
                BucketInfoSimple(
                    type = "BASIC",
                    id = "3",
                    title = "Dday가 있는 애22233",
                    currentCount = 5,
                    goalCount = 10,
                    dDay = -10,
                    detailType = "MY_OPEN"
                ),
                BucketInfoSimple(
                    type = "BASIC",
                    id = "3",
                    title = "Dday가 있는 애22233",
                    currentCount = 5,
                    goalCount = 10,
                    dDay = -10,
                    detailType = "MY_OPEN"
                ),
                BucketInfoSimple(
                    type = "BASIC",
                    id = "3",
                    title = "Dday가 있는 애22233",
                    currentCount = 5,
                    goalCount = 10,
                    dDay = -10,
                    detailType = "MY_OPEN"
                ),
                BucketInfoSimple(
                    type = "BASIC",
                    id = "3",
                    title = "Dday가 있는 애22233",
                    currentCount = 5,
                    goalCount = 10,
                    dDay = -10,
                    detailType = "MY_OPEN"
                ),
                BucketInfoSimple(
                    type = "BASIC",
                    id = "3",
                    title = "Dday가 있는 애22233",
                    currentCount = 5,
                    goalCount = 10,
                    dDay = -10,
                    detailType = "MY_OPEN"
                ),
                BucketInfoSimple(
                    type = "BASIC",
                    id = "3",
                    title = "Dday가 있는 애22233",
                    currentCount = 5,
                    goalCount = 10,
                    dDay = -10,
                    detailType = "MY_OPEN"
                ),
                BucketInfoSimple(
                    type = "BASIC",
                    id = "3",
                    title = "Dday가 있는 애22233",
                    currentCount = 5,
                    goalCount = 10,
                    dDay = -10,
                    detailType = "MY_OPEN"
                ),
                BucketInfoSimple(
                    type = "BASIC",
                    id = "3",
                    title = "Dday가 있는 애22233",
                    currentCount = 5,
                    goalCount = 10,
                    dDay = -10,
                    detailType = "MY_OPEN"
                ),
                BucketInfoSimple(
                    type = "BASIC",
                    id = "3",
                    title = "Dday가 있는 애22233",
                    currentCount = 5,
                    goalCount = 10,
                    dDay = -10,
                    detailType = "MY_OPEN"
                )
            )
        )

    }

    override suspend fun loadCategoryList(): List<Category> {
        // return berryBucketApi.loadCategoryList()
        return listOf(
            Category(
                id = 1,
                name = "여행",
                count = "20"
            ),
            Category(
                id = 1,
                name = "아주아주 맛있는 것을 먹으러 다니는 거야 냠냠쩝쩝 하면서 룰루리랄라 크크루삥봉",
                count = "10"
            ),
            Category(
                id = 1,
                name = "제주도여행을 갈거야",
                count = "3"
            )
        )

    }

    override suspend fun loadMyState(): MyState {
        return berryBucketApi.loadMyState()
    }
}