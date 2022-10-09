package com.zinc.berrybucket.ui.presentation.write

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zinc.berrybucket.CommonViewModel
import com.zinc.berrybucket.model.WriteFriend
import com.zinc.datastore.login.LoginPreferenceDataStoreModule
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WriteViewModel @Inject constructor(
    private val loginPreferenceDataStoreModule: LoginPreferenceDataStoreModule
) : CommonViewModel(loginPreferenceDataStoreModule) {

    private val _searchFriendsResult = MutableLiveData<List<WriteFriend>>()
    val searchFriendsResult: LiveData<List<WriteFriend>> get() = _searchFriendsResult


    fun addNewBucketList() {

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