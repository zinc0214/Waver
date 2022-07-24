package com.zinc.berrybucket.ui.presentation.write

import android.util.Log
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.zinc.berrybucket.R
import com.zinc.berrybucket.model.WriteAddOption
import com.zinc.berrybucket.model.WriteImageInfo
import com.zinc.berrybucket.model.WriteInfo1
import com.zinc.berrybucket.model.WriteResultInfo
import com.zinc.berrybucket.ui.compose.theme.Gray2
import com.zinc.berrybucket.ui.presentation.write.options.ImageScreen


// TODO : 백키를 통해 이동시킬 때 데이터를 가져가지 않고 그냥 돌아가는 문제 해결 필요
@Composable
fun WriteScreen2(
    modifier: Modifier = Modifier,
    writeInfo1: WriteInfo1,
    goToBack: (WriteInfo1) -> Unit,
    goToAddBucket: (WriteResultInfo) -> Unit
) {
    ConstraintLayout(modifier = modifier.fillMaxSize()) {

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
                        val newImage = mutableListOf<WriteImageInfo>()
                        writeInfo1.images.forEach { info ->
                            val image = info.copy(key = info.intKey() + writeInfo1.images.size)
                            newImage.add(image)
                        }
                        val newInfo = writeInfo1.copy(images = newImage)
                        Log.e("ayhan", "newInfo :$newInfo")
                        goToBack(newInfo)
                        // TODO : 이미지
                    }
                    WriteAppBarClickEvent.NextClicked -> {
                        // goToAddBucket(WriteResultInfo())
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

            ImageScreen(images = writeInfo1.images)

            // TODO : Flexible 로 수정 필요
            optionsList.forEach {
                WriteAddOptionView(
                    modifier = Modifier.fillMaxWidth(),
                    option = it, isLastItem = it == optionsList.last()
                )
            }

            Divider(color = Gray2, thickness = 8.dp)
        }
    }
}
