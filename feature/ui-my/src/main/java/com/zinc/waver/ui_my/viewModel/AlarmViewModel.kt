package com.zinc.waver.ui_my.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zinc.common.models.AlarmItem
import com.zinc.common.models.AlarmList
import com.zinc.common.models.AlarmType
import com.zinc.domain.usecases.alarm.LoadAlarmList
import com.zinc.waver.ui.viewmodel.CommonViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val loadAlarmList: LoadAlarmList,
) : CommonViewModel() {

    private val _alarmList = MutableLiveData<AlarmList>()
    val alarmList: LiveData<AlarmList> get() = _alarmList

    fun loadAlarmList() {
        _alarmList.value = AlarmList(
            alarmList = buildList {
                add(
                    AlarmItem(
                        type = AlarmType.COMMENT,
                        title = "맹꽁이 조아시러유님이 회원님의 버킷리스트를 좋아합니다.",
                        memberImg = null,
                        bucketId = null,
                        memberId = null
                    )
                )

                add(
                    AlarmItem(
                        type = AlarmType.LIKE,
                        title = "<b>안녕안영</b>님이 회원님의 버킷리스트를 좋아합니다. 이렇게 하면 그냥 두줄이 되나",
                        memberImg = null,
                        bucketId = null,
                        memberId = null
                    )
                )

                add(
                    AlarmItem(
                        type = AlarmType.DDAY,
                        title = "<font color=\"#ffad00\"><b>디데이가 7일 남은 버킷리스트</b></font>가 있습니다.<br><font color=\"#222222\">한라봉 따먹기</font>",
                        memberImg = null,
                        bucketId = null,
                        memberId = null
                    )
                )
            }
        )
//        accessToken.value?.let {
//            viewModelScope.launch {
//                _alarmList.value = loadAlarmList.invoke(it)
//            }.runCatching {
//
//            }
//        }
    }
}