package com.zinc.berrybucket.ui_write.presentation

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue.Hidden
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.hana.berrybucket.ui_write.R
import com.zinc.berrybucket.model.WriteCategoryInfo
import com.zinc.berrybucket.model.WriteOption
import com.zinc.berrybucket.model.WriteOption1Info
import com.zinc.berrybucket.model.WriteOptionsType1
import com.zinc.berrybucket.model.WriteOptionsType1.CATEGORY
import com.zinc.berrybucket.model.WriteOptionsType1.D_DAY
import com.zinc.berrybucket.model.WriteOptionsType1.GOAL
import com.zinc.berrybucket.model.WriteOptionsType1.IMAGE
import com.zinc.berrybucket.model.WriteOptionsType1.MEMO
import com.zinc.berrybucket.model.WriteTotalInfo
import com.zinc.berrybucket.model.parseWrite1Info
import com.zinc.berrybucket.ui.presentation.component.ImageSelectBottomScreen
import com.zinc.berrybucket.ui.presentation.component.gridItems
import com.zinc.berrybucket.ui.presentation.model.ActionWithActivity
import com.zinc.berrybucket.ui.util.CameraPermission
import com.zinc.berrybucket.ui.util.parseWithDday
import com.zinc.berrybucket.ui_write.presentation.bottomScreens.CalendarSelectBottomSheet
import com.zinc.berrybucket.ui_write.presentation.bottomScreens.CategorySelectBottomScreen
import com.zinc.berrybucket.ui_write.presentation.bottomScreens.GoalCountBottomScreen
import com.zinc.berrybucket.ui_write.presentation.options.AddImageItem
import com.zinc.berrybucket.ui_write.presentation.options.ImageItem
import com.zinc.berrybucket.ui_write.presentation.options.MemoScreen
import com.zinc.berrybucket.ui_write.presentation.options.OptionScreen
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterialApi::class)
@Composable
fun WriteScreen1(
    action: (ActionWithActivity) -> Unit,
    goToBack: () -> Unit,
    goToNext: (WriteTotalInfo) -> Unit,
    originWriteTotalInfo: WriteTotalInfo
) {
    val context = LocalContext.current
    val originWriteInfo1 = originWriteTotalInfo.parseWrite1Info()
    val initialized = remember { mutableStateOf(false) }

    // 지금 선택된 option
    var selectedOption: WriteOptionsType1? by remember { mutableStateOf(null) }

    // 저장된 option 값들
    val updatedWriteOptions = remember { mutableStateListOf<WriteOption>() }

    if (!initialized.value) {
        Log.e("ayhan", "WriteScreen1: {${initialized.value}, ${originWriteInfo1.options}")
        updatedWriteOptions.addAll(originWriteInfo1.options)
        initialized.value = true
    }


    val nextButtonClickable = remember { mutableStateOf(false) }

    // BottomSheet 관련
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetScaffoldState = rememberModalBottomSheetState(
        initialValue = Hidden, skipHalfExpanded = true
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

    LaunchedEffect(bottomSheetScaffoldState.currentValue) {
        when (bottomSheetScaffoldState.currentValue) {
            Hidden -> {
                selectedOption = null
                isNeedToBottomSheetOpen(false)
            }

            else -> {
                // Do Nothing
            }
        }
    }


    val title = remember { mutableStateOf(originWriteInfo1.title) }
    val originMemo = remember { mutableStateOf(originWriteInfo1.getMemo()) }
    val originCount = remember { mutableStateOf(originWriteInfo1.getGoalCount()) }
    val originDdayDate = remember { mutableStateOf(originWriteInfo1.getDday()) }
    val imageList = remember { mutableStateOf(originWriteInfo1.getImages()) }

    nextButtonClickable.value = title.value.isNotEmpty()

// 백키 이벤트
    BackHandler {
        if (selectedOption == null) {
            goToBack()
        } else {
            coroutineScope.launch {
                isNeedToBottomSheetOpen.invoke(false)
            }
            selectedOption = null
        }
    }


// 각 option 값이 들어왔을 때 해야하는 것들 정리
    if (selectedOption == MEMO) {
        MemoScreen(
            originMemo = originMemo.value,
            memoChanged = {
                originMemo.value = it
                updatedWriteOptions.add(
                    WriteOption(
                        type = MEMO,
                        title = "메모",
                        info = WriteOption1Info.Memo(it)
                    )
                )
                selectedOption = null
            }, closeClicked = {
                selectedOption = null
            })
    }

    if (selectedOption == IMAGE) {
        CameraPermission(isAvailable = {
            selectedOption = if (it) {
                IMAGE
            } else {
                null
            }
        })
    }

    // 기존 option 값 제거?
    fun deleteOption(type: WriteOptionsType1) {
        updatedWriteOptions.find { it.type == type }?.let {
            updatedWriteOptions.remove(it)
        }
    }

// 바텀시트
    ModalBottomSheetLayout(
        sheetState = bottomSheetScaffoldState,
        sheetContent = {
            Box(Modifier.defaultMinSize(minHeight = 1.dp)) {
                when (selectedOption) {
                    IMAGE -> {
                        ImageSelectBottomScreen(selectedType = { it ->
                            action(
                                ActionWithActivity.AddImage(
                                    type = it,
                                    failed = {
                                        Toast.makeText(
                                            context,
                                            "이미지 로드에 실패했습니다.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        selectedOption = null
                                        isNeedToBottomSheetOpen.invoke(false)
                                    },
                                    succeed = { uri ->
                                        Log.e("ayhan", "uri : $uri")
                                        imageList.value += uri
                                        selectedOption = null
                                        isNeedToBottomSheetOpen.invoke(false)

                                        val prevImageOption =
                                            updatedWriteOptions.find { options -> options.type == IMAGE }
                                        if (prevImageOption != null) {
                                            updatedWriteOptions.remove(prevImageOption)
                                        }
                                        updatedWriteOptions.add(
                                            WriteOption(
                                                type = IMAGE,
                                                title = "이미지",
                                                info = WriteOption1Info.Images(imageList.value)
                                            )
                                        )
                                    })
                            )
                        })
                        isNeedToBottomSheetOpen.invoke(true)
                    }

                    CATEGORY -> {
                        CategorySelectBottomScreen(confirmed = { category ->
                            deleteOption(CATEGORY)
                            updatedWriteOptions.add(
                                WriteOption(
                                    type = CATEGORY,
                                    title = "카테고리",
                                    info = WriteOption1Info.Category(
                                        WriteCategoryInfo(
                                            category.id,
                                            category.name,
                                            category.defaultYn,
                                            category.bucketlistCount
                                        )
                                    )
                                )
                            )
                            selectedOption = null
                            isNeedToBottomSheetOpen.invoke(false)
                        })
                        isNeedToBottomSheetOpen.invoke(true)
                    }

                    D_DAY -> {
                        CalendarSelectBottomSheet(confirmed = {
                            deleteOption(D_DAY)
                            updatedWriteOptions.add(
                                WriteOption(
                                    type = D_DAY,
                                    title = "디데이",
                                    info = WriteOption1Info.Dday(it, it.parseWithDday())
                                )
                            )
                            originDdayDate.value = WriteOption1Info.Dday(it, it.parseWithDday())
                            selectedOption = null
                            isNeedToBottomSheetOpen.invoke(false)
                        }, canceled = {
                            selectedOption = null
                            isNeedToBottomSheetOpen.invoke(false)
                        }, selectedDate = originDdayDate.value?.localDate ?: LocalDate.now())
                        isNeedToBottomSheetOpen.invoke(true)
                    }

                    GOAL -> {
                        GoalCountBottomScreen(
                            originCount = originCount.value.toString(),
                            canceled = {
                                selectedOption = null
                                isNeedToBottomSheetOpen.invoke(false)
                            }, confirmed = {
                                deleteOption(GOAL)
                                updatedWriteOptions.add(
                                    WriteOption(
                                        type = GOAL,
                                        title = "목표 달성 횟수",
                                        info = WriteOption1Info.GoalCount(it.toInt())
                                    )
                                )
                                selectedOption = null
                                isNeedToBottomSheetOpen.invoke(false)
                                originCount.value = it.toInt()
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
        if (selectedOption != MEMO) {
            ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                val (appBar, contents, option) = createRefs()

                WriteAppBar(
                    modifier = Modifier
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
                                Log.e("ayhan", "WriteScreen1: ${updatedWriteOptions.toList()}")
                                goToNext(
                                    originWriteTotalInfo.copy(
                                        title = title.value,
                                        options = updatedWriteOptions.toList()
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
                        if (updatedWriteOptions.find { it.type == MEMO } != null) {
                            MemoOptionView(
                                modifier = Modifier.padding(top = 28.dp),
                                memoText = originMemo.value
                            )
                        }
                    }

                    item {
                        if (updatedWriteOptions.find { it.type == IMAGE } != null) {
                            gridItems(data = imageList.value,
                                maxRow = 3,
                                modifier = Modifier
                                    .padding(horizontal = 28.dp)
                                    .padding(top = 28.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(
                                    32.dp,
                                    Alignment.CenterHorizontally
                                ),
                                verticalSpace = 28.dp,
                                itemContent = {
                                    ImageItem(
                                        imageInfo = it,
                                        deleteImage = { removeImage ->
                                            imageList.value -= removeImage
                                            if (imageList.value.isEmpty()) {
                                                updatedWriteOptions.removeIf { it.type == IMAGE }
                                            }
                                        })

                                    if (imageList.value.isNotEmpty() && imageList.value.size < 3 && it == imageList.value.last()) {
                                        AddImageItem {
                                            selectedOption = IMAGE
                                        }
                                    }
                                },
                                emptyContent = {
                                    Spacer(modifier = Modifier.size(80.dp))
                                })
                        }
                    }

                    item {
                        OptionScreen(options = updatedWriteOptions.toList())
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
                    },
                    currentClickedOptions = updatedWriteOptions.map { it.type }.toSet(),
                    optionClicked = { option ->
                        val isAlreadySelected = updatedWriteOptions.find { it.type == option }
                        if (isAlreadySelected != null) {
                            when (option) {
                                IMAGE,
                                MEMO,
                                CATEGORY,
                                D_DAY,
                                GOAL -> {
                                    updatedWriteOptions.remove(isAlreadySelected)
                                }
                            }
                        } else {
                            when (option) {
                                IMAGE -> {
                                    if (imageList.value.isEmpty()) {
                                        selectedOption = option
                                    } else {
                                        updatedWriteOptions.add(
                                            WriteOption(
                                                type = IMAGE,
                                                title = "이미지",
                                                info = WriteOption1Info.Images(imageList.value)
                                            )
                                        )
                                    }
                                }

                                MEMO, CATEGORY, D_DAY, GOAL -> {
                                    selectedOption = option
                                }
                            }
                        }
                    })
            }
        }
    }
}

