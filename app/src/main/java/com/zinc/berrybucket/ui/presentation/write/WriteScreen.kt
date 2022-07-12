package com.zinc.berrybucket.ui.presentation.write

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.zinc.berrybucket.R
import com.zinc.berrybucket.ui.presentation.write.BottomOptionType.*
import com.zinc.berrybucket.ui.presentation.write.options.MemoScreen

@Composable
fun WriteScreen(
    goToBack: () -> Unit
) {
    var showOptionView: BottomOptionType? by remember { mutableStateOf(null) }
    val currentClickedOptions = remember { mutableStateListOf<BottomOptionType>() }
    val originMemo = remember { mutableStateOf("") }
    val title = remember { mutableStateOf("") }

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

    if (showOptionView == null) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (appBar, contents, option) = createRefs()
            val nextButtonClickable = remember { mutableStateOf(false) }

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