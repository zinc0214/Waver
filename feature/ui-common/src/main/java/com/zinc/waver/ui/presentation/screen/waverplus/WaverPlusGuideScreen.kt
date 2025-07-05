package com.zinc.waver.ui.presentation.screen.waverplus

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zinc.waver.ui.design.theme.Gray1
import com.zinc.waver.ui.design.theme.Gray11
import com.zinc.waver.ui.design.theme.Gray3
import com.zinc.waver.ui.presentation.component.IconButton
import com.zinc.waver.ui.presentation.model.WaverPlusType
import com.zinc.waver.ui.viewmodel.CommonViewModel
import com.zinc.waver.util.DpToPx
import kotlinx.coroutines.launch
import com.zinc.waver.ui_common.R as CommonR

@Composable
fun WaverPlusGuideScreen(
    onBackPressed: () -> Unit,
    inAppBillingShow: (WaverPlusType) -> Unit,
    viewModel: CommonViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var scrollPosition by remember { mutableIntStateOf(0) }
    val locale = context.resources.configuration.locales.get(0)

    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.value }
            .collect { position ->
                scrollPosition = position
            }
    }

    val blueIsGoneOffset = DpToPx(294f)

    val closeButtonTint by animateColorAsState(
        targetValue = if (scrollPosition > blueIsGoneOffset) Gray11 else Gray1,
        label = "closeButtonTint"
    )
    val closeButtonBg by animateColorAsState(
        targetValue = if (scrollPosition > blueIsGoneOffset) Gray1 else Color(0xff2375e9),
        label = "closeButtonBg"
    )

    val bottomSheetScaffoldState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden, skipHalfExpanded = true
    )
    val coroutineScope = rememberCoroutineScope()
    val showInAppBillingBottomSheet: (Boolean) -> Unit = {
        coroutineScope.launch {
            if (it) {
                bottomSheetScaffoldState.show()
            } else {
                bottomSheetScaffoldState.hide()
            }
        }
    }

    ModalBottomSheetLayout(
        modifier = Modifier
            .background(closeButtonBg)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding(),
        sheetState = bottomSheetScaffoldState,
        sheetContent = {
            WavePlusBottomSheet(modifier = Modifier.fillMaxWidth(), selectedItem = {
                inAppBillingShow(it)
                showInAppBillingBottomSheet(false)
            })
        },
        sheetShape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .background(closeButtonBg)
                .statusBarsPadding()
                .fillMaxSize()
                .background(Gray1)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
            ) {

                WavePlusTopView()

                WavePlusPayView(locale)

                Spacer(modifier = Modifier.height(45.dp))

                WavePlusPayJoinButtonView({
                    showInAppBillingBottomSheet(true)
                })
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = closeButtonBg)
            ) {
                Column {
                    IconButton(
                        image = CommonR.drawable.btn_40_close,
                        contentDescription = stringResource(id = CommonR.string.closeDesc),
                        modifier = Modifier
                            .padding(start = 14.dp, top = 8.dp, bottom = 8.dp)
                            .size(40.dp),
                        colorFilter = ColorFilter.tint(color = closeButtonTint),
                        onClick = { onBackPressed() })

                    if (scrollPosition > blueIsGoneOffset) {
                        Divider(thickness = 1.dp, color = Gray3)
                    }
                }

            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
private fun WavePlusGuidePreview() {
    WaverPlusGuideScreen({}, inAppBillingShow = {})
}