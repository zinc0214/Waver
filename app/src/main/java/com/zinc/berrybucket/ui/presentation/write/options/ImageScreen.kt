package com.zinc.berrybucket.ui.presentation.write.options

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberAsyncImagePainter
import com.zinc.berrybucket.R
import com.zinc.berrybucket.model.WriteImageInfo
import com.zinc.berrybucket.ui.compose.theme.Gray1
import com.zinc.berrybucket.ui.presentation.common.IconButton

@Composable
fun ImageScreen(
    modifier: Modifier = Modifier,
    imageInfo: WriteImageInfo,
    deleteImage: ((WriteImageInfo) -> Unit)? = null
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