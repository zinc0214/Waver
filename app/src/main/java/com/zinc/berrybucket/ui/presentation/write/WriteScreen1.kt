package com.zinc.berrybucket.ui.presentation.write

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
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
import com.zinc.berrybucket.model.WriteOptionsType1
import com.zinc.berrybucket.model.WriteOptionsType1.*
import com.zinc.berrybucket.ui.presentation.ActionWithActivity
import com.zinc.berrybucket.ui.presentation.CameraPermission
import com.zinc.berrybucket.ui.presentation.common.gridItems
import com.zinc.berrybucket.ui.presentation.write.bottomScreens.CalendarSelectBottomSheet
import com.zinc.berrybucket.ui.presentation.write.bottomScreens.CategorySelectBottomScreen
import com.zinc.berrybucket.ui.presentation.write.bottomScreens.GoalCountBottomScreen
import com.zinc.berrybucket.ui.presentation.write.bottomScreens.ImageSelectBottomScreen
import com.zinc.berrybucket.ui.presentation.write.options.ImageItem
import com.zinc.berrybucket.ui.presentation.write.options.MemoScreen
import com.zinc.berrybucket.ui.presentation.write.options.OptionScreen
import com.zinc.berrybucket.util.parseWithDday
import com.zinc.berrybucket.util.textParseToLocalDate
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterialApi::class)
@Composable
fun WriteScreen1(
    action: (ActionWithActivity) -> Unit,
    goToBack: () -> Unit,
    goToNext: (WriteInfo1) -> Unit,
    writeInfo1: WriteInfo1
) {
    val context = LocalContext.current

    var selectedOptionType: WriteOptionsType1? by remember { mutableStateOf(null) }
    val currentClickedOptions = remember { mutableSetOf<WriteOptionsType1>() }
    val nextButtonClickable = remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val bottomSheetScaffoldState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden, skipHalfExpanded = true
    )
    val isNeedToBottomSheetOpen: (Boolean) -> Unit = {
        coroutineScope.launch {
            if (it) {
                bottomSheetScaffoldState.show()
            } else {
                bottomSheetScaffoldState.hide()
            }
        }
    }

    val title = remember { mutableStateOf(writeInfo1.title) }
    val originMemo = remember { mutableStateOf(writeInfo1.memo) }
    val originCount = remember {
        mutableStateOf(writeInfo1.options.find { option -> option.type == GOAL }?.content ?: "0")
    }
    val originDdayDate = remember {
        mutableStateOf(textParseToLocalDate(writeInfo1.options.find { option ->
            option.type == D_DAY
        }?.content))
    }

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
            CATEGORY -> currentClickedOptions.add(CATEGORY)
            D_DAY -> currentClickedOptions.add(D_DAY)
            GOAL -> currentClickedOptions.add(GOAL)
            else -> {
                // do nothing
            }
        }
    }

// 백키 이벤트
    BackHandler() {
        if (selectedOptionType == null) {
            goToBack()
        } else {
            selectedOptionType = null
        }
    }


// 각 option 값이 들어왔을 때 해야하는 것들 정리
    if (selectedOptionType == MEMO) {
        MemoScreen(originMemo = originMemo.value, memoChanged = {
            originMemo.value = it
            currentClickedOptions.add(MEMO)
            selectedOptionType = null
        }, closeClicked = {
            selectedOptionType = null
        })
    }

    if (selectedOptionType == IMAGE) {
        CameraPermission(modifier = Modifier, isAvailable = {
            selectedOptionType = if (it) {
                IMAGE
            } else {
                null
            }
        })
    }

    // 기존 option 값 제거?
    fun deleteOption(type: WriteOptionsType1) {
        optionList.value.find { option -> option.type == type }
            ?.let { existOption ->
                optionList.value -= existOption
            }
    }

// 바텀시트
    ModalBottomSheetLayout(
        sheetState = bottomSheetScaffoldState,
        sheetContent = {
            Box(Modifier.defaultMinSize(minHeight = 1.dp)) {
                when (selectedOptionType) {
                    IMAGE -> {
                        ImageSelectBottomScreen(selectedType = {
                            action(
                                ActionWithActivity.AddImage(type = it,
                                    failed = {
                                        Toast.makeText(
                                            context,
                                            "이미지 로드에 실패했습니다.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        selectedOptionType = null
                                        isNeedToBottomSheetOpen.invoke(false)
                                    },
                                    succeed = { uri ->
                                        Log.e("ayhan", "uri : $uri")
                                        imageList.value += uri
                                        selectedOptionType = null
                                        isNeedToBottomSheetOpen.invoke(false)
                                    })
                            )
                        })
                        isNeedToBottomSheetOpen.invoke(true)
                    }
                    CATEGORY -> {
                        CategorySelectBottomScreen(confirmed = {
                            currentClickedOptions.add(CATEGORY)
                            deleteOption(CATEGORY)
                            optionList.value += WriteOption(
                                type = CATEGORY,
                                title = "카테고리",
                                content = it.name
                            )
                            selectedOptionType = null
                            isNeedToBottomSheetOpen.invoke(false)
                        })
                        isNeedToBottomSheetOpen.invoke(true)
                    }
                    D_DAY -> {
                        CalendarSelectBottomSheet(confirmed = {
                            currentClickedOptions.add(D_DAY)
                            deleteOption(D_DAY)
                            optionList.value += WriteOption(
                                type = D_DAY,
                                title = "디데이",
                                content = it.parseWithDday()
                            )
                            originDdayDate.value = it
                            selectedOptionType = null
                            isNeedToBottomSheetOpen.invoke(false)
                        }, canceled = {
                            selectedOptionType = null
                            isNeedToBottomSheetOpen.invoke(false)
                        }, selectedDate = originDdayDate.value)
                        isNeedToBottomSheetOpen.invoke(true)
                    }
                    GOAL -> {
                        GoalCountBottomScreen(
                            originCount = originCount.value,
                            canceled = {
                                selectedOptionType = null
                                isNeedToBottomSheetOpen.invoke(false)
                            }, confirmed = {
                                currentClickedOptions.add(GOAL)
                                deleteOption(GOAL)
                                optionList.value += WriteOption(
                                    type = GOAL,
                                    title = "목표 달성 횟수",
                                    content = it
                                )
                                selectedOptionType = null
                                isNeedToBottomSheetOpen.invoke(false)
                                originCount.value = it
                            })
                        isNeedToBottomSheetOpen.invoke(true)
                    }
                    else -> {
                        // Do Nothing
                    }
                }
            }
        },
        sheetShape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp)
    ) {
        if (selectedOptionType != MEMO) {
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
                            selectedOptionType = it
                        }
                    }
                })
            }
        }
    }
}


