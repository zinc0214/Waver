package com.zinc.berrybucket.ui.presentation.write

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.zinc.berrybucket.R
import com.zinc.berrybucket.model.WriteInfo1
import com.zinc.berrybucket.model.WriteResultInfo
import com.zinc.berrybucket.ui.presentation.common.gridItems
import com.zinc.berrybucket.ui.presentation.write.options.ImageScreen

@Composable
fun WriteScreen2(
    modifier: Modifier = Modifier,
    writeInfo1: WriteInfo1,
    goToBack: () -> Unit,
    goToAddBucket: (WriteResultInfo) -> Unit
) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {

        val (appBar, contents) = createRefs()
        val nextButtonClickable = remember { mutableStateOf(false) }

        WriteAppBar(modifier = Modifier
            .fillMaxWidth()
            .constrainAs(appBar) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            nextButtonClickable = nextButtonClickable.value,
            rightText = R.string.writeGoToFinish,
            clickEvent = {
                when (it) {
                    WriteAppBarClickEvent.CloseClicked -> {
                        goToBack()
                    }
                    WriteAppBarClickEvent.NextClicked -> {
                        // go to write
                    }
                }
            })

        LazyColumn(
            modifier = Modifier.constrainAs(contents) {
                top.linkTo(appBar.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
                height = Dimension.fillToConstraints
            }
        ) {
            item {
                WriteTitleView(
                    modifier = Modifier.padding(bottom = if (writeInfo1.images.isEmpty()) 40.dp else 150.dp),
                    title = writeInfo1.title
                )
            }

            gridItems(
                data = writeInfo1.images,
                columnCount = 3,
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                modifier = Modifier
                    .padding(horizontal = 28.dp)
                    .padding(top = 28.dp)
            ) { itemData ->
                ImageScreen(imageInfo = itemData)
            }
        }

    }
}