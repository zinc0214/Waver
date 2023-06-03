package com.zinc.berrybucket.ui_write.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.berrybucket.CommonViewModel
import com.zinc.berrybucket.model.UIAddBucketListInfo
import com.zinc.berrybucket.model.WriteFriend
import com.zinc.common.models.AddBucketListRequest
import com.zinc.datastore.login.LoginPreferenceDataStoreModule
import com.zinc.domain.usecases.write.AddNewBucketList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WriteViewModel @Inject constructor(
    private val loginPreferenceDataStoreModule: LoginPreferenceDataStoreModule,
    private val addNewBucketList: AddNewBucketList
) : CommonViewModel(loginPreferenceDataStoreModule) {

    private val _searchFriendsResult = MutableLiveData<List<WriteFriend>>()
    val searchFriendsResult: LiveData<List<WriteFriend>> get() = _searchFriendsResult

    private val _addNewBucketListResult = MutableLiveData<Boolean>()
    val addNewBucketListResult: LiveData<Boolean> get() = _addNewBucketListResult


    fun addNewBucketList(writeInfo: UIAddBucketListInfo, isSucceed: (Boolean) -> Unit) {
        viewModelScope.launch {
            runCatching {
                accessToken.value?.let { accessToken ->
                    addNewBucketList.invoke(
                        accessToken,
                        addBucketListRequest = AddBucketListRequest(
                            bucketType = writeInfo.bucketType,
                            exposureStatus = writeInfo.exposureStatus,
                            title = writeInfo.title,
                            content = writeInfo.content,
                            memo = writeInfo.memo,
                            tags = writeInfo.tags,
                            friendUserIds = writeInfo.friendUserIds,
                            scrapYn = writeInfo.scrapYn,
                            images = writeInfo.images,
                            targetDate = writeInfo.targetDate,
                            goalCount = writeInfo.goalCount,
                            categoryId = writeInfo.categoryId
                        )
                    )
                    isSucceed(true)
                }
            }.getOrElse {
                isSucceed(false)
            }
        }
    }

    fun clearFriendsData() {
        _searchFriendsResult.value = emptyList()
    }

    fun searchFriends(searchName: String) {
        _searchFriendsResult.value = buildList {
            add(WriteFriend("1", "ㅁㅁㅁ", "유저 닉네임 11"))
            add(WriteFriend("3", "ㅁㅁㅁ", "유저 닉네임 12"))
            add(WriteFriend("2", "ㅁㅁㅁ", "유저 닉네임 133"))
            add(WriteFriend("4", "ㅁㅁㅁ", "유저 닉네임 01"))
            add(WriteFriend("5", "ㅁㅁㅁ", "유저 1"))
            add(WriteFriend("6", "ㅁㅁㅁ", "유1"))
            add(WriteFriend("7", "ㅁㅁㅁ", "유5541"))
            add(WriteFriend("8", "ㅁㅁㅁ", "유55dd541"))
            add(WriteFriend("9", "ㅁㅁㅁ", "유5541"))
            add(WriteFriend("10", "ㅁㅁㅁ", "유d임 55541"))
        }
    }

}