package com.zinc.waver.ui_more.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.domain.usecases.more.LoadMyBadgeInfo
import com.zinc.domain.usecases.more.LoadMyWaveInfo
import com.zinc.domain.usecases.more.UpdateMyBadge
import com.zinc.waver.ui.viewmodel.CommonViewModel
import com.zinc.waver.ui_more.models.MyWaverTotalInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyWaveManageViewModel @Inject constructor(
    private val loadMyBadgeInfo: LoadMyBadgeInfo,
    private val loadMyWaveInfo: LoadMyWaveInfo,
    private val updateMyBadge: UpdateMyBadge
) : CommonViewModel() {

    private val _myWaverInfo = MutableLiveData<MyWaverTotalInfo>()
    val myWaverInfo: LiveData<MyWaverTotalInfo> get() = _myWaverInfo

    private val _loadFail = MutableLiveData<Boolean>()
    val loadFail: LiveData<Boolean> get() = _loadFail

    private val _updateSucceed = MutableLiveData<Boolean>()
    val updateSucceed: LiveData<Boolean> get() = _updateSucceed

    fun loadMyWaveInfo() {
        viewModelScope.launch(ceh(_loadFail, true)) {

            val badgeJob = async { loadMyBadgeInfo.invoke() }
            val infoJob = async { loadMyWaveInfo.invoke() }

            try {
                val badgeInfo = badgeJob.await().data
                val waveInfo = infoJob.await().data

                // 두 API 결과가 모두 성공했을 때
                _myWaverInfo.value = MyWaverTotalInfo(
                    totalBadgeCount = waveInfo.totalBadgeCount,
                    totalLikeCount = waveInfo.totalLikeCount,
                    totalBucketCount = waveInfo.totalBucketCount,
                    badgeImgUrl = waveInfo.badgeImgUrl,
                    badges = badgeInfo

                )

            } catch (e: Exception) {
                // 하나라도 실패하면 이쪽으로
                _loadFail.value = false
            }
        }
    }

    fun updateMySelectBadge(badgeId: Int) {
        _updateSucceed.value = false

        viewModelScope.launch(ceh(_loadFail, true)) {
            val result = updateMyBadge.invoke(badgeId)
            Log.e("ayhan", "updateMySelectBadge: $result, $badgeId")
            _updateSucceed.value = result.success
        }
    }
}