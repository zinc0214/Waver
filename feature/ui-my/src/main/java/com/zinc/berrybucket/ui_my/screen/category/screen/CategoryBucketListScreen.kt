package com.zinc.berrybucket.ui_my.screen.category.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zinc.berrybucket.model.MyTabType
import com.zinc.berrybucket.model.UICategoryInfo
import com.zinc.berrybucket.ui.design.theme.Gray2
import com.zinc.berrybucket.ui.presentation.component.TitleIconType
import com.zinc.berrybucket.ui.presentation.component.TitleView
import com.zinc.berrybucket.ui.presentation.component.dialog.ApiFailDialog
import com.zinc.berrybucket.ui.viewmodel.CategoryViewModel
import com.zinc.berrybucket.ui_common.R
import com.zinc.berrybucket.ui_my.SimpleBucketCard
import com.zinc.berrybucket.ui_my.screen.category.component.MyCategoryBucketListBlankView
import com.zinc.berrybucket.ui_my.viewModel.MyViewModel

@Composable
fun CategoryBucketListScreen(
    _categoryInfo: UICategoryInfo,
    onBackPressed: () -> Unit,
    bucketItemClicked: (String, Boolean) -> Unit,
    goToAddBucket: () -> Unit,
    viewModel: CategoryViewModel = hiltViewModel(),
    myViewModel: MyViewModel = hiltViewModel()
) {

    val _bucketList by viewModel.categoryBucketList.observeAsState()
    val apiFailed by viewModel.apiFailed2.observeAsState()

    val categoryInfo = remember {
        mutableStateOf(_categoryInfo)
    }
    val bucketList = remember {
        mutableStateOf(_bucketList)
    }
    val showApiFail = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = _categoryInfo) {
        categoryInfo.value = _categoryInfo
    }

    LaunchedEffect(key1 = _bucketList) {
        bucketList.value = _bucketList
    }

    LaunchedEffect(key1 = apiFailed) {
        if (apiFailed == true) {
            showApiFail.value = true
        }
    }

    if (bucketList.value.isNullOrEmpty()) {
        viewModel.loadCategoryBucketList(categoryInfo.value.id)
    }

    bucketList.value?.let { data ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Gray2)
        ) {
            TitleView(
                title = categoryInfo.value.name,
                leftIconType = TitleIconType.BACK,
                isDividerVisible = true,
                onLeftIconClicked = {
                    onBackPressed()
                }
            )

            if (data.isEmpty()) {
                MyCategoryBucketListBlankView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    categoryName = _categoryInfo.name
                ) {
                    goToAddBucket()
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(
                        bottom = 60.dp,
                        top = 24.dp,
                        start = 16.dp,
                        end = 16.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(data, key = { _, item -> item.id }) { _, bucket ->
                        SimpleBucketCard(
                            itemInfo = bucket,
                            tabType = MyTabType.ALL,
                            isShowDday = false,
                            itemClicked = { item ->
                                bucketItemClicked.invoke(
                                    item.id,
                                    item.isPrivate()
                                )
                            },
                            achieveClicked = { myViewModel.achieveBucket(it, MyTabType.ALL) }
                        )
                    }
                }
            }
        }
    }

    if (showApiFail.value) {
        ApiFailDialog(
            title = stringResource(R.string.apiFailTitle),
            message = stringResource(id = R.string.apiFailMessage),
            dismissEvent = {
                showApiFail.value = false
                onBackPressed()
            })
    }
}