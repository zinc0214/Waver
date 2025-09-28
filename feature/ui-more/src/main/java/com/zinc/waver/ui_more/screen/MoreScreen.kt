package com.zinc.waver.ui_more.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.zinc.waver.ui.design.theme.Gray10
import com.zinc.waver.ui.presentation.ListPopupView
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.presentation.component.PopUpView
import com.zinc.waver.ui.presentation.component.dialog.ApiFailDialog
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.ui_more.R
import com.zinc.waver.ui_more.components.MoreItemsView
import com.zinc.waver.ui_more.components.MoreTitleView
import com.zinc.waver.ui_more.components.MoreTopProfileView
import com.zinc.waver.ui_more.components.WaverClubLabelView
import com.zinc.waver.ui_more.models.MoreItemType
import com.zinc.waver.ui_more.viewModel.MoreViewModel
import com.zinc.waver.ui_common.R as CommonR

@Composable
fun MoreScreen(
    modifier: Modifier = Modifier,
    moreItemClicked: (MoreItemType) -> Unit,
    goToBack: () -> Unit
) {
    var logoutPopupShow by remember { mutableStateOf(false) }
    var csPopupShow by remember { mutableStateOf(false) }
    val viewModel: MoreViewModel = hiltViewModel()

    val profileInfoAsState by viewModel.profileInfo.observeAsState()
    val loadProfileFail by viewModel.profileLoadFail.observeAsState()
    val hasWaverPlus by viewModel.hasWaverPlus.observeAsState()
    val email by viewModel.loginEmail.observeAsState()

    val showApiFailDialog = remember { mutableStateOf(false) }
    val profileInfo = remember { mutableStateOf(profileInfoAsState) }

    LaunchedEffect(Unit) {
        viewModel.loadMyProfile()
        viewModel.checkHasWaverPlus()
        viewModel.loadLoginEmail()
    }

    LaunchedEffect(key1 = loadProfileFail) {
        showApiFailDialog.value = loadProfileFail ?: false
    }

    LaunchedEffect(key1 = profileInfoAsState) {
        profileInfo.value = profileInfoAsState
    }

    Column(modifier = modifier.statusBarsPadding()) {
        MoreTitleView()

        profileInfo.value?.let { info ->
            MoreTopProfileView(
                info,
                hasWaverPlus = hasWaverPlus == true,
                goToMyWave = { moreItemClicked(MoreItemType.MY_WAVE) },
                goToProfileUpdate = { moreItemClicked(MoreItemType.PROFILE) },
                goToWavePlus = { moreItemClicked(MoreItemType.WAVE_PLUS) }
            )

            if (hasWaverPlus == false) {
                WaverClubLabelView(enterClubClick = {
                    moreItemClicked(MoreItemType.WAVE_PLUS)
                })
            }

            if (!email.isNullOrBlank()) {
                MoreItemsView(email.orEmpty()) {
                    when (it) {
                        MoreItemType.LOGOUT -> {
                            logoutPopupShow = true
                        }

                        MoreItemType.CS -> {
                            csPopupShow = true
                        }

                        else -> {
                            moreItemClicked(it)
                        }
                    }
                }
            }
        }
    }

    if (logoutPopupShow) {
        PopUpView(
            title = stringResource(R.string.logoutTitle),
            cancelText = stringResource(CommonR.string.cancel),
            positiveText = stringResource(R.string.logoutButton),
            cancelClicked = {
                logoutPopupShow = false
            },
            positiveClicked = {
                logoutPopupShow = false
                moreItemClicked.invoke(MoreItemType.LOGOUT)
            },
            onDismissRequest = { logoutPopupShow = false },
            clickable = true
        )
    }

    if (csPopupShow) {
        ListPopupView(
            modifier = Modifier,
            title = stringResource(R.string.moreMenuCs),
            onDismissRequest = {
                csPopupShow = false
            },
            clickable = true,
            listBlock = {
                Column {
                    MyText(
                        text = stringResource(R.string.csFAQ),
                        fontSize = dpToSp(14.dp),
                        color = Gray10,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    )
                    Spacer(Modifier.height(20.dp))
                    MyText(
                        text = stringResource(R.string.csServiceQuestion), fontSize = dpToSp(14.dp),
                        color = Gray10,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .clickable {
                                moreItemClicked.invoke(MoreItemType.CS_QNA)
                            }
                    )
                    Spacer(Modifier.height(32.dp))
                }
            }

        )
    }

    if (showApiFailDialog.value) {
        ApiFailDialog(
            title = stringResource(id = R.string.loadFailProfile),
            message = stringResource(id = CommonR.string.retryDesc),
            dismissEvent = {
                showApiFailDialog.value = false
                goToBack()
            })
    }
}