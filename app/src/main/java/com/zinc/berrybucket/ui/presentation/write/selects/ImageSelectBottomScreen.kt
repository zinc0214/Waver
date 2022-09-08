package com.zinc.berrybucket.ui.presentation.write.selects

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zinc.berrybucket.R
import com.zinc.berrybucket.ui.design.theme.Gray9
import com.zinc.berrybucket.ui.presentation.ActionWithActivity
import com.zinc.berrybucket.ui.util.dpToSp

@Composable
fun ImageSelectBottomScreen(
    selectedType: (ActionWithActivity.AddImageType) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp, start = 28.dp, end = 28.dp, bottom = 28.dp)
    ) {

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    selectedType(ActionWithActivity.AddImageType.GALLERY)
                }
                .padding(vertical = 16.dp),
            text = stringResource(id = R.string.selectGallery),
            fontSize = dpToSp(dp = 15.dp),
            color = Gray9,
        )

        Text(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .clickable {
                    selectedType(ActionWithActivity.AddImageType.CAMERA)
                },
            text = stringResource(id = R.string.selectCamera),
            fontSize = dpToSp(dp = 15.dp),
            color = Gray9,
        )
    }
}

@Preview
@Composable
private fun ImageSelectBottomScreenPreview() {
    ImageSelectBottomScreen {

    }
}