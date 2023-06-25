package com.zinc.data.repository

import android.util.Log
import com.zinc.common.models.AllBucketListRequest
import com.zinc.common.models.AllBucketListResponse
import com.zinc.common.models.BucketInfoSimple
import com.zinc.common.models.BucketStatus
import com.zinc.common.models.BucketType
import com.zinc.common.models.CategoryInfo
import com.zinc.common.models.DdayBucketList
import com.zinc.common.models.DetailType
import com.zinc.common.models.MyProfileResponse
import com.zinc.common.models.MyState
import com.zinc.data.api.BerryBucketApi
import com.zinc.domain.repository.MyRepository
import javax.inject.Inject

internal class MyRepositoryImpl @Inject constructor(
    private val berryBucketApi: BerryBucketApi
) : MyRepository {
    override suspend fun loadMyProfileInfo(token: String): MyProfileResponse {
        return berryBucketApi.loadMyProfileInfo(token)
    }

    override suspend fun loadMyDdayBucketList(): DdayBucketList {
        //return berryBucketApi.loadMyDdayBucketList()
        return DdayBucketList(
            bucketList = listOf(
                BucketInfoSimple(
                    id = "3",
                    title = "Dday가 있는 애 1, 아이스크림 냠냠 후루룹쨥짭",
                    userCount = 1,
                    goalCount = 10,
                    dDay = 0,
                    status = BucketStatus.COMPLETE,
                    detailType = DetailType.MY_OPEN,
                    bucketType = BucketType.ORIGINAL
                ),
                BucketInfoSimple(
                    id = "3",
                    title = "Dday가 있는 애222",
                    userCount = 2,
                    goalCount = 10,
                    dDay = 20,
                    status = BucketStatus.COMPLETE,
                    detailType = DetailType.MY_OPEN,
                    bucketType = BucketType.ORIGINAL
                ),
                BucketInfoSimple(
                    id = "3",
                    title = "Dday가 있는 애22233",
                    userCount = 5,
                    goalCount = 10,
                    dDay = -10,
                    status = BucketStatus.PROGRESS,
                    detailType = DetailType.MY_OPEN,
                    bucketType = BucketType.ORIGINAL
                )
            )
        )
    }

    override suspend fun loadAllBucketList(
        token: String,
        allBucketListRequest: AllBucketListRequest
    ): AllBucketListResponse {
        Log.e("ayhan", "loadAllBucketList: $allBucketListRequest")
        return berryBucketApi.loadAllBucketList(
            token = token,
            dDayBucketOnly = allBucketListRequest.dDayBucketOnly,
            isPassed = allBucketListRequest.isPassed,
            status = allBucketListRequest.status,
            sort = allBucketListRequest.sort
        )
//        return AllBucketList(
//            processingCount = "15",
//            succeedCount = "20",
//            bucketList = listOf(
//                BucketInfoSimple(
//                    type = "BASIC",
//                    id = "1",
//                    title = "아이스크림을 먹을테야 히힛",
//                    currentCount = 1,
//                    status = BucketStatus.PROGRESS,
//                    detailType = DetailType.MY_CLOSE
//                ),
//                BucketInfoSimple(
//                    type = "BASIC",
//                    id = "2",
//                    title = "제주도 여행을 갈거란 말이야",
//                    currentCount = 1,
//                    status = BucketStatus.COMPLETE,
//                    detailType = DetailType.MY_CLOSE
//                ),
//                BucketInfoSimple(
//                    type = "BASIC",
//                    id = "1",
//                    title = "아이스크림을 먹을테야",
//                    currentCount = 1,
//                    goalCount = 5,
//                    status = BucketStatus.PROGRESS,
//                    detailType = DetailType.MY_OPEN
//                ),
//                BucketInfoSimple(
//                    type = "BASIC",
//                    id = "3",
//                    title = "Dday가 있는 애222",
//                    currentCount = 5,
//                    goalCount = 10,
//                    dDay = 20,
//                    status = BucketStatus.PROGRESS,
//                    detailType = DetailType.MY_OPEN
//                ),
//                BucketInfoSimple(
//                    type = "BASIC",
//                    id = "3",
//                    title = "Dday가 있는 애22233",
//                    currentCount = 5,
//                    goalCount = 10,
//                    dDay = -10,
//                    status = BucketStatus.PROGRESS,
//                    detailType = DetailType.MY_OPEN
//                ),
//                BucketInfoSimple(
//                    type = "BASIC",
//                    id = "3",
//                    title = "Dday가 있는 애22233",
//                    currentCount = 5,
//                    goalCount = 10,
//                    dDay = -10,
//                    status = BucketStatus.PROGRESS,
//                    detailType = DetailType.MY_OPEN
//                ),
//                BucketInfoSimple(
//                    type = "BASIC",
//                    id = "3",
//                    title = "Dday가 있는 애22233",
//                    currentCount = 5,
//                    goalCount = 10,
//                    dDay = -10,
//                    status = BucketStatus.PROGRESS,
//                    detailType = DetailType.MY_OPEN
//                ),
//                BucketInfoSimple(
//                    type = "BASIC",
//                    id = "3",
//                    title = "Dday가 있는 애22233",
//                    currentCount = 5,
//                    goalCount = 10,
//                    dDay = -10,
//                    status = BucketStatus.COMPLETE,
//                    detailType = DetailType.MY_OPEN
//                ),
//                BucketInfoSimple(
//                    type = "BASIC",
//                    id = "3",
//                    title = "Dday가 있는 애22233",
//                    currentCount = 5,
//                    goalCount = 10,
//                    dDay = -10,
//                    status = BucketStatus.COMPLETE,
//                    detailType = DetailType.MY_OPEN
//                ),
//                BucketInfoSimple(
//                    type = "BASIC",
//                    id = "3",
//                    title = "Dday가 있는 애22233",
//                    currentCount = 5,
//                    goalCount = 10,
//                    dDay = -10,
//                    status = BucketStatus.COMPLETE,
//                    detailType = DetailType.MY_OPEN
//                ),
//                BucketInfoSimple(
//                    type = "BASIC",
//                    id = "3",
//                    title = "Dday가 있는 애22233",
//                    currentCount = 5,
//                    goalCount = 10,
//                    dDay = -10,
//                    status = BucketStatus.COMPLETE,
//                    detailType = DetailType.MY_OPEN
//                ),
//                BucketInfoSimple(
//                    type = "BASIC",
//                    id = "3",
//                    title = "Dday가 있는 애22233",
//                    currentCount = 5,
//                    goalCount = 10,
//                    dDay = -10,
//                    status = BucketStatus.COMPLETE,
//                    detailType = DetailType.MY_OPEN
//                ),
//                BucketInfoSimple(
//                    type = "BASIC",
//                    id = "3",
//                    title = "Dday가 있는 애22233",
//                    currentCount = 5,
//                    goalCount = 10,
//                    dDay = -10,
//                    status = BucketStatus.COMPLETE,
//                    detailType = DetailType.MY_OPEN
//                ),
//                BucketInfoSimple(
//                    type = "BASIC",
//                    id = "3",
//                    title = "Dday가 있는 애22233",
//                    currentCount = 5,
//                    goalCount = 10,
//                    dDay = -10,
//                    status = BucketStatus.COMPLETE,
//                    detailType = DetailType.MY_OPEN
//                ),
//                BucketInfoSimple(
//                    type = "BASIC",
//                    id = "3",
//                    title = "Dday가 있는 애22233",
//                    currentCount = 5,
//                    goalCount = 10,
//                    dDay = -10,
//                    status = BucketStatus.COMPLETE,
//                    detailType = DetailType.MY_OPEN
//                ),
//                BucketInfoSimple(
//                    type = "BASIC",
//                    id = "3",
//                    title = "Dday가 있는 애22233",
//                    currentCount = 5,
//                    goalCount = 10,
//                    dDay = -10,
//                    status = BucketStatus.COMPLETE,
//                    detailType = DetailType.MY_OPEN
//                )
//            )
//        )

    }

    override suspend fun loadCategoryList(): List<CategoryInfo> {
        // return berryBucketApi.loadCategoryList()
        return listOf(
            CategoryInfo(
                id = 1,
                name = "여행",
                bucketlistCount = "20"
            ),
            CategoryInfo(
                id = 1,
                name = "아주아주 맛있는 것을 먹으러 다니는 거야 냠냠쩝쩝 하면서 룰루리랄라 크크루삥봉",
                bucketlistCount = "10"
            ),
            CategoryInfo(
                id = 1,
                name = "제주도여행을 갈거야",
                bucketlistCount = "3"
            )
        )

    }

    override suspend fun loadMyState(): MyState {
        return berryBucketApi.loadMyState()
    }
}