package com.zinc.berrybucket.compose.ui.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun IconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    @DrawableRes image: Int,
    contentDescription: String

) {
    IconButton(
        onClick = {
            onClick.invoke()
        },
        enabled = enabled,
        modifier = modifier
            .padding(0.dp)
    ) {
        Image(
            painter = painterResource(image),
            contentDescription = contentDescription
        )
    }
}


@Composable
fun IconToggleButton(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    @DrawableRes image: Int,
    contentDescription: String
) {
    androidx.compose.material.IconToggleButton(
        modifier = modifier
            .size(32.dp),
        checked = checked,
        onCheckedChange = {
            onCheckedChange.invoke(it)
        }) {
        Image(
            painter = painterResource(id = image),
            contentDescription = contentDescription
        )
    }
}