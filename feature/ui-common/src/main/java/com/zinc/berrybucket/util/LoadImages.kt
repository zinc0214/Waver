package com.zinc.berrybucket.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter
import com.zinc.berrybucket.model.UserSelectedImageInfo
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

@Composable
fun loadImages(images: List<String>): List<UserSelectedImageInfo> {

    return buildList {
        images.forEachIndexed { index, url ->
            val a = loadImage(path = url, index = index)
            Log.e("ayhan", "loadImage aaa  $a")
            add(
                loadImage(url, index)
            )
        }
    }
}

@Composable
fun loadImage(path: String, index: Int): UserSelectedImageInfo {
    val context = LocalContext.current

    val cacheFile = File(context.cacheDir, "${index}_${path}")

    return UserSelectedImageInfo(
        key = index, uri = Uri.parse(path), file = cacheFile, path = path
    )
}

@Composable
fun loadImageFiles(images: List<String>): List<UserSelectedImageInfo> {
    return buildList {
        images.forEachIndexed { index, url ->
            val a = loadImage(path = url, index = index)
            Log.e("ayhan", "loadImage aaa  $a")
            val info = loadImageFile(path = url, index = index)
            info?.let {
                add(it)
            }
        }
    }
}

@Composable
fun loadImageFile(path: String, index: Int): UserSelectedImageInfo? {
    val src = BitmapFactory.decodeFile(path)
    val resized = Bitmap.createScaledBitmap(src, 700, 700, true)
    val imageFile = saveBitmapAsFile(resized, path)

    return getImageFileWithImageInfo(
        photoUri = Uri.parse(path), key = index
    )
}