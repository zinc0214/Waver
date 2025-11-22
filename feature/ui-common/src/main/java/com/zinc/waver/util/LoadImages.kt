package com.zinc.waver.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.scale
import androidx.core.net.toUri
import coil.ImageLoader
import coil.request.ImageRequest
import com.zinc.waver.model.LoadedImageInfo
import java.io.File

// 로컬파일 저장 목적
fun createImageInfoWithPath(context: Context, images: List<String>): List<LoadedImageInfo> {

    return buildList {
        images.forEachIndexed { index, url ->
            val a = createImageInfoWithPath(context = context, path = url, index = index)
            Log.e("ayhan", "loadImage aaa  $a")
            add(
                createImageInfoWithPath(context, url, index)
            )
        }
    }
}

fun createImageInfoWithPath(context: Context, path: String, index: Int): LoadedImageInfo {
    val cacheFile = File(context.cacheDir, "${index}_${path}")

    return LoadedImageInfo(
        key = index, uri = path.toUri(), file = cacheFile, path = path
    )
}

// 서버의 Url 을 받아서 저정하는 목적
fun loadImageFiles(context: Context, images: List<String>): List<LoadedImageInfo> {
    return buildList {
        images.forEachIndexed { index, url ->
            val a = createImageInfoWithPath(context = context, path = url, index = index)
            Log.e("ayhan", "loadImage aaa2  $a")
            val info = loadImageFile(path = url, index = index)
            info?.let {
                add(it)
            }
        }
    }
}

fun loadImageFile(path: String, index: Int): LoadedImageInfo? {
    val src = BitmapFactory.decodeFile(path) ?: return null

    val resized = src.scale(700, 700)
    val imageFile = saveBitmapAsFile(resized, path)

    return getImageFileWithImageInfo(
        photoUri = imageFile.toUri(), key = index
    )
}


// Coil을 사용해 서버에서 이미지를 내려받아 캐시에 저장하고 UserSelectedImageInfo로 반환하는 suspend 함수입니다.
// Coil이 이미 프로젝트에서 사용되므로 우선 Coil 기반 구현을 제공합니다.
suspend fun downloadImageWithCoil(
    context: Context,
    imageUrl: String,
    index: Int
): LoadedImageInfo? {
    return try {
        val loader = ImageLoader.Builder(context).build()
        val request = ImageRequest.Builder(context)
            .data(imageUrl)
            .allowHardware(false) // to ensure we can get a mutable bitmap
            .build()

        val result = loader.execute(request)
        val drawable = result.drawable ?: return null

        // Drawable -> Bitmap 변환
        val bitmap: Bitmap = when (drawable) {
            is BitmapDrawable -> drawable.bitmap
            else -> drawable.toBitmap(
                width = drawable.intrinsicWidth.coerceAtLeast(1),
                height = drawable.intrinsicHeight.coerceAtLeast(1)
            )
        }

        val resized = bitmap.scale(700, 700)
        val safeName = imageUrl.toUri().lastPathSegment ?: "image_$index.jpg"
        val cacheFile = File(context.cacheDir, "${index}_$safeName")

        saveBitmapAsFile(resized, cacheFile.absolutePath)

        LoadedImageInfo(
            key = index,
            uri = cacheFile.toUri(),
            file = cacheFile,
            path = cacheFile.absolutePath
        )
    } catch (e: Exception) {
        Log.e("LoadImages", "downloadImageWithCoil failed for $imageUrl", e)
        null
    }
}

// 여러 URL을 받아 일괄 다운로드하고 UserSelectedImageInfo 리스트로 반환합니다.
suspend fun downloadImagesWithCoil(context: Context, images: List<String>): List<LoadedImageInfo> {
    return buildList {
        images.forEachIndexed { index, url ->
            val info = downloadImageWithCoil(context, url, index)
            info?.let { add(it) }
        }
    }
}