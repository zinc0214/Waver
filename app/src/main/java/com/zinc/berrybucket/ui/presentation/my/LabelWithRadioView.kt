package com.zinc.berrybucket.ui.presentation.my

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zinc.berrybucket.R
import com.zinc.berrybucket.ui.design.theme.Gray7
import com.zinc.berrybucket.ui.design.theme.Main4
import com.zinc.berrybucket.ui.util.dpToSp

@Composable
fun LabelWithRadioView(
    modifier: Modifier = Modifier,
    itemLabels: List<Pair<Int, Int>>,
    selectedIndex: Int,
    changedSelectedItem: (Int) -> Unit
) {

    val selectedItemIndex = remember {
        mutableStateOf(selectedIndex)
    }

    itemLabels.forEach {
        RadioItemView(
            modifier = modifier,
            item = it,
            isSelected = it.first == selectedItemIndex.value,
            changedSelectedItem = { index ->
                selectedItemIndex.value = index
                changedSelectedItem(selectedItemIndex.value)
            })
    }
}

@Composable
private fun RadioItemView(
    modifier: Modifier = Modifier,
    item: Pair<Int, Int>,
    isSelected: Boolean,
    changedSelectedItem: (Int) -> Unit
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .padding(horizontal = 28.dp)
        .padding(bottom = 20.dp)
        .clickable {
            changedSelectedItem.invoke(item.first)
        }) {
        Text(
            text = stringResource(id = item.second),
            color = if (isSelected) Main4 else Gray7,
            fontSize = dpToSp(16.dp),
        )
        Spacer(modifier = Modifier.width(8.dp))
        if (isSelected) {
            Image(
                painter = painterResource(id = R.drawable.radio_check),
                contentDescription = stringResource(
                    id = R.string.selected
                )
            )
        }
    }
}

@Composable
@Preview
private fun LabelWithRadioView() {
    LabelWithRadioView(modifier = Modifier,
        itemLabels = listOf(0 to R.string.sortByUpdate, 1 to R.string.sortByCreate),
        selectedIndex = 0,
        changedSelectedItem = {})
}