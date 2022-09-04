package com.zinc.berrybucket.ui.presentation.write

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.zinc.berrybucket.R
import com.zinc.berrybucket.model.*
import com.zinc.berrybucket.ui.design.theme.Gray2
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
    goToAddBucket: (WriteResultInfo) -> Unit
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
            })
    )

    if (optionScreenShow != null) {
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
        WriteScreen2ContentView(
            modifier = modifier,
            writeInfo1 = writeInfo1,
            optionsList = optionsList,
            goToBack = { goToBack(writeInfo1) },
            write2OptionClicked = { optionScreenShow = it },
            goToAddBucket = { })
    }
}


@Composable
private fun WriteScreen2ContentView(
    modifier: Modifier = Modifier,
    writeInfo1: WriteInfo1,
    optionsList: List<WriteAddOption>,
    goToBack: (WriteInfo1) -> Unit,
    write2OptionClicked: (WriteOptionsType2) -> Unit,
    goToAddBucket: () -> Unit
) {
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
            nextButtonClickable = true,
            rightText = R.string.writeGoToFinish,
            clickEvent = {
                when (it) {
                    WriteAppBarClickEvent.CloseClicked -> {
                        val newImage = mutableListOf<WriteImageInfo>()
                        writeInfo1.images.forEach { info ->
                            val image = info.copy(key = info.intKey() + writeInfo1.images.size)
                            newImage.add(image)
                        }
                        val newInfo = writeInfo1.copy(images = newImage)
                        goToBack(newInfo)
                        // TODO : 이미지
                    }
                    WriteAppBarClickEvent.NextClicked -> {
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
                    modifier = Modifier.padding(bottom = if (writeInfo1.images.isEmpty()) 150.dp else 32.dp),
                    title = writeInfo1.title
                )
            }

            item {
                gridItems(data = writeInfo1.images,
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
                        optionClicked = { write2OptionClicked(it.type) }
                    )
                }

                Divider(color = Gray2, thickness = 8.dp)
            }
        }
    }
}
