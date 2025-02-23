package com.zinc.waver.ui_write.presentation

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.zinc.waver.model.WriteAddOption
import com.zinc.waver.model.WriteOpenType
import com.zinc.waver.model.WriteOption1Info
import com.zinc.waver.model.WriteOptionsType1
import com.zinc.waver.model.WriteOptionsType2
import com.zinc.waver.model.WriteTotalInfo
import com.zinc.waver.model.parseUIBucketListInfo
import com.zinc.waver.model.parseWrite1Info
import com.zinc.waver.ui.presentation.component.dialog.ApiFailDialog
import com.zinc.waver.ui.presentation.component.gridItems
import com.zinc.waver.ui.presentation.model.ActionWithActivity
import com.zinc.waver.ui.presentation.screen.waverplus.WaverPlusGuideScreen
import com.zinc.waver.ui_write.R
import com.zinc.waver.ui_write.model.WriteEvent
import com.zinc.waver.ui_write.presentation.options.ImageItem
import com.zinc.waver.ui_write.presentation.options.WriteSelectFriendsScreen
import com.zinc.waver.ui_write.presentation.options.WriteSelectKeyWordScreen
import com.zinc.waver.ui_write.viewmodel.WriteBucketListViewModel
import com.zinc.waver.util.createImageInfoWithPath
import com.zinc.waver.util.loadImageFiles
import com.zinc.waver.ui_common.R as CommonR

@Composable
fun WriteScreen2(
    modifier: Modifier = Modifier,
    writeTotalInfo: WriteTotalInfo,
    viewModel: WriteBucketListViewModel,
    goToBack: (WriteTotalInfo) -> Unit,
    event: (WriteEvent) -> Unit,
    addBucketSucceed: () -> Unit
) {

    val context = LocalContext.current

    var optionScreenShow: WriteOptionsType2? by remember { mutableStateOf(null) }
    val originKeyWords by viewModel.keywordList.observeAsState()
    val originFriendsAsState by viewModel.searchFriendsResult.observeAsState()
    val loadFailAsState by viewModel.loadFail.observeAsState()
    val addNewBucketListResult by viewModel.addNewBucketListResult.observeAsState()
    val hasWaverPlusAsState by viewModel.hasWaverPlus.observeAsState()

    val selectedKeyWords = remember { mutableStateOf(writeTotalInfo.keyWord) }
    val selectedFriends = remember { mutableStateOf(writeTotalInfo.tagFriends) }
    val selectedOpenType = remember { mutableStateOf(writeTotalInfo.writeOpenType) }
    val isScrapUsed = remember { mutableStateOf(writeTotalInfo.isScrapUsed) }
    val keyWordList = remember { mutableStateOf(originKeyWords) }
    val originFriendList = remember { mutableStateOf(originFriendsAsState) }
    val showApiFailDialog = remember { mutableStateOf(false) }
    var hasWaverPlus by remember { mutableStateOf(false) }
    val showWaverPlus = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.checkHasWaverPlus()
    }

    LaunchedEffect(originKeyWords) {
        keyWordList.value = originKeyWords
    }

    LaunchedEffect(key1 = originFriendsAsState) {
        originFriendList.value = originFriendsAsState
    }

    LaunchedEffect(key1 = loadFailAsState) {
        if (loadFailAsState?.first.isNullOrEmpty().not() && loadFailAsState?.second.isNullOrEmpty()
                .not()
        ) {
            showApiFailDialog.value = true
        }
    }

    LaunchedEffect(key1 = addNewBucketListResult) {
        if (addNewBucketListResult == true) {
            Toast.makeText(
                context,
                R.string.addBucketListSucceed,
                Toast.LENGTH_SHORT
            ).show()
            addBucketSucceed()
        }
    }

    LaunchedEffect(hasWaverPlusAsState) {
        hasWaverPlus = hasWaverPlusAsState ?: false
    }

    BackHandler(enabled = true) { // <-----
        if (optionScreenShow != null) {
            optionScreenShow = null
        } else {
            viewModel.clearData()
            goToBack(
                writeTotalInfo.copy(
                    keyWord = selectedKeyWords.value,
                    tagFriends = selectedFriends.value,
                    writeOpenType = selectedOpenType.value,
                    isScrapUsed = isScrapUsed.value
                )
            )
        }
    }

    val optionsList = mutableListOf(
        WriteAddOption(
            type = WriteOptionsType2.TAG,
            title = "키워드 추가",
            showList = selectedKeyWords.value.map { "#${it.text}" },
            dataList = selectedFriends.value.map { it.id },
            clicked = {
                optionScreenShow = it
            }),
        WriteAddOption(
            type = WriteOptionsType2.FRIENDS(isUsable = hasWaverPlus),
            title = "함께할 친구 추가하기",
            showList = selectedFriends.value.map { "@${it.nickname}" },
            dataList = selectedFriends.value.map { it.id },
            clicked = {
                val isValid = (it as WriteOptionsType2.FRIENDS).isUsable
                if (isValid) {
                    optionScreenShow = it
                } else {
                    showWaverPlus.value = true
                }

            }),
        WriteAddOption(
            type = WriteOptionsType2.OPEN,
            title = "공개설정",
            showList = listOf(selectedOpenType.value.text),
            dataList = listOf(selectedOpenType.value.text),
            showDivider = true,
            clicked = {
                optionScreenShow = it
            }),
        WriteAddOption(
            type = WriteOptionsType2.SCRAP(
                isScrapAvailable = selectedOpenType.value != WriteOpenType.PRIVATE,
                isScrapUsed = isScrapUsed.value
            ),
            title = "스크랩",
            dataList = emptyList(),
            showList = emptyList(),
            clicked = {
                optionScreenShow = null
            }),
    )

    if (optionScreenShow != null && optionScreenShow != WriteOptionsType2.OPEN) {
        when (optionScreenShow) {
            WriteOptionsType2.TAG -> {
                LaunchedEffect(Unit) {
                    viewModel.loadKeyword()
                }
                WriteSelectKeyWordScreen(
                    closeClicked = { optionScreenShow = null },
                    originKeyWord = keyWordList.value.orEmpty(),
                    selectedKeyWords = selectedKeyWords.value,
                    addKeyWordClicked = {
                        selectedKeyWords.value = it
                        optionScreenShow = null
                    }
                )
            }

            is WriteOptionsType2.FRIENDS -> {
                LaunchedEffect(Unit) {
                    viewModel.loadFriends()
                }
                WriteSelectFriendsScreen(
                    closeClicked = { optionScreenShow = null },
                    selectedFriends = selectedFriends.value,
                    originFriends = originFriendList.value.orEmpty(),
                    addFriendsClicked = {
                        selectedFriends.value = it
                        if (selectedFriends.value.isNotEmpty() && selectedOpenType.value == WriteOpenType.PRIVATE) {
                            selectedOpenType.value = WriteOpenType.PUBLIC
                            Toast.makeText(
                                context,
                                R.string.optionIfFriendsNotEmptyOpenPrivateDetect,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        optionScreenShow = null
                    }
                )
            }
        }
    } else {
        ConstraintLayout(
            modifier = modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {

            WriteScreen2ContentView(
                viewModel = viewModel,
                context = context,
                writeTotalInfo = writeTotalInfo,
                optionsList = optionsList,
                goToBack = {
                    viewModel.clearData()
                    goToBack(
                        writeTotalInfo.copy(
                            writeOpenType = selectedOpenType.value,
                            keyWord = selectedKeyWords.value,
                            tagFriends = selectedFriends.value,
                            isScrapUsed = isScrapUsed.value
                        )
                    )
                },
                selectedOpenType = selectedOpenType,
                optionValueChanged = { changedOption ->
                    val changedIndex = optionsList.indexOfFirst { it.type == changedOption.type }
                    optionsList[changedIndex] = changedOption

                    if (changedOption.type is WriteOptionsType2.SCRAP) {
                        isScrapUsed.value =
                            (changedOption.type as WriteOptionsType2.SCRAP).isScrapUsed
                    }
                }
            )

            if (optionScreenShow == WriteOptionsType2.OPEN) {
                SelectOpenTypePopup(
                    isPrivateAvailable = selectedFriends.value.isEmpty(),
                    onDismissRequest = {
                        optionScreenShow = null
                    },
                    typeSelected = {
                        selectedOpenType.value = it
                        optionScreenShow = null
                        val changedIndex =
                            optionsList.indexOfFirst { option -> option.type is WriteOptionsType2.SCRAP }
                        val scarpOption = optionsList[changedIndex]
                        (scarpOption.type as WriteOptionsType2.SCRAP).isScrapAvailable =
                            it != WriteOpenType.PRIVATE
                        optionsList[changedIndex] = scarpOption
                    }
                )
            }
        }

        if (showApiFailDialog.value) {
            ApiFailDialog(
                title = loadFailAsState?.first.orEmpty(),
                message = loadFailAsState?.second.orEmpty(),
                dismissEvent = {
                    showApiFailDialog.value = false
                })
        }

        AnimatedVisibility(showWaverPlus.value, enter = fadeIn(), exit = fadeOut()) {
            if (showWaverPlus.value) {
                WaverPlusGuideScreen(onBackPressed = {
                    showWaverPlus.value = false
                }) { type ->
                    event.invoke(WriteEvent.ActivityAction(ActionWithActivity.InAppBilling(type)))
                }
            }
        }
    }
}


@Composable
private fun WriteScreen2ContentView(
    modifier: Modifier = Modifier,
    context: Context,
    viewModel: WriteBucketListViewModel,
    writeTotalInfo: WriteTotalInfo,
    optionsList: List<WriteAddOption>,
    goToBack: (WriteTotalInfo) -> Unit,
    selectedOpenType: MutableState<WriteOpenType>,
    optionValueChanged: (WriteAddOption) -> Unit
) {

    val writeInfo1 = writeTotalInfo.parseWrite1Info()
    val imagesInfo = loadImageFiles(context, images = writeInfo1.getImagesPaths())

    Log.e("ayhan", "imagesInfo :${imagesInfo}")

    ConstraintLayout(modifier = modifier.fillMaxSize()) {

        val (appBar, contents) = createRefs()
        WriteAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(appBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            leftIcon = CommonR.drawable.btn_40_back,
            nextButtonClickable = true,
            rightText = R.string.writeGoToFinish,
            clickEvent = { it ->
                when (it) {
                    WriteAppBarClickEvent.CloseClicked -> {
                        val newImage = mutableListOf<String>()
                        writeInfo1.getImagesPaths().let { infos ->
                            infos.forEach { info ->
                                newImage.add(info)
                            }
                        }
                        val option =
                            writeInfo1.options.find { option -> option.type() == WriteOptionsType1.IMAGE }
                        val optionUpdate = writeInfo1.options.toMutableList()
                        optionUpdate.remove(option)
                        optionUpdate.add(
                            WriteOption1Info.Images(newImage)
                        )
                        goToBack(writeTotalInfo.copy(options = optionUpdate))
                        // TODO : 이미지
                    }

                    WriteAppBarClickEvent.NextClicked -> {
                        Log.e("ayhan", "optionList: $optionsList")
                        val scrapOption =
                            (optionsList.find { it.type is WriteOptionsType2.SCRAP })?.type as WriteOptionsType2.SCRAP
                        viewModel.addNewBucketList(
                            parseUIBucketListInfo(
                                bucketId = writeTotalInfo.bucketId,
                                title = writeTotalInfo.title,
                                options = writeTotalInfo.options,
                                writeOpenType = selectedOpenType.value,
                                imageFiles = imagesInfo.map { it.file },
                                keyWord = optionsList.find { it.type == WriteOptionsType2.TAG }?.dataList.orEmpty(),
                                tagFriends = optionsList.find { it.type is WriteOptionsType2.FRIENDS }?.dataList.orEmpty(),
                                isScrapAvailable = scrapOption.isScrapUsed
                            ),
                            isForUpdate = writeTotalInfo.isForUpdate
                        )
                    }
                }
            })

        val state = rememberLazyGridState()

        LazyColumn(
            modifier = Modifier
                .constrainAs(contents) {
                    top.linkTo(appBar.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }
                .scrollable(
                    state = state,
                    orientation = Orientation.Vertical
                ),
            state = rememberLazyListState()
        ) {
            item {
                WriteTitleView(
                    modifier = Modifier.padding(
                        bottom = if (writeInfo1.getImagesPaths().isEmpty()) 150.dp else 32.dp
                    ),
                    title = writeTotalInfo.title
                )
            }

            item {
                gridItems(
                    data = writeInfo1.getImagesPaths(),
                    maxRow = 3,
                    modifier = Modifier
                        .padding(horizontal = 28.dp)
                        .padding(top = 28.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalSpace = 28.dp,
                    itemContent = {
                        val info = createImageInfoWithPath(context = context, path = it, index = 0)
                        ImageItem(imageInfo = info)
                    },
                    emptyContent = {
                        Spacer(modifier = Modifier.size(80.dp))
                    })
            }

            item {
                // TODO : Flexible 로 수정 필요
                optionsList.forEach {
                    WriteAddOptionView(
                        modifier = Modifier.fillMaxWidth(),
                        option = it,
                        isLastItem = it == optionsList.last(),
                        optionClicked = { it.clicked(it.type) },
                        optionValueChanged = { option ->
                            optionValueChanged(option)
                        }
                    )
                }
            }
        }
    }
}