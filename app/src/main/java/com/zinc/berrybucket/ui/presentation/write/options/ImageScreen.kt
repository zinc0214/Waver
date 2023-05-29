package com.zinc.berrybucket.ui.presentation.write.options

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberAsyncImagePainter
import com.zinc.berrybucket.R
import com.zinc.berrybucket.model.UserSelectedImageInfo
import com.zinc.berrybucket.ui.design.theme.Gray1
import com.zinc.berrybucket.ui.design.theme.Gray2
import com.zinc.berrybucket.ui.design.theme.Gray3
import com.zinc.berrybucket.ui.presentation.common.IconButton

@Composable
fun ImageScreen(
    modifier: Modifier = Modifier,
    images: List<UserSelectedImageInfo>,
    deleteImage: ((UserSelectedImageInfo) -> Unit)? = null
) {

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 80.dp),
        state = rememberLazyGridState(),
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(32.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        items(items = images, key = { it.parseKey() }) { imageInfo ->
            ImageItem(imageInfo, deleteImage)
        }
    }
}

@Composable
fun ImageItem(
    imageInfo: UserSelectedImageInfo,
    deleteImage: ((UserSelectedImageInfo) -> Unit)? = null
) {
    ConstraintLayout(
        modifier = Modifier
            .size(80.dp)
            .clip(RoundedCornerShape(10.dp))
    ) {
        val (icon, image) = createRefs()

        Image(
            modifier = Modifier
                .padding(0.dp)
                .clip(RoundedCornerShape(10.dp))
                .constrainAs(image) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
            painter = rememberAsyncImagePainter(model = imageInfo.uri),
            contentDescription = "Captured image"
        )


        if (deleteImage != null) {
            IconButton(
                onClick = { deleteImage(imageInfo) },
                image = R.drawable.btn_12_close,
                modifier = Modifier
                    .padding(5.dp)
                    .background(
                        color = Gray1.copy(alpha = 0.7f), shape = CircleShape
                    )
                    .then(Modifier.size(20.dp))
                    .constrainAs(icon) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                    },
                contentDescription = "닫기"
            )
        }

    }
}

@Composable
fun AddImageItem(
    addButtonClicked: () -> Unit
) {
    Card(
        backgroundColor = Gray3,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, Gray2),
        elevation = 0.dp,
        modifier = Modifier
            .size(80.dp)
            .clickable {
                addButtonClicked()
            }
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                painter = painterResource(id = R.drawable.btn_20_add),
                contentDescription = stringResource(id = R.string.addImageDesc)
            )
        }
    }

}

@Preview
@Composable
private fun AddImageItemPreview() {
    AddImageItem({

    })
}