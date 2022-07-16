package com.zinc.berrybucket.ui.presentation.write

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.zinc.berrybucket.R
import com.zinc.berrybucket.model.WriteOption
import com.zinc.berrybucket.ui.presentation.ActionWithActivity
import com.zinc.berrybucket.ui.presentation.CameraPermission
import com.zinc.berrybucket.ui.presentation.write.BottomOptionType.*
import com.zinc.berrybucket.ui.presentation.write.options.ImageScreen
import com.zinc.berrybucket.ui.presentation.write.options.MemoScreen
import com.zinc.berrybucket.ui.presentation.write.options.OptionScreen

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WriteScreen(
    action: (ActionWithActivity) -> Unit, goToBack: () -> Unit
) {
    val context = LocalContext.current

    var showOptionView: BottomOptionType? by remember { mutableStateOf(null) }
    val currentClickedOptions = remember { mutableStateListOf<BottomOptionType>() }

    val title = remember { mutableStateOf("") }
    val originMemo = remember { mutableStateOf("") }
    val imageList = remember { mutableStateListOf<Uri>() }
    val optionList = remember { mutableStateListOf<WriteOption>() }

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
        // 카메라인지 갤러리인지 확인필요
        CameraPermission(modifier = Modifier, isAvailable = {
            if (it) {
                action(
                    ActionWithActivity.AddImage(type = ActionWithActivity.AddImageType.GALLERY,
                        failed = {
                            Toast.makeText(context, "카메라 실패", Toast.LENGTH_SHORT).show()
                            showOptionView = null
                        },
                        succeed = { uri ->
                            imageList.add(uri)
                            showOptionView = null
                        })
                )
            }
        })
    }

    if (showOptionView == CATEGORY) {
        currentClickedOptions.add(CATEGORY)
        optionList.add(WriteOption(title = "카테고리", content = "요가를해보자요가는재미가없지만"))
        showOptionView = null
    }

    if (showOptionView == D_DAY) {
        currentClickedOptions.add(D_DAY)
        optionList.add(WriteOption(title = "디데이", content = "2022.10.08(D-102)"))
        showOptionView = null
    }

    if (showOptionView == GOAL) {
        currentClickedOptions.add(GOAL)
        optionList.add(WriteOption(title = "목표 달성 횟수", content = "10"))
        showOptionView = null
    }


    if (showOptionView == null) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (appBar, contents, option) = createRefs()
            val nextButtonClickable = remember { mutableStateOf(false) }

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


                        }
                    }
                })

            Column(modifier = Modifier.constrainAs(contents) {
                top.linkTo(appBar.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(option.top)
                height = Dimension.fillToConstraints
            }) {

                WriteTitleView(modifier = Modifier
                    .fillMaxWidth()
                    .padding(28.dp),
                    title = title.value,
                    textChanged = {
                        title.value = it
                        nextButtonClickable.value = it.isNotEmpty()
                    })

                if (originMemo.value.isNotEmpty()) {
                    MemoOptionView(modifier = Modifier.padding(top = 28.dp),
                        memoText = originMemo.value,
                        memoDelete = {
                            originMemo.value = ""
                            currentClickedOptions.remove(MEMO)
                        })
                }

                // TODO : 카메라 이미지 뷰 정의
                ImageScreen(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 28.dp, start = 28.dp, end = 28.dp),
                    imageList = imageList,
                    state = rememberLazyGridState(),
                    deleteImage = {
                        imageList.remove(it)
                    })

                OptionScreen(options = optionList)
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