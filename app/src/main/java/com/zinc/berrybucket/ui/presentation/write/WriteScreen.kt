package com.zinc.berrybucket.ui.presentation.write

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import com.zinc.berrybucket.R

@Composable
fun WriteScreen(
    goToBack: () -> Unit
) {
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
            rightText = R.string.writeGoToNext,
            clickEvent = {
                when (it) {
                    WriteAppBarClickEvent.CloseClicked -> {
                        goToBack()
                    }
                    WriteAppBarClickEvent.NextClicked -> {


                    }
                }
            }
        )
    }
}