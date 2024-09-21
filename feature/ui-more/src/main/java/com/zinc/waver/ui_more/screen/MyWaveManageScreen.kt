package com.zinc.waver.ui_more.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.zinc.waver.ui.design.theme.Gray1
import com.zinc.waver.ui.design.theme.Gray10
import com.zinc.waver.ui.design.theme.Gray3
import com.zinc.waver.ui.design.theme.Gray8
import com.zinc.waver.ui.design.theme.Main3
import com.zinc.waver.ui.design.theme.Main4
import com.zinc.waver.ui.presentation.component.IconButton
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.util.dpToSp
import com.zinc.waver.ui_more.R
import com.zinc.waver.ui_more.models.MyWaveInfo
import com.zinc.waver.ui_more.viewModel.MyWaveManageViewModel
import com.zinc.waver.ui_common.R as CommonR

@Composable
fun MyWaveManageScreen(
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MyWaveManageViewModel = hiltViewModel()
) {
    val myWaveInfoAsState by viewModel.myWaveInfo.observeAsState()

    val myWaveInfo = remember {
        mutableStateOf(myWaveInfoAsState)
    }
    var selectedBadge: MyWaveInfo.BadgeInfo? by remember {
        mutableStateOf(null)
    }

    LaunchedEffect(key1 = myWaveInfoAsState) {
        myWaveInfo.value = myWaveInfoAsState
        selectedBadge = myWaveInfo.value?.badgeList?.firstOrNull()
    }

    if (myWaveInfo.value == null) {
        viewModel.loadMyWaveInfo()
    }

    myWaveInfo.value?.let { info ->
        Column(modifier = modifier.fillMaxSize()) {
            HeaderView(info = info) {
                onBackPressed()
            }

            MyWaveStatusView(info = info)

            Spacer(modifier = Modifier.height(36.dp))
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp)
                    .padding(bottom = 24.dp),
                color = Gray3
            )

            selectedBadge?.let { badge ->
                BadgeListView(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    badgeInfoList = info.badgeList,
                    selectedBadge = badge,
                    badgeSelected = {
                        selectedBadge = it
                    })
            }
        }
    }
}

@Composable
private fun HeaderView(info: MyWaveInfo, onBackPressed: () -> Unit) {
    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val (closeButton, title, currentBadge, logo) = createRefs()

        IconButton(
            image = CommonR.drawable.btn_40_close,
            contentDescription = stringResource(id = CommonR.string.closeDesc),
            modifier = Modifier
                .padding(start = 14.dp, top = 6.dp, bottom = 6.dp)
                .size(40.dp)
                .constrainAs(closeButton) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                },
            onClick = { onBackPressed() }
        )

        MyText(
            text = stringResource(id = R.string.myWave),
            modifier = Modifier
                .padding(start = 28.dp, top = 24.dp, end = 16.dp)
                .constrainAs(title) {
                    top.linkTo(closeButton.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(currentBadge.start)
                    width = Dimension.fillToConstraints
                },
            color = Gray10,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Bold,
            fontSize = dpToSp(dp = 24.dp)
        )

        Image(
            painter = rememberAsyncImagePainter(
                model = info.badgeUrl,
                placeholder = painterResource(CommonR.drawable.testimg),
                error = painterResource(CommonR.drawable.testimg)
            ),
            contentDescription = info.badgeTitle,
            modifier = Modifier
                .padding(top = 12.dp, end = 32.dp)
                .size(60.dp)
                .constrainAs(currentBadge) {
                    end.linkTo(parent.end)
                    top.linkTo(closeButton.bottom)
                },
            contentScale = ContentScale.FillWidth
        )

        Image(
            painter = painterResource(id = R.drawable.img_wave_plus),
            contentDescription = null,
            modifier = Modifier
                .sizeIn(60.dp)
                .constrainAs(logo) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
                .padding(top = 30.dp)
        )
    }
}

@Composable
private fun MyWaveStatusView(info: MyWaveInfo, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 29.dp, start = 28.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StatusView(count = info.badgeCount, text = stringResource(id = R.string.badgeCount))
        StatusView(count = info.likedCount, text = stringResource(id = R.string.likedCount))
        StatusView(count = info.bucketCount, text = stringResource(id = R.string.bucketCount))
    }
}

@Composable
private fun StatusView(count: Int, text: String) {
    Column(modifier = Modifier.width(80.dp)) {
        MyText(
            text = count.toString(),
            color = Gray10,
            fontSize = dpToSp(dp = 24.dp),
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        MyText(text = text, color = Gray10, fontSize = dpToSp(dp = 16.dp))
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BadgeListView(
    badgeInfoList: List<MyWaveInfo.BadgeInfo>,
    selectedBadge: MyWaveInfo.BadgeInfo,
    badgeSelected: (MyWaveInfo.BadgeInfo) -> Unit,
    modifier: Modifier
) {
    FlowRow(
        modifier = modifier
            .padding(horizontal = 28.dp)
            .verticalScroll(rememberScrollState()),
        maxItemsInEachRow = 3,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        badgeInfoList.forEach {
            BadgeView(info = it, selectedBadge = selectedBadge, badgeSelected = {
                badgeSelected(it)
            })
        }
    }
}

@Composable
private fun BadgeView(
    info: MyWaveInfo.BadgeInfo,
    selectedBadge: MyWaveInfo.BadgeInfo,
    badgeSelected: () -> Unit
) {
    val borderColor = if (info == selectedBadge) Main3 else Gray3
    val textColor = if (info == selectedBadge) Main4 else Gray10
    Box(
        modifier = Modifier
            .background(color = Gray1, shape = RoundedCornerShape(8.dp))
            .border(
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(width = 1.dp, color = borderColor)
            )
            .clickable {
                badgeSelected()
            }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 16.dp, bottom = 12.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = info.url,
                    placeholder = painterResource(CommonR.drawable.testimg),
                    error = painterResource(CommonR.drawable.testimg)
                ),
                contentDescription = info.name,
                modifier = Modifier
                    .padding(horizontal = 23.dp)
                    .size(50.dp),
                contentScale = ContentScale.Crop
            )

            MyText(
                text = info.name,
                color = textColor,
                fontSize = dpToSp(dp = 16.dp),
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 8.dp)
            )

            MyText(
                text = "${info.grade}${stringResource(id = R.string.badgeGrade)}",
                color = Gray8,
                fontSize = dpToSp(dp = 14.dp),
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun HeaderPreview() {
    HeaderView(
        info = MyWaveInfo(
            badgeCount = 5685,
            likedCount = 2627,
            bucketCount = 6723,
            badgeUrl = "",
            badgeTitle = "Title",
            badgeList = buildList {
                add(MyWaveInfo.BadgeInfo(url = "", name = "요리", grade = 0))
                add(MyWaveInfo.BadgeInfo(url = "", name = "취미", grade = 1))
                add(MyWaveInfo.BadgeInfo(url = "", name = "반려동물", grade = 3))

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
    ) {

    }

}
