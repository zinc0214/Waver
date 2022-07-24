package com.zinc.berrybucket.ui.presentation.write

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.zinc.berrybucket.R
import com.zinc.berrybucket.model.WriteInfo1
import com.zinc.berrybucket.model.WriteOption
import com.zinc.berrybucket.ui.presentation.ActionWithActivity
import com.zinc.berrybucket.ui.presentation.CameraPermission
import com.zinc.berrybucket.ui.presentation.common.gridItems
import com.zinc.berrybucket.ui.presentation.write.BottomOptionType.*
import com.zinc.berrybucket.ui.presentation.write.options.ImageItem
import com.zinc.berrybucket.ui.presentation.write.options.MemoScreen
import com.zinc.berrybucket.ui.presentation.write.options.OptionScreen

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WriteScreen1(
    action: (ActionWithActivity) -> Unit,
    goToBack: () -> Unit,
    goToNext: (WriteInfo1) -> Unit,
    writeInfo1: WriteInfo1
) {
    val context = LocalContext.current

    var showOptionView: BottomOptionType? by remember { mutableStateOf(null) }
    val currentClickedOptions = remember { mutableStateListOf<BottomOptionType>() }
    val nextButtonClickable = remember { mutableStateOf(false) }

    val title = remember { mutableStateOf(writeInfo1.title) }
    val originMemo = remember { mutableStateOf(writeInfo1.memo) }
    val imageList = remember { mutableStateOf(writeInfo1.images) }
    val optionList = remember { mutableStateOf(writeInfo1.options) }

    // init
    nextButtonClickable.value = title.value.isNotEmpty()
    if (writeInfo1.memo.isNotEmpty()) {
        currentClickedOptions.add(MEMO)
    }
    if (imageList.value.isNotEmpty()) {
        currentClickedOptions.add(IMAGE)
    }
    optionList.value.forEach {
        when (it.type) {
            OptionsType.CATEGORY -> currentClickedOptions.add(CATEGORY)
            OptionsType.D_DAY -> currentClickedOptions.add(D_DAY)
            OptionsType.GOAL -> currentClickedOptions.add(GOAL)
        }
    }

    BackHandler() {
        if (showOptionView == null) {
            goToBack()
        } else {
            showOptionView = null
        }
    }

    if (showOptionView == MEMO) {
        MemoScreen(originMemo = originMemo.value, memoChanged = {
            originMemo.value = it
            currentClickedOptions.add(MEMO)
            showOptionView = null
        }, closeClicked = {
            showOptionView = null
        })
    }

    if (showOptionView == IMAGE) {
        // TODO : 카메라인지 갤러리인지 확인필요
        CameraPermission(modifier = Modifier, isAvailable = {
            if (it) {
                action(
                    ActionWithActivity.AddImage(type = ActionWithActivity.AddImageType.GALLERY,
                        failed = {
                            Toast.makeText(context, "카메라 실패", Toast.LENGTH_SHORT).show()
                            showOptionView = null
                        },
                        succeed = { uri ->
                            Log.e("ayhan", "uri : $uri")
                            imageList.value += uri
                            showOptionView = null
                        })
                )
            }
            showOptionView = null
        })
    }

    if (showOptionView == CATEGORY) {
        currentClickedOptions.add(CATEGORY)
        optionList.value += WriteOption(
            type = OptionsType.CATEGORY,
            title = "카테고리",
            content = "요가를해보자요가는재미가없지만"
        )
        showOptionView = null
    }

    if (showOptionView == D_DAY) {
        currentClickedOptions.add(D_DAY)
        optionList.value += WriteOption(
            type = OptionsType.D_DAY,
            title = "디데이",
            content = "2022.10.08(D-102)"
        )
        showOptionView = null
    }

    if (showOptionView == GOAL) {
        currentClickedOptions.add(GOAL)
        optionList.value += WriteOption(type = OptionsType.GOAL, title = "목표 달성 횟수", content = "10")
        showOptionView = null
    }

    if (showOptionView == null) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (appBar, contents, option) = createRefs()

            WriteAppBar(modifier = Modifier
                .fillMaxWidth()
                .constrainAs(appBar) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                },
                nextButtonClickable = nextButtonClickable.value,
                rightText = R.string.writeGoToNext,
                clickEvent = {
                    when (it) {
                        WriteAppBarClickEvent.CloseClicked -> {
                            goToBack()
                        }
                        WriteAppBarClickEvent.NextClicked -> {
                            goToNext(
                                WriteInfo1(
                                    title = title.value,
                                    memo = originMemo.value,
                                    images = imageList.value,
                                    options = optionList.value
                                )
                            )
                        }
                    }
                })

            LazyColumn(modifier = Modifier
                .constrainAs(contents) {
                    top.linkTo(appBar.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(option.top)
                    height = Dimension.fillToConstraints
                }
                .padding(bottom = 20.dp)) {

                item {
                    WriteTitleFieldView(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 28.dp)
                        .padding(horizontal = 28.dp),
                        title = title.value,
                        textChanged = {
                            title.value = it
                            nextButtonClickable.value = it.isNotEmpty()
                        })
                }

                item {
                    if (originMemo.value.isNotEmpty()) {
                        MemoOptionView(modifier = Modifier.padding(top = 28.dp),
                            memoText = originMemo.value,
                            memoDelete = {
                                originMemo.value = ""
                                currentClickedOptions.remove(MEMO)
                            })
                    }
                }

                item {
                    gridItems(data = imageList.value,
                        maxRow = 3,
                        modifier = Modifier
                            .padding(horizontal = 28.dp)
                            .padding(top = 28.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalSpace = 28.dp,
                        itemContent = {
                            ImageItem(imageInfo = it, deleteImage = { removeImage ->
                                imageList.value -= removeImage
                            })
                        },
                        emptyContent = {
                            Spacer(modifier = Modifier.size(80.dp))
                        })
                }

                item {
                    OptionScreen(options = optionList.value)
                }
            }

            BottomOptionView(modifier = Modifier
                .fillMaxWidth()
                .constrainAs(
                    option
                ) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }, currentClickedOptions = currentClickedOptions, optionUsed = {
                when (it) {
                    MEMO, IMAGE, CATEGORY, D_DAY, GOAL -> {
                        showOptionView = it
                    }
                }
            })
        }
    }
}

