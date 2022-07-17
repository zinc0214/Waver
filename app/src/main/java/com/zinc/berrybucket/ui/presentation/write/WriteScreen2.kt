package com.zinc.berrybucket.ui.presentation.write

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.zinc.berrybucket.R
import com.zinc.berrybucket.model.WriteAddOption
import com.zinc.berrybucket.model.WriteInfo1
import com.zinc.berrybucket.model.WriteResultInfo
import com.zinc.berrybucket.ui.presentation.write.options.ImageScreen

@Composable
fun WriteScreen2(
    modifier: Modifier = Modifier,
    writeInfo1: WriteInfo1,
    goToBack: (WriteInfo1) -> Unit,
    goToAddBucket: (WriteResultInfo) -> Unit
) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {

        val (appBar, contents) = createRefs()
        val optionsList = listOf(
            WriteAddOption(
                title = "키워드 추가",
                tagList = listOf(
                    "#제주도",
                    "#서울",
                    "#2e3제주도",
                    "#1서울",
                    "#2제주도가나나나나나",
                    "#3서울",
                    "#4서울",
                    "#5제주도",
                    "6#서울",
                    "#7서울",
                    "#8제주도",
                    "#9서울"
                ),
                clicked = {}),
            WriteAddOption(title = "함께할 친구 추가하기", tagList = listOf("aaa"), clicked = {})
        )

        WriteAppBar(modifier = Modifier
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
                        goToBack(writeInfo1)
                    }
                    WriteAppBarClickEvent.NextClicked -> {
                        // go to write
                    }
                }
            })

        val state = rememberLazyGridState()

        Column(
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
                )
        ) {
            WriteTitleView(
                modifier = Modifier.padding(bottom = if (writeInfo1.images.isEmpty()) 150.dp else 32.dp),
                title = writeInfo1.title
            )

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 60.dp), state = rememberLazyGridState(),
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                userScrollEnabled = false,
                modifier = Modifier
                    .padding(horizontal = 28.dp)
                    .padding(bottom = 40.dp)
            ) {
                items(items = writeInfo1.images) { image ->
                    ImageScreen(imageInfo = image)
                }
            }

            // TODO : Flexible 로 수정 필요
            optionsList.forEach {
                WriteAddOptionView(
                    modifier = Modifier.fillMaxWidth(),
                    option = it, isLastItem = it == optionsList.last()
                )
            }
        }
    }
}
