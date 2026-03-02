package com.zinc.waver.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.createBitmap
import androidx.core.net.toUri
import com.zinc.waver.ui_common.R
import com.zinc.waver.util.FileUtil.getFileFromUri
import java.io.File
import java.io.FileOutputStream

object RandomProfileImageUtil {
    private val profileIcons = listOf(
        R.drawable.profile_icon_1,
        R.drawable.profile_icon_2,
        R.drawable.profile_icon_3
    )

    fun getRandomProfileImageFile(context: Context): File? {
        val randomProfile = profileIcons.random()
        val uri = ("android.resource://" + context.packageName + "/" + randomProfile).toUri()
        val file = try {
            getFileFromUri(context, uri)
        } catch (e: Exception) {
            null
        }
        if (file != null) {
            val decoded = try {
                BitmapFactory.decodeFile(file.path)
            } catch (_: Throwable) {
                null
            }
            if (decoded != null) {
                try {
                    if (!decoded.isRecycled) decoded.recycle()
                } catch (_: Exception) {
                }
                return file
            } else {
                val drawable: Drawable? = ResourcesCompat.getDrawable(
                    context.resources,
                    randomProfile,
                    context.theme
                )
                if (drawable != null) {
                    val bmp = drawableToBitmap(drawable)
                    val outFile = File(context.cacheDir, "random_profile_${randomProfile}.png")
                    FileOutputStream(outFile).use { fos ->
                        bmp.compress(Bitmap.CompressFormat.PNG, 100, fos)
                        fos.flush()
                    }
                    try {
                        if (!bmp.isRecycled) bmp.recycle()
                    } catch (_: Exception) {
                    }
                    return outFile
                }
            }
        }
        return null
    }

    private fun drawableToBitmap(
        drawable: Drawable,
        reqWidth: Int = -1,
        reqHeight: Int = -1
    ): android.graphics.Bitmap {
        val width = when {
            reqWidth > 0 -> reqWidth
            drawable.intrinsicWidth > 0 -> drawable.intrinsicWidth
            else -> 512
        }
        val height = when {
            reqHeight > 0 -> reqHeight
            drawable.intrinsicHeight > 0 -> drawable.intrinsicHeight
            else -> 512
        }
        val bitmap = createBitmap(width, height)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}
