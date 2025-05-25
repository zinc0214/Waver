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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
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
import com.zinc.waver.model.WriteFriend
import com.zinc.waver.model.WriteKeyWord
import com.zinc.waver.model.WriteOpenType
import com.zinc.waver.model.WriteOption1Info
import com.zinc.waver.model.WriteOptionsType1
import com.zinc.waver.model.WriteOptionsType2
import com.zinc.waver.model.WriteOptionsType2.FRIENDS
import com.zinc.waver.model.WriteOptionsType2.TAG.getFriendsEnableType
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
    val friendOption = remember { FRIENDS(enableType = getFriendsEnableType(hasWaverPlus)) }

    LaunchedEffect(Unit, showWaverPlus.value) {
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
        friendOption.enableType = getFriendsEnableType(hasWaverPlus)
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

            is FRIENDS -> {
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
                selectedKeywordList = selectedKeyWords.value,
                writeTotalInfo = writeTotalInfo,
                selectedFriends = selectedFriends.value,
                selectedOpenType = selectedOpenType,
                keywordSelected = {
                    optionScreenShow = WriteOptionsType2.TAG
                },
                friendsOption = friendOption,
                friendSelected = {
                    if (it == FRIENDS.EnableType.Enable) {
                        optionScreenShow = friendOption
                    } else {
                        showWaverPlus.value = true
                    }
                },
                isScrapUsed = isScrapUsed.value,
                scrapChanged = {
                    isScrapUsed.value = it
                },
                openType = selectedOpenType.value,
                openTypeSelected = {
                    optionScreenShow = WriteOptionsType2.OPEN
                },
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
            )

            if (optionScreenShow == WriteOptionsType2.OPEN) {
                SelectOpenTypePopup(
                    isPrivateAvailable = selectedFriends.value.isEmpty(),
                    onDismissRequest = {
                        optionScreenShow = null
                    },
                    typeSelected = {
                        if (selectedFriends.value.isEmpty() && it == WriteOpenType.FRIENDS_OPEN) {
                            Toast.makeText(
                                context,
                                R.string.optionIfHasNoFriends,
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            selectedOpenType.value = it
                        }
                        optionScreenShow = null
                        isScrapUsed.value = it != WriteOpenType.PRIVATE
                        friendOption.enableType =
                            if (it == WriteOpenType.PRIVATE) FRIENDS.EnableType.Disable else friendOption.enableType
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
                }, inAppBillingShow = { type ->
                    event.invoke(WriteEvent.ActivityAction(ActionWithActivity.InAppBilling(type)))
                })
            }
        }
    }
}


@Composable
private fun WriteScreen2ContentView(
    modifier: Modifier = Modifier,
    context: Context,
    viewModel: WriteBucketListViewModel,
    selectedKeywordList: List<WriteKeyWord>,
    friendsOption: FRIENDS,
    isScrapUsed: Boolean,
    selectedFriends: List<WriteFriend>,
    writeTotalInfo: WriteTotalInfo,
    goToBack: (WriteTotalInfo) -> Unit,
    selectedOpenType: MutableState<WriteOpenType>,
    keywordSelected: () -> Unit,
    friendSelected: (FRIENDS.EnableType) -> Unit,
    scrapChanged: (Boolean) -> Unit,
    openType: WriteOpenType,
    openTypeSelected: () -> Unit,
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
                        viewModel.addNewBucketList(
                            parseUIBucketListInfo(
                                bucketId = writeTotalInfo.bucketId,
                                title = writeTotalInfo.title,
                                options = writeTotalInfo.options,
                                writeOpenType = selectedOpenType.value,
                                imageFiles = imagesInfo.map { it.file },
                                keyWord = selectedKeywordList.map { it.id },
                                tagFriends = selectedFriends.map { it.id },
                                isScrapAvailable = isScrapUsed
                            ),
                            isForUpdate = writeTotalInfo.isForUpdate
                        )
                    }
                }
            })

        val state = rememberLazyGridState()

        Column(
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
        ) {
            WriteTitleView(
                modifier = Modifier.padding(
                    bottom = if (writeInfo1.getImagesPaths().isEmpty()) 150.dp else 32.dp
                ),
                title = writeTotalInfo.title
            )

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

            KeywordsOptionView(
                modifier = Modifier.fillMaxWidth(),
                keywordList = selectedKeywordList
            ) {
                keywordSelected()
            }

            FriendsOptionView(
                modifier = Modifier.fillMaxWidth(),
                friendOption = friendsOption,
                friendsList = selectedFriends
            ) {
                friendSelected(it)
            }

            OpenTypeOptionView(
                modifier = Modifier.fillMaxWidth(),
                openType = openType,
                optionClicked = openTypeSelected
            )

            WriteScrapOptionView(
                modifier = Modifier.fillMaxWidth(),
                isScrapAvailable = selectedOpenType.value == WriteOpenType.PUBLIC,
                isScrapUsed = isScrapUsed,
                scrapChanged = scrapChanged
            )
        }
    }
}