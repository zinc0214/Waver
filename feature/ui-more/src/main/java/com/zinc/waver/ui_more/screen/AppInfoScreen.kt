package com.zinc.waver.ui_more.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.zinc.waver.ui.design.theme.Gray7
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.presentation.component.dialog.ApiFailDialog
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.ui_more.R
import com.zinc.waver.ui_more.components.AppInfoMoreItemsView
import com.zinc.waver.ui_more.components.AppInfoTitle
import com.zinc.waver.ui_more.components.AppVersionInfo
import com.zinc.waver.ui_more.models.AppInfoItemType
import com.zinc.waver.ui_more.viewModel.AppInfoViewModel

@Composable
fun AppInfoScreen(
    onBackClicked: () -> Unit,
    onAppInfoItemClicked: (AppInfoItemType) -> Unit,
    withdrawEvent: () -> Unit,
) {

    val viewModel: AppInfoViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        viewModel.loadAppVersion()
    }

    val appVersion by viewModel.appVersion.observeAsState()
    val withdrawSucceed by viewModel.withdrawSucceed.observeAsState()
    val requestFail by viewModel.requestFail.observeAsState()

    LaunchedEffect(key1 = withdrawSucceed) {
        if (withdrawSucceed == true) {
            withdrawEvent()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        AppInfoTitle {
            onBackClicked()
        }

        appVersion?.let { version ->
            AppVersionInfo(appVersion = version, isLately = false, gotoUpdate = {})
        }

        AppInfoMoreItemsView {
            onAppInfoItemClicked(it)
        }

        Spacer(modifier = Modifier.weight(1f))

        MyText(
            modifier = Modifier
                .padding(start = 28.dp, bottom = 32.dp)
                .clickable {
                    viewModel.deleteAccount()
                },
            text = stringResource(R.string.deleteAccountButton),
            fontSize = dpToSp(16.dp),
            color = Gray7,
            textDecoration = TextDecoration.Underline
        )
    }

    if (requestFail == true) {
        ApiFailDialog() {
            viewModel.deleteAccount()
        }
    }
}

@Preview
@Composable
private fun AppInfoScreenPreview() {
    AppInfoScreen(
        {}, {}, {}
    )
}