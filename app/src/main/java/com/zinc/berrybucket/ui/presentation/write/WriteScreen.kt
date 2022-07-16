package com.zinc.berrybucket.ui.presentation.write

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.zinc.berrybucket.R
import com.zinc.berrybucket.ui.presentation.ActionWithActivity
import com.zinc.berrybucket.ui.presentation.CameraPermission
import com.zinc.berrybucket.ui.presentation.write.BottomOptionType.*
import com.zinc.berrybucket.ui.presentation.write.options.MemoScreen

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WriteScreen(
    action: (ActionWithActivity) -> Unit,
    goToBack: () -> Unit
) {
    val context = LocalContext.current

    var showOptionView: BottomOptionType? by remember { mutableStateOf(null) }
    val currentClickedOptions = remember { mutableStateListOf<BottomOptionType>() }
    val originMemo = remember { mutableStateOf("") }
    val title = remember { mutableStateOf("") }

    val isPermissionAgreed = remember { mutableStateOf(false) }
    val imageList = remember { mutableStateListOf<Uri>() }

    if (showOptionView == BottomOptionType.MEMO) {
        MemoScreen(
            originMemo = originMemo.value,
            memoChanged = {
                originMemo.value = it
                currentClickedOptions.add(MEMO)
                showOptionView = null
            }, closeClicked = {
                showOptionView = null
            })
    }

    if (showOptionView == BottomOptionType.IMAGE) {
        // 카메라인지 갤러리인지 확인필요
        CameraPermission(modifier = Modifier,
            isAvailable = {
                if (it) {
                    action(ActionWithActivity.AddImage(
                        type = ActionWithActivity.AddImageType.GALLERY,
                        failed = {
                            Toast.makeText(context, "카메라 실패", Toast.LENGTH_SHORT).show()
                            showOptionView = null
                        },
                        succeed = { uri ->
                            imageList.add(uri)
                            showOptionView = null
                        }
                    ))
                }
            })
        // 갤러리를 선택한 경우
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

                WriteTitleView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(28.dp),
                    title = title.value,
                    onImeAction = {
                        title.value = it
                    })

                if (originMemo.value.isNotEmpty()) {
                    MemoOptionView(
                        modifier = Modifier.padding(top = 28.dp),
                        memoText = originMemo.value,
                        memoDelete = {
                            originMemo.value = ""
                            currentClickedOptions.remove(MEMO)
                        })
                }

                // TODO : 카메라 이미지 뷰 정의
                imageList.forEach {
                    Image(
                        modifier = Modifier.size(80.dp),
                        painter = rememberAsyncImagePainter(model = it),
                        contentDescription = "Captured image"
                    )
                }
            }

            BottomOptionView(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(
                        option
                    ) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    },
                currentClickedOptions = currentClickedOptions,
                optionUsed = {
                    when (it) {
                        MEMO, IMAGE, CATEGORY, D_DAY, GOAL -> {
                            showOptionView = it
                        }
                    }
                }
            )
        }
    }
}