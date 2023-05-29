package com.zinc.berrybucket.ui.presentation.write

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.zinc.berrybucket.R
import com.zinc.berrybucket.model.UserSelectedImageInfo
import com.zinc.berrybucket.model.WriteAddOption
import com.zinc.berrybucket.model.WriteKeyWord
import com.zinc.berrybucket.model.WriteOpenType
import com.zinc.berrybucket.model.WriteOption
import com.zinc.berrybucket.model.WriteOption1Info
import com.zinc.berrybucket.model.WriteOptionsType1
import com.zinc.berrybucket.model.WriteOptionsType2
import com.zinc.berrybucket.model.WriteTotalInfo
import com.zinc.berrybucket.model.parseUIBucketListInfo
import com.zinc.berrybucket.model.parseWrite1Info
import com.zinc.berrybucket.ui.presentation.component.gridItems
import com.zinc.berrybucket.ui.presentation.write.options.ImageItem
import com.zinc.berrybucket.ui.presentation.write.options.WriteSelectFriendsScreen
import com.zinc.berrybucket.ui.presentation.write.options.WriteSelectKeyWordScreen

@Composable
fun WriteScreen2(
    modifier: Modifier = Modifier,
    writeTotalInfo: WriteTotalInfo,
    goToBack: (WriteTotalInfo) -> Unit,
    addBucketSucceed: () -> Unit
) {

    val context = LocalContext.current

    var optionScreenShow: WriteOptionsType2? by remember { mutableStateOf(null) }

    val selectedKeyWords = remember { mutableStateOf(writeTotalInfo.keyWord) }
    val selectedFriends = remember { mutableStateOf(writeTotalInfo.tagFriends) }
    val selectedOpenType = remember { mutableStateOf(writeTotalInfo.writeOpenType) }
    val isScrapUsed = remember { mutableStateOf(writeTotalInfo.isScrapUsed) }

    BackHandler(enabled = true) { // <-----
        if (optionScreenShow != null) {
            optionScreenShow = null
        } else {
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

    // TODO : 데이터 받아오도록 수정 필요
    val originKeyWord = buildList {
        repeat(50) {
            add(WriteKeyWord(id = it.toString(), text = "여행 + $it"))
        }
    }


    val optionsList = mutableListOf(
        WriteAddOption(
            type = WriteOptionsType2.TAG,
            title = "키워드 추가",
            tagList = selectedKeyWords.value.map { "#${it.text}" },
            clicked = {
                optionScreenShow = it
            }),
        WriteAddOption(
            type = WriteOptionsType2.FRIENDS,
            title = "함께할 친구 추가하기",
            tagList = selectedFriends.value.map { "@${it.nickname}" },
            clicked = {
                optionScreenShow = it
            }),
        WriteAddOption(
            type = WriteOptionsType2.OPEN,
            title = "공개설정",
            tagList = listOf(selectedOpenType.value.text),
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
            tagList = emptyList(),
            clicked = {
                optionScreenShow = null
            }),
    )

    if (optionScreenShow != null && optionScreenShow != WriteOptionsType2.OPEN) {
        when (optionScreenShow) {
            WriteOptionsType2.TAG -> {
                WriteSelectKeyWordScreen(
                    closeClicked = { optionScreenShow = null },
                    originKeyWord = originKeyWord,
                    selectedKeyWords = selectedKeyWords.value,
                    addKeyWordClicked = {
                        selectedKeyWords.value = it
                        optionScreenShow = null
                    }
                )
            }

            WriteOptionsType2.FRIENDS -> {
                WriteSelectFriendsScreen(
                    closeClicked = { optionScreenShow = null },
                    selectedFriends = selectedFriends.value,
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
        ConstraintLayout(modifier = modifier.fillMaxSize()) {

            WriteScreen2ContentView(
                writeTotalInfo = writeTotalInfo,
                optionsList = optionsList,
                goToBack = {
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
            ) {
                addBucketSucceed()
            }

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
    }
}


@Composable
private fun WriteScreen2ContentView(
    modifier: Modifier = Modifier,
    writeTotalInfo: WriteTotalInfo,
    optionsList: List<WriteAddOption>,
    goToBack: (WriteTotalInfo) -> Unit,
    selectedOpenType: MutableState<WriteOpenType>,
    optionValueChanged: (WriteAddOption) -> Unit,
    addBucketSucceed: () -> Unit,
) {

    val writeInfo1 = writeTotalInfo.parseWrite1Info()
    ConstraintLayout(modifier = modifier.fillMaxSize()) {

        val (appBar, contents) = createRefs()
        val viewModel: WriteViewModel = hiltViewModel()
        WriteAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(appBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            nextButtonClickable = true,
            rightText = R.string.writeGoToFinish,
            clickEvent = { it ->
                when (it) {
                    WriteAppBarClickEvent.CloseClicked -> {
                        val newImage = mutableListOf<UserSelectedImageInfo>()
                        writeInfo1.getImages().let { infos ->
                            infos.forEach { info ->
                                val image = info.copy(key = info.intKey() + infos.size)
                                newImage.add(image)
                            }
                        }
                        val option =
                            writeInfo1.options.find { option -> option.type == WriteOptionsType1.IMAGE }
                        val optionUpdate = writeInfo1.options.toMutableList()
                        optionUpdate.remove(option)
                        optionUpdate.add(
                            WriteOption(
                                type = WriteOptionsType1.IMAGE,
                                title = "이미지",
                                info = WriteOption1Info.Images(newImage)
                            )
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
                                title = writeTotalInfo.title,
                                options = writeTotalInfo.options,
                                writeOpenType = selectedOpenType.value,
                                keyWord = optionsList.find { it.type == WriteOptionsType2.TAG }?.tagList.orEmpty(),
                                tagFriends = optionsList.find { it.type == WriteOptionsType2.FRIENDS }?.tagList.orEmpty(),
                                isScrapAvailable = scrapOption.isScrapUsed
                            )
                        ) {
                            if (it) addBucketSucceed() else addBucketSucceed()
                        }
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
                        bottom = if (writeInfo1.getImages().isEmpty()) 150.dp else 32.dp
                    ),
                    title = writeTotalInfo.title
                )
            }

            item {
                gridItems(
                    data = writeInfo1.getImages(),
                    maxRow = 3,
                    modifier = Modifier
                        .padding(horizontal = 28.dp)
                        .padding(top = 28.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalSpace = 28.dp,
                    itemContent = {
                        ImageItem(imageInfo = it)
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