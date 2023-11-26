package com.zinc.berrybucket.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.zinc.berrybucket.model.UserSelectedImageInfo
import java.io.File

// 로컬파일 저장 목적
fun createImageInfoWithPath(context: Context, images: List<String>): List<UserSelectedImageInfo> {

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

fun createImageInfoWithPath(context: Context, path: String, index: Int): UserSelectedImageInfo {
    val cacheFile = File(context.cacheDir, "${index}_${path}")

    return UserSelectedImageInfo(
        key = index, uri = Uri.parse(path), file = cacheFile, path = path
    )
}

// 서버의 Url 을 받아서 저정하는 목적
fun loadImageFiles(context: Context, images: List<String>): List<UserSelectedImageInfo> {
    return buildList {
        images.forEachIndexed { index, url ->
            val a = createImageInfoWithPath(context = context, path = url, index = index)
            Log.e("ayhan", "loadImage aaa  $a")
            val info = loadImageFile(path = url, index = index)
            info?.let {
                add(it)
            }
        }
    }
}

fun loadImageFile(path: String, index: Int): UserSelectedImageInfo? {
    val src = BitmapFactory.decodeFile(path) ?: return null

    val resized = Bitmap.createScaledBitmap(src, 700, 700, true)
    val imageFile = saveBitmapAsFile(resized, path)

    return getImageFileWithImageInfo(
        photoUri = Uri.parse(path), key = index
    )
}