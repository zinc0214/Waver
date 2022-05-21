package com.zinc.berrybucket.compose.ui.my

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.zinc.berrybucket.presentation.my.viewModel.MyViewModel

@Composable
fun My(
    modifier: Modifier = Modifier,
    key: String

) {
    val viewModel: MyViewModel = hiltViewModel()
    viewModel.loadProfile()

    val profileInfo by viewModel.profileInfo.observeAsState()

    Column(
        modifier = modifier
    ) {
        profileInfo?.let {
            MyTopLayer(profileInfo = it)
        }
    }
}