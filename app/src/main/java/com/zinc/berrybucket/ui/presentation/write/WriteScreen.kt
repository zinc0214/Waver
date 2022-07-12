package com.zinc.berrybucket.ui.presentation.write

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.zinc.berrybucket.R
import com.zinc.berrybucket.ui.presentation.write.BottomOptionType.*

@Composable
fun WriteScreen(
    goToBack: () -> Unit
) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (appBar, contents, option) = createRefs()

        val nextButtonClickable = remember { mutableStateOf(false) }
        val title = remember { mutableStateOf("") }
        val currentClickedOptions = remember { mutableStateListOf<BottomOptionType>() }

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
            optionClicked = {
                when (it.first) {
                    MEMO, IMAGE, CATEGORY, D_DAY, GOAL -> {
                        if (it.second) {
                            currentClickedOptions.add(it.first)
                        } else {
                            currentClickedOptions.remove(it.first)
                        }
                    }
                }

                currentClickedOptions.forEachIndexed { index, bottomOptionType ->
                    Log.e("ayhan", "currentClickedOptions : ${bottomOptionType.name}, $index")

                }

            }
        )
    }
}