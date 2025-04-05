package com.zinc.waver.ui_more.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.domain.usecases.more.LoadBlockedUsers
import com.zinc.domain.usecases.more.RequestBlockUserRelease
import com.zinc.waver.ui.viewmodel.CommonViewModel
import com.zinc.waver.ui_more.models.BlockMemberData
import com.zinc.waver.ui_more.models.toUIModel
import com.zinc.waver.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlockSettingViewModel @Inject constructor(
    private val loadBlockedUsers: LoadBlockedUsers,
    private val requestBlockUserRelease: RequestBlockUserRelease
) : CommonViewModel() {

    private val _loadError = SingleLiveEvent<Boolean>()
    val loadError: LiveData<Boolean> get() = _loadError

    private val _blockedUserList = MutableLiveData<List<BlockMemberData>>()
    val blockedUserList: LiveData<List<BlockMemberData>> get() = _blockedUserList

    fun loadBlockUserList() {
        _loadError.value = false

        viewModelScope.launch(ceh(_loadError, true)) {
            loadBlockedUsers.invoke().apply {
                if (success) {
                    Log.e("ayhan", "blockUser : $data")
                    _blockedUserList.value = data.toUIModel()
                } else {
                    _loadError.value = true
                }
            }
        }
    }

    fun requestBlockUserRelease(userId: Int) {
        _loadError.value = false

        viewModelScope.launch(ceh(_loadError, true)) {
            val result = requestBlockUserRelease.invoke(userId)
            Log.e("ayhan", "result : $result")
            if (result.success) {
                loadBlockUserList()
            } else {
                _loadError.value = true
            }
        }
    }
}