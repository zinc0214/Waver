package com.zinc.waver.ui.presentation.report

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.zinc.common.models.ReportItem
import com.zinc.common.models.ReportItems
import com.zinc.waver.R
import com.zinc.waver.model.ReportClickEvent
import com.zinc.waver.model.ReportInfo
import com.zinc.waver.ui.design.theme.Gray1
import com.zinc.waver.ui.design.util.Keyboard
import com.zinc.waver.ui.design.util.keyboardAsState
import com.zinc.waver.ui.presentation.component.TitleIconType
import com.zinc.waver.ui.presentation.component.TitleView


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ReportScreen(
    reportInfo: ReportInfo, backPress: (Boolean) -> Unit
) {
    val viewModel: ReportViewModel = hiltViewModel()
    viewModel.loadReportItmes()

    val reportItems by viewModel.reportItemList.observeAsState()
    val commentReportSucceedAsState by viewModel.commentReportSucceed.observeAsState()
    val apiLoadFailAsState by viewModel.requestFail.observeAsState()

    LaunchedEffect(key1 = commentReportSucceedAsState) {
        if (commentReportSucceedAsState == true) {
            backPress(true)
        }
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray1)
    ) {
        val (topAppBar, content, bottomButton) = createRefs()

        val scrollState = rememberLazyListState()
        val keyboardController = LocalSoftwareKeyboardController.current
        val isKeyboardStatus by keyboardAsState()
        var etcText by remember { mutableStateOf("") }
        val selectedItem: MutableState<ReportItem?> = remember {
            mutableStateOf(null)
        }

        val isScrolled by remember {
            derivedStateOf {
                scrollState.firstVisibleItemIndex != 0
            }
        }

        TitleView(modifier = Modifier
            .fillMaxWidth()
            .constrainAs(topAppBar) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            title = stringResource(id = R.string.commentReportTitle),
            leftIconType = TitleIconType.BACK,
            isDividerVisible = isScrolled,
            onLeftIconClicked = {
                backPress(false)
            })

        ReportContentView(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(content) {
                    top.linkTo(topAppBar.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }
                .padding(bottom = 70.dp),
            scrollState = scrollState,
            reportInfo = reportInfo,
            reportItems = reportItems ?: ReportItems(items = emptyList()),
            keyboardController = keyboardController,
            onImeAction = {
                etcText = it
            },
            clickEvent = {
                when (it) {
                    is ReportClickEvent.ReportItemSelected -> {
                        selectedItem.value = it.reportItem
                    }
                }

            }
        )

        if (isKeyboardStatus == Keyboard.Closed) {
            ReportButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(bottomButton) {
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
                    .padding(bottom = 28.dp),
                reportButtonClicked = {
                    val reason =
                        etcText.ifEmpty { selectedItem.value?.text.orEmpty() }
                    viewModel.requestReportComment(reportInfo.id, reason)
                }
            )
        }
    }
}