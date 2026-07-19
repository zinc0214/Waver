package com.zinc.waver.ui.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.zinc.common.models.ProfileInfo
import com.zinc.datastore.login.PreferenceDataStoreModule
import com.zinc.domain.models.BillingCycle
import com.zinc.domain.models.SubscriptionStartRequest
import com.zinc.domain.usecases.detail.LoadProfileInfo
import com.zinc.domain.usecases.more.StartSubscription
import com.zinc.waver.ui.viewmodel.CommonViewModel
import com.zinc.waver.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val preferenceDataStoreModule: PreferenceDataStoreModule,
    private val loadProfileInfo: LoadProfileInfo,
    private val startSubscription: StartSubscription
) : CommonViewModel() {
    private val _logoutSucceed = SingleLiveEvent<Boolean>()
    val logoutSucceed: LiveData<Boolean> get() = _logoutSucceed

    private val _doNothing = SingleLiveEvent<Nothing>()

    // 이번 세션에 서버로 구독 시작을 이미 알린 subscribeId(purchaseToken) 목록
    private val notifiedSubscribeIds = mutableSetOf<String>()

    fun logout() {
        viewModelScope.launch {
            preferenceDataStoreModule.clearLoginEmail()
            _logoutSucceed.value = true
        }
    }

    fun loadProfileInfo() {
        viewModelScope.launch(ceh(_doNothing, null)) {
            val response = loadProfileInfo.invoke(true, null)
            if (response.success) {
                Log.e("ayhan", "loadProfileInfo: ${response.data}")
                val isWaverUser = response.data.premiumStatus == ProfileInfo.PremiumStatus.ACTIVE
                updateWaverPlus(isWaverUser)
            }
        }
    }

    fun updateWaverPlus(purchased: Boolean) {
        viewModelScope.launch {
            preferenceDataStoreModule.setHasWaverPlus(purchased)
        }
    }

    // 구독 완료(구매/앱 시작 시 활성 구독 감지) 시 서버에 구독 시작을 알린다.
    // 앱 시작 시 YEAR/MONTH 두 설정이 같은 구매를 매칭하므로, 세션당 같은 subscribeId는 한 번만 전송한다.
    // 서버에 전송이 성공하면 그 결과를 DataStore(hasWaverPlus)에도 반영한다.
    fun notifySubscriptionStarted(billingCycle: BillingCycle, subscribeId: String) {
        if (!notifiedSubscribeIds.add(subscribeId)) return
        viewModelScope.launch(ceh(_doNothing, null)) {
            val response = startSubscription(
                SubscriptionStartRequest(
                    billingCycle = billingCycle,
                    subscribeId = subscribeId
                )
            )
            if (response.success) {
                preferenceDataStoreModule.setHasWaverPlus(true)
            }
        }
    }
}
