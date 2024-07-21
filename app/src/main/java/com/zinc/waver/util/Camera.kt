package com.zinc.waver.util

import android.annotation.SuppressLint
import android.content.Context
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


@SuppressLint("SimpleDateFormat")
@Throws(IOException::class)
fun createImageFile(context: Context): File {
    val timeStamp = SimpleDateFormat("HHmmss").format(Date())
    val imageFileName = "zinc_" + timeStamp + "_"
    val storageDir = File("${context.getExternalFilesDir(null)}/br/")
    if (!storageDir.exists()) {
        storageDir.mkdirs()
    }
    return File.createTempFile(imageFileName, ".jpg", storageDir)
}
