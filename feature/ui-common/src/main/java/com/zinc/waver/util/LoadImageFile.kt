package com.zinc.waver.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.zinc.waver.model.UserSelectedImageInfo
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

fun getImageFileWithImageInfo(photoUri: Uri, key: Int): UserSelectedImageInfo? {
    if (photoUri.path.isNullOrEmpty()) {
        return null
    }
    val src = BitmapFactory.decodeFile(photoUri.path)
    val resized = Bitmap.createScaledBitmap(src, 700, 700, true)
    val imageFile = saveBitmapAsFile(resized, photoUri.path!!)
    return UserSelectedImageInfo(
        key = key,
        uri = photoUri,
        file = imageFile,
        path = photoUri.path!!
    )
}

fun saveBitmapAsFile(bitmap: Bitmap?, filePath: String): File {
    val file = File(filePath)
    val os: OutputStream
    try {
        file.createNewFile()
        os = FileOutputStream(file)
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 80, os)
        os.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return file
}