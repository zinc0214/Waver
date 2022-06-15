package com.zinc.berrybucket.ui.presentation.report

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.constraintlayout.compose.ConstraintLayout
import com.zinc.berrybucket.model.ReportInfo
import com.zinc.berrybucket.model.ReportItem
import com.zinc.berrybucket.model.ReportItems
import com.zinc.berrybucket.ui.compose.theme.BaseTheme
import com.zinc.berrybucket.ui.compose.util.Keyboard
import com.zinc.berrybucket.ui.compose.util.keyboardAsState


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ReportScreen(
    reportInfo: ReportInfo, backPress: () -> Unit
) {
    val reportItems: ReportItems = ReportItems(listOf(ReportItem("0", ""), ReportItem("기타", "1")))

    BaseTheme {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (topAppBar, content, bottomButton) = createRefs()

            val scrollState = rememberLazyListState()
            val isScrolled = scrollState.firstVisibleItemIndex != 0
            val keyboardController = LocalSoftwareKeyboardController.current
            val isKeyboardStatus by keyboardAsState()
            var etcText by remember { mutableStateOf("") }

            ReportTopAppBar(modifier = Modifier
                .fillMaxWidth()
                .constrainAs(topAppBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }, isDividerVisible = isScrolled, backButtonClicked = {

            })

            ReportContentView(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(content) {
                        top.linkTo(topAppBar.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                scrollState = scrollState,
                reportInfo = reportInfo,
                reportItems = reportItems,
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
                            top.linkTo(topAppBar.bottom)
                            bottom.linkTo(parent.bottom)
                            end.linkTo(parent.end)
                        },
                    reportButtonClicked = {

                    }
                )
            }
        }
    }
}