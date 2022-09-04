package com.zinc.berrybucket.ui.presentation.report

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.zinc.berrybucket.ui.design.theme.BaseTheme
import com.zinc.berrybucket.ui.design.util.Keyboard
import com.zinc.berrybucket.ui.design.util.keyboardAsState
import com.zinc.common.models.ReportInfo
import com.zinc.common.models.ReportItems


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ReportScreen(
    reportInfo: ReportInfo, backPress: () -> Unit
) {
    val viewModel: ReportViewModel = hiltViewModel()
    viewModel.loadReportItmes()

    val reportItems by viewModel.reportItemList.observeAsState()

    BaseTheme {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (topAppBar, content, bottomButton) = createRefs()

            val scrollState = rememberLazyListState()
            //   val isScrolled = scrollState.firstVisibleItemIndex != 0
            val keyboardController = LocalSoftwareKeyboardController.current
            val isKeyboardStatus by keyboardAsState()
            var etcText by remember { mutableStateOf("") }

            val isScrolled by remember {
                derivedStateOf {
                    scrollState.firstVisibleItemIndex != 0
                }
            }

            ReportTopAppBar(modifier = Modifier
                .fillMaxWidth()
                .constrainAs(topAppBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }, isDividerVisible = isScrolled, backButtonClicked = {
                backPress()
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

                    }
                )
            }
        }
    }
}