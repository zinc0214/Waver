package com.zinc.waver.ui_more.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zinc.waver.ui.viewmodel.CommonViewModel
import com.zinc.waver.ui_more.models.MyWaveInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyWaveManageViewModel @Inject constructor() : CommonViewModel() {

    private val _myWaveInfo = MutableLiveData<MyWaveInfo>()
    val myWaveInfo: LiveData<MyWaveInfo> get() = _myWaveInfo

    fun loadMyWaveInfo() {
        _myWaveInfo.value = MyWaveInfo(
            badgeCount = 5685,
            likedCount = 2627,
            bucketCount = 6723,
            badgeUrl = "",
            badgeTitle = "Title",
            badgeList = buildList {
                add(MyWaveInfo.BadgeInfo(url = "", name = "요리", grade = 0))
                add(MyWaveInfo.BadgeInfo(url = "", name = "취미", grade = 1))
                add(MyWaveInfo.BadgeInfo(url = "", name = "요리", grade = 2))

                repeat(20) {
                    add(
                        MyWaveInfo.BadgeInfo(
                            url = "",
                            name = "요리$it",
                            grade = it
                        )
                    )
                }
            }
        )
    }
}