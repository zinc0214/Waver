package com.zinc.waver.ui_more

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.zinc.waver.ui_more.components.AppInfoMoreItemsView
import com.zinc.waver.ui_more.components.AppInfoTitle
import com.zinc.waver.ui_more.components.AppVersionInfo
import com.zinc.waver.ui_more.models.AppInfoItemType

@Composable
fun AppInfoScreen(
    onBackClicked: () -> Unit,
    moreItemClicked: (AppInfoItemType) -> Unit
) {

    val viewModel: AppInfoViewModel = hiltViewModel()
    viewModel.loadAppVersion()
    val appVersion by viewModel.appVersion.observeAsState()


    Column(modifier = Modifier.fillMaxSize()) {
        AppInfoTitle {
            onBackClicked()
        }

        appVersion?.let { version ->
            AppVersionInfo(appVersion = version, isLately = false, gotoUpdate = {})
        }

        AppInfoMoreItemsView {
            moreItemClicked(it)
        }
    }
}