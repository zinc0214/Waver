package com.zinc.berrybucket.ui.presentation.write

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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.zinc.berrybucket.R
import com.zinc.berrybucket.model.WriteAddOption
import com.zinc.berrybucket.model.WriteFriend
import com.zinc.berrybucket.model.WriteImageInfo
import com.zinc.berrybucket.model.WriteInfo1
import com.zinc.berrybucket.model.WriteKeyWord
import com.zinc.berrybucket.model.WriteOption
import com.zinc.berrybucket.model.WriteOption1Info
import com.zinc.berrybucket.model.WriteOptionsType1
import com.zinc.berrybucket.model.WriteOptionsType2
import com.zinc.berrybucket.ui.model.WriteOpenType
import com.zinc.berrybucket.ui.presentation.common.gridItems
import com.zinc.berrybucket.ui.presentation.write.options.ImageItem
import com.zinc.berrybucket.ui.presentation.write.options.WriteSelectFriendsScreen
import com.zinc.berrybucket.ui.presentation.write.options.WriteSelectKeyWordScreen


// TODO : 백키를 통해 이동시킬 때 데이터를 가져가지 않고 그냥 돌아가는 문제 해결 필요
@Composable
fun WriteScreen2(
    modifier: Modifier = Modifier,
    writeInfo1: WriteInfo1,
    goToBack: (WriteInfo1) -> Unit,
    goToAddBucket: () -> Unit
) {

    var optionScreenShow: WriteOptionsType2? by remember { mutableStateOf(null) }

    BackHandler(enabled = true) { // <-----
        if (optionScreenShow != null) {
            optionScreenShow = null
        } else {
            goToBack(writeInfo1)
        }
    }

    // TODO : 데이터 받아오도록 수정 필요
    val originKeyWord = buildList {
        repeat(50) {
            add(WriteKeyWord(id = it.toString(), text = "여행 + $it"))
        }
    }

    val selectedKeyWords = remember { mutableStateListOf<WriteKeyWord>() }
    val selectedFriends = remember { mutableStateListOf<WriteFriend>() }
    val selectedOpenType = remember { mutableStateOf(WriteOpenType.PUBLIC) }

    val optionsList = mutableListOf(
        WriteAddOption(
            type = WriteOptionsType2.TAG,
            title = "키워드 추가",
            tagList = selectedKeyWords.map { "#${it.text}" },
            clicked = {
                optionScreenShow = it
            }),
        WriteAddOption(
            type = WriteOptionsType2.FRIENDS,
            title = "함께할 친구 추가하기",
            tagList = selectedFriends.map { "@${it.nickname}" },
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
                isScrapUsed = false
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
                    selectedKeyWords = selectedKeyWords,
                    addKeyWordClicked = {
                        selectedKeyWords.clear()
                        selectedKeyWords.addAll(it)
                        optionScreenShow = null
                    }
                )
            }

            WriteOptionsType2.FRIENDS -> {
                WriteSelectFriendsScreen(
                    closeClicked = { optionScreenShow = null },
                    selectedFriends = selectedFriends,
                    addFriendsClicked = {
                        selectedFriends.clear()
                        selectedFriends.addAll(it)
                        optionScreenShow = null
                    }
                )
            }
        }
    } else {
        ConstraintLayout(modifier = modifier.fillMaxSize()) {

            WriteScreen2ContentView(
                writeInfo1 = writeInfo1,
                optionsList = optionsList,
                goToBack = { goToBack(writeInfo1) },
                selectedOpenType = selectedOpenType,
                optionValueChanged = { changedOption ->
                    val changedIndex = optionsList.indexOfFirst { it.type == changedOption.type }
                    optionsList[changedIndex] = changedOption
                }
            ) { }

            if (optionScreenShow == WriteOptionsType2.OPEN) {
                SelectOpenTypePopup(
                    isPrivateAvailable = selectedFriends.isEmpty(),
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
    writeInfo1: WriteInfo1,
    optionsList: List<WriteAddOption>,
    goToBack: (WriteInfo1) -> Unit,
    selectedOpenType: MutableState<WriteOpenType>,
    optionValueChanged: (WriteAddOption) -> Unit,
    goToAddBucket: () -> Unit,
) {

    val isScrapAvailable by remember { mutableStateOf(selectedOpenType.value != WriteOpenType.PRIVATE) }

    ConstraintLayout(modifier = modifier.fillMaxSize()) {

        val (appBar, contents) = createRefs()
        val viewModel: WriteViewModel = hiltViewModel()
        val scrapUsed = remember { mutableStateOf(false) }

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
                        val newImage = mutableListOf<WriteImageInfo>()
                        writeInfo1.getImages()?.let { infos ->
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
                        goToBack(writeInfo1.copy(options = optionUpdate))
                        // TODO : 이미지
                    }

                    WriteAppBarClickEvent.NextClicked -> {
                        viewModel.addNewBucketList()
                        // goToAddBucket(WriteResultInfo())
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
                    title = writeInfo1.title
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