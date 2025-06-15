package com.zinc.domain.usecases.other

import com.zinc.domain.repository.OtherRepository
import javax.inject.Inject

class LoadOtherInfo @Inject constructor(
    private val otherRepository: OtherRepository
) {
    suspend operator fun invoke(userId: String) = otherRepository.loadOtherHome(userId)

//    suspend operator fun invoke(userId: String): OtherProfileLoad? {
//
//        var otherProfileLoad: OtherProfileLoad? = null
//        coroutineScope {
//            launch {
//
//                var profileResponse: ProfileResponse? = null
//                var bucketListResponse: OtherBucketListResponse? = null
//                var followResponse: OtherFollowDataResponse? = null
//
//                joinAll(launch {
//                    profileResponse = otherRepository.loadOtherProfile(userId)
//                }, launch {
//                    bucketListResponse = otherRepository.loadOtherBucketList(userId)
//                }, launch {
//                    followResponse = otherRepository.loadOtherFollow(userId)
//                })
//
//                Log.e(
//                    "ayhan",
//                    "LoadOtherInfo : $profileResponse , $bucketListResponse, $followResponse"
//                )
//                otherProfileLoad =
//                    if (profileResponse?.success == true && bucketListResponse?.success == true && followResponse?.success == true) {
//
//                        val profile = profileResponse?.data!!
//                        val bucketList = bucketListResponse?.data!!
//                        val follow = followResponse?.data!!
//
//                        OtherProfileLoad(
//                            isSuccess = true,
//                            data = OtherProfileHomeData(
//                                profile = TopProfile(
//                                    isFollowed = profile.followYn?.isYes() ?: false,
//                                    name = profile.name,
//                                    imgUrl = profile.imgUrl,
//                                    percent = bucketList.completedCount.toFloat() / bucketList.totalCount.toFloat(),
//                                    badgeImgUrl = profile.badgeImgUrl,
//                                    badgeTitle = profile.badgeTitle,
//                                    bio = profile.bio,
//                                    followerCount = follow.followerCount.toString(),
//                                    followingCount = follow.followingCount.toString()
//                                ), bucketList = bucketList.bucketlist.map {
//                                    OtherProfileHomeData.OtherBucketInfo(
//                                        title = it.title,
//                                        bucketId = it.id,
//                                        isProgress = it.status == BucketStatus.PROGRESS
//                                    )
//                                }
//                            )
//                        )
//                    } else {
//                        OtherProfileLoad(
//                            isSuccess = false,
//                            data = null
//                        )
//                    }
//            }
//        }
//
//        return otherProfileLoad
//    }
}