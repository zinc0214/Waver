package com.zinc.berrybucket.ui.presentation.detail.screen

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter
import com.zinc.berrybucket.model.UserSelectedImageInfo
import java.io.File

@Composable
internal fun loadImages(images: List<String>): List<UserSelectedImageInfo> {

    val context = LocalContext.current

    return buildList {
        images.forEachIndexed { index, url ->
            val cacheFile = File(context.cacheDir, "${index}_$url")
            Image(
                rememberAsyncImagePainter(cacheFile),
                contentDescription = "...",
            )

            add(
                UserSelectedImageInfo(
                    key = index, uri = Uri.parse(url), file = cacheFile
                )
            )
        }
    }
}