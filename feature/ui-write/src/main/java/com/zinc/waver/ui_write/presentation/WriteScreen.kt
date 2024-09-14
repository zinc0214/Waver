package com.zinc.waver.ui_write.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.zinc.waver.model.DetailDescType
import com.zinc.waver.model.toUpdateUiModel
import com.zinc.waver.ui.presentation.screen.category.CategoryEditScreen
import com.zinc.waver.ui_write.model.WriteEvent
import com.zinc.waver.ui_write.viewmodel.WriteBucketListViewModel
import com.zinc.waver.util.createImageInfoWithPath

@Composable
fun WriteScreen(
    id: String? = null,
    event: (WriteEvent) -> Unit,
    addBucketSucceed: () -> Unit
) {
    val context = LocalContext.current

    val writeBucketListViewModel: WriteBucketListViewModel = hiltViewModel()
    val savedWriteDataAsSate by writeBucketListViewModel.savedWriteData.observeAsState()
    val prevWriteDataForUpdateAsState by writeBucketListViewModel.prevWriteDataForUpdate.observeAsState()

    val pageNumber = remember {
        mutableIntStateOf(1)
    }

    val originWriteTotalInfo = remember {
        mutableStateOf(savedWriteDataAsSate)
    }

    val needToShowCategory = remember {
        mutableStateOf(false)
    }

    if (originWriteTotalInfo.value == null) {
        writeBucketListViewModel.getBucketDetailData(id.orEmpty())
    }

    LaunchedEffect(key1 = savedWriteDataAsSate) {
        originWriteTotalInfo.value = savedWriteDataAsSate
    }

    LaunchedEffect(key1 = prevWriteDataForUpdateAsState) {
        if (prevWriteDataForUpdateAsState != null) {
            val info = prevWriteDataForUpdateAsState
            val imageInfo = DetailDescType.ImageInfo(info?.images.orEmpty())
            val imageToUiModel =
                createImageInfoWithPath(context = context, images = imageInfo.imageList)

            writeBucketListViewModel.savedWriteData(info?.toUpdateUiModel(imageToUiModel))
        }
    }

    originWriteTotalInfo.value?.let { info ->
        if (pageNumber.intValue == 1) {
            WriteScreen1(
                originWriteTotalInfo = info,
                event = {
                    if (it is WriteEvent.GoToAddCategory) {
                        writeBucketListViewModel.savedWriteData(originWriteTotalInfo.value)
                        needToShowCategory.value = true
                    } else {
                        event(it)
                    }

                },
                goToNextPage = {
                    originWriteTotalInfo.value = it
                    pageNumber.intValue = 2
                })
        } else if (pageNumber.intValue == 2) {
            WriteScreen2(
                writeTotalInfo = info,
                viewModel = writeBucketListViewModel,
                goToBack = {
                    originWriteTotalInfo.value = it
                    pageNumber.intValue = 1
                },
                addBucketSucceed = {
                    addBucketSucceed()
                }
            )
        }
    }

    if (needToShowCategory.value) {
        CategoryEditScreen(backClicked = { needToShowCategory.value = false })
    }
}